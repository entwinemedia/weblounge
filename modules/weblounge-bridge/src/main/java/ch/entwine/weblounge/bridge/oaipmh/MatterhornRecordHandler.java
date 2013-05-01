package ch.entwine.weblounge.bridge.oaipmh;

import ch.entwine.weblounge.bridge.oaipmh.harvester.HarvesterException;
import ch.entwine.weblounge.bridge.oaipmh.harvester.ListRecordsResponse;
import ch.entwine.weblounge.bridge.oaipmh.harvester.RecordHandler;
import ch.entwine.weblounge.common.content.Resource;
import ch.entwine.weblounge.common.content.image.ImageStyle;
import ch.entwine.weblounge.common.content.movie.MovieContent;
import ch.entwine.weblounge.common.impl.content.image.ImageStyleImpl;
import ch.entwine.weblounge.common.impl.content.image.ImageStyleUtils;
import ch.entwine.weblounge.common.impl.content.movie.MovieContentImpl;
import ch.entwine.weblounge.common.impl.content.movie.MovieResourceImpl;
import ch.entwine.weblounge.common.impl.content.movie.MovieResourceURIImpl;
import ch.entwine.weblounge.common.impl.content.movie.VideoStreamImpl;
import ch.entwine.weblounge.common.language.Language;
import ch.entwine.weblounge.common.repository.WritableContentRepository;
import ch.entwine.weblounge.common.security.User;
import ch.entwine.weblounge.common.site.Site;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.opencastproject.mediapackage.Attachment;
import org.opencastproject.mediapackage.AudioStream;
import org.opencastproject.mediapackage.Catalog;
import org.opencastproject.mediapackage.MediaPackage;
import org.opencastproject.mediapackage.MediaPackageBuilder;
import org.opencastproject.mediapackage.MediaPackageBuilderFactory;
import org.opencastproject.mediapackage.MediaPackageElement;
import org.opencastproject.mediapackage.MediaPackageElementFlavor;
import org.opencastproject.mediapackage.MediaPackageException;
import org.opencastproject.mediapackage.Stream;
import org.opencastproject.mediapackage.Track;
import org.opencastproject.mediapackage.VideoStream;
import org.opencastproject.metadata.dublincore.DublinCoreCatalogImpl;
import org.opencastproject.metadata.dublincore.DublinCoreValue;
import org.opencastproject.util.MimeType;
import org.opencastproject.util.MimeTypes;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A Matterhorn specified implementation of a record handler. This class is not
 * thread safe.
 */
public class MatterhornRecordHandler extends AbstractWebloungeRecordHandler implements RecordHandler {

  public static final String MATTERHORN_HARVESTING = "Matterhorn-harvested";
  
  /** Name of the oai pmh prefix */
  private static final String MATTERHORN_REPOSITORY_PREFIX = "matterhorn";

  /** The media package builder */
  private MediaPackageBuilder mediaPackageBuilder;

  /**
   * Creates a new matterhorn record handler
   * 
   * @param site
   *          the site
   * @param contentRepository
   *          the content repository
   * @param harvesterUser
   *          the harvester user
   * @param presentationTrackFlavor
   *          the presentation track flavor
   * @param presenterTrackFlavor
   *          the presenter track flavor
   * @param dcEpisodeFlavor
   *          the dublin core episode flavor
   * @param dcSeriesFlavor
   *          the dublin core series flavor
   * @param mimeTypesStr
   * 		  the string containing the list of mime-types separated by a string
   */
  public MatterhornRecordHandler(Site site,
      WritableContentRepository contentRepository, User harvesterUser,
      String presentationTrackFlavor, String presenterTrackFlavor,
      String dcEpisodeFlavor, String dcSeriesFlavor, String mimeTypesStr) {
    super(site, contentRepository, harvesterUser, presentationTrackFlavor, presenterTrackFlavor, dcEpisodeFlavor, dcSeriesFlavor, mimeTypesStr);
    mediaPackageBuilder = MediaPackageBuilderFactory.newInstance().newMediaPackageBuilder();
  }

  /**
   * {@inheritDoc}
   * 
   * @see ch.entwine.weblounge.bridge.oaipmh.RecordHandler#getMetadataPrefix()
   */
  public String getMetadataPrefix() {
    return MATTERHORN_REPOSITORY_PREFIX;
  }

  /**
   * {@inheritDoc}
   * 
   * @see ch.entwine.weblounge.bridge.oaipmh.AbstractWebloungeRecordHandler#parseResource()
   */
  protected Resource<MovieContent> parseResource(Node record) {
    Node mediaPackageNode = ListRecordsResponse.metadataOfRecord(record);
    MediaPackage mediaPackage;
    try {
      mediaPackage = mediaPackageBuilder.loadFromXml(mediaPackageNode);
    } catch (MediaPackageException e) {
      logger.warn("Error loading mediapackage from record");
      throw new RuntimeException(e);
    }

    Language language = getLanguage(mediaPackage.getLanguage());

    MovieResourceImpl movieResource = new MovieResourceImpl(new MovieResourceURIImpl(site));
    movieResource.setCreated(harvesterUser, mediaPackage.getDate());

    // Set Header Metadata
    movieResource.setTitle(mediaPackage.getTitle(), language);
    for (String subject : mediaPackage.getSubjects()) {
      movieResource.addSubject(subject);
    }

    if (StringUtils.isNotBlank(mediaPackage.getSeries()))
      movieResource.addSeries(mediaPackage.getSeries());
    if (StringUtils.isNotBlank(mediaPackage.getSeriesTitle()))
      movieResource.setDescription(mediaPackage.getSeriesTitle(), language);
    
    
    // Copy preview images
    ArrayList<MimeType> imgMimeTypes = new ArrayList<MimeType>();
    imgMimeTypes.add(MimeTypes.JPG);
    imgMimeTypes.add(MimeTypes.parseMimeType("image/jpeg"));
    
    ImageStyle imgStyle = new ImageStyleImpl(MATTERHORN_REPOSITORY_PREFIX);
    Attachment attachement = getAttachementByFlavor(MediaPackageElementFlavor.parseFlavor("presenter/player+preview"), imgMimeTypes, mediaPackage);
    
    if (attachement != null) {
    	for (Language l:site.getLanguages()) {
    		File file = ImageStyleUtils.getScaledFile(movieResource, attachement.getURI().getPath(), l, imgStyle);
    		try {
    			FileUtils.copyURLToFile(attachement.getURI().toURL(), file);
    		} catch (MalformedURLException e) {
    			logger.error("Error copying preview image: {}", e.getMessage());
    			e.printStackTrace();
    		} catch (IOException e) {
    			logger.error("Error copying preview image: {}", e.getMessage());
    			e.printStackTrace();
    		}     		
    	}    	
    }
    
    DublinCoreCatalogImpl episodeDC = null;
    Catalog episodeCatalog = null;
    Catalog[] episodeCatalogs = mediaPackage.getCatalogs(MediaPackageElementFlavor.parseFlavor(dcEpisodeFlavor));
    if(episodeCatalogs.length > 0) {
    	episodeCatalog = episodeCatalogs[0];
    }

    InputStream is = null;
    try {
      is = episodeCatalog.getURI().toURL().openStream();
      episodeDC = new DublinCoreCatalogImpl(is);
    } catch (Exception e) {
      logger.error("Error loading Dublin Core metadata: {}", e.getMessage());
    } finally {
      IOUtils.closeQuietly(is);
    }
    
    if (episodeCatalog != null) {
    	List<DublinCoreValue> descriptions = episodeDC.get(DublinCoreCatalogImpl.PROPERTY_DESCRIPTION);
    	if (descriptions.size() > 0)	
    		movieResource.setDescription(descriptions.get(0).getValue(), site.getDefaultLanguage());		
    }
  
    // Add matterhorn-harvested as subject to help identifying it as harvested resource
    movieResource.addSubject(MATTERHORN_HARVESTING);

    return movieResource;
  }

  /**
   * {@inheritDoc}
   * 
   * @see ch.entwine.weblounge.bridge.oaipmh.AbstractWebloungeRecordHandler#parseResourceContent()
   */
  protected MovieContent parseResourceContent(Node record) {
    Node mediaPackageNode = ListRecordsResponse.metadataOfRecord(record);
    MediaPackage mediaPackage;
    try {
      mediaPackage = mediaPackageBuilder.loadFromXml(mediaPackageNode);
    } catch (MediaPackageException e) {
      logger.warn("Error loading mediapackage from record");
      throw new RuntimeException(e);
    }

    Language language = getLanguage(mediaPackage.getLanguage());

    // TODO: Use tracks with correct flavor for movie
    // ??? presenterTrackFlavor
    // MediaPackageElementFlavor elementFlavor = MediaPackageElementFlavor.parseFlavor(presentationTrackFlavor);

    // Set Content
    MediaPackageElement element = getMediapackageElementByFlavor(MediaPackageElementFlavor.parseFlavor(presentationTrackFlavor), mimeTypes, mediaPackage);
    if (element == null) {
    	element = getMediapackageElementByFlavor(MediaPackageElementFlavor.parseFlavor(presenterTrackFlavor), mimeTypes, mediaPackage);
    	if (element == null)
    		return null;
    }

    MovieContent content = new MovieContentImpl(FilenameUtils.getBaseName(element.getURI().toString()), language, element.getMimeType().toString());
    StringBuilder author = new StringBuilder();
    String[] creators = mediaPackage.getCreators();
    for (int i = 0; i < creators.length; i++) {
      if (i != 0)
        author.append(", ");
      author.append(creators[i]);
    }

    try {
      content.setExternalLocation(element.getURI().toURL());
    } catch (MalformedURLException e) {
      logger.debug("No record url for element {}", element.getIdentifier());
      throw new HarvesterException("No record url for element: " + element.getIdentifier());
    }
    content.setAuthor(author.toString());
    if (element.getSize() != -1)
      content.setSize(element.getSize());
    if (mediaPackage.getDuration() != -1)
      content.setDuration(mediaPackage.getDuration());

    Track track = getTrackByFlavor(MediaPackageElementFlavor.parseFlavor(presentationTrackFlavor), mimeTypes, mediaPackage);
    if (track == null) {
    	track = getTrackByFlavor(MediaPackageElementFlavor.parseFlavor(presenterTrackFlavor), mimeTypes, mediaPackage);
        if (track == null)
            return null;
    }

    for (Stream stream : track.getStreams()) {
      if (stream instanceof AudioStream) {
        ch.entwine.weblounge.common.content.movie.AudioStream audioStream = new ch.entwine.weblounge.common.impl.content.movie.AudioStreamImpl();
        AudioStream matterhornAudioStream = (AudioStream) stream;
        if (matterhornAudioStream.getBitDepth() != null)
          audioStream.setBitDepth(matterhornAudioStream.getBitDepth());
        if (matterhornAudioStream.getBitRate() != null)
          audioStream.setBitRate(matterhornAudioStream.getBitRate());
        if (matterhornAudioStream.getChannels() != null)
          audioStream.setChannels(matterhornAudioStream.getChannels());
        if (StringUtils.isNotBlank(matterhornAudioStream.getFormat()))
          audioStream.setFormat(matterhornAudioStream.getFormat());
        if (matterhornAudioStream.getSamplingRate() != null)
          audioStream.setSamplingRate(matterhornAudioStream.getSamplingRate());
        content.addStream(audioStream);
      } else if (stream instanceof VideoStream) {
        ch.entwine.weblounge.common.content.movie.VideoStream videoStream = new VideoStreamImpl();
        VideoStream matterhornVideoStream = (VideoStream) stream;
        if (matterhornVideoStream.getBitRate() != null)
          videoStream.setBitRate(matterhornVideoStream.getBitRate());
        if (StringUtils.isNotBlank(matterhornVideoStream.getFormat()))
          videoStream.setFormat(matterhornVideoStream.getFormat());
        if (matterhornVideoStream.getFrameHeight() != null)
          videoStream.setFrameHeight(matterhornVideoStream.getFrameHeight());
        if (matterhornVideoStream.getFrameWidth() != null)
          videoStream.setFrameWidth(matterhornVideoStream.getFrameWidth());
        if (matterhornVideoStream.getFrameRate() != null)
          videoStream.setFrameRate(matterhornVideoStream.getFrameRate());
        // if (matterhornVideoStream.getScanType() != null)
        // videoStream.setScanType(ScanType.fromString(matterhornVideoStream.getScanType().toString()));
        content.addStream(videoStream);
      }
    }

    // Use the logged in user as the author
    content.setCreator(harvesterUser);
    content.setCreationDate(mediaPackage.getDate());
    return content;
  }
  
  /**
   * Get the first mediapackage element matching with the given flavor and mimetypes.
   * 
   * @param flavor
   * @param mimeType
   * @param mediaPackage
   * @return the fist matching mediapackage element or null 
   */
  private MediaPackageElement getMediapackageElementByFlavor(MediaPackageElementFlavor flavor, ArrayList<MimeType> mimeTypes, MediaPackage mediaPackage) {
	MediaPackageElement element = null;
	  
    for (MediaPackageElement elem : mediaPackage.getElementsByFlavor(flavor)) {
    	for (MimeType mimeType : mimeTypes) {	
	        if (elem.getMimeType().equals(mimeType)) {
	          element = elem;
	          break;
	        }
    	}
      }
    return element;
  }
  
  /**
   * Get the first track matching with the given flavor and mimetypes.
   * 
   * @param flavor
   * @param mimeType
   * @param mediaPackage
   * @return the fist matching mediapackage element or null 
   */
  private Track getTrackByFlavor(MediaPackageElementFlavor flavor, ArrayList<MimeType> mimeTypes, MediaPackage mediaPackage) {
	Track track = null;
	  
    for (Track t : mediaPackage.getTracks(flavor)) {
    	for (MimeType mimeType : mimeTypes) {	
	        if (t.getMimeType().equals(mimeType)) {
	          track = t;
	          break;
	        }
    	}
      }
    return track;
  }
  
  /**
   * Get the first attachement matching with the given flavor and mimetypes.
   * 
   * @param flavor
   * @param mimeType
   * @param mediaPackage
   * @return the fist matching mediapackage element or null 
   */
  private Attachment getAttachementByFlavor(MediaPackageElementFlavor flavor, ArrayList<MimeType> mimeTypes, MediaPackage mediaPackage) {
	Attachment attachement = null;
	  
    for (Attachment a : mediaPackage.getAttachments(flavor)) {
    	for (MimeType mimeType : mimeTypes) {	
	        if (a.getMimeType().equals(mimeType)) {
	          attachement = a;
	          break;
	        }
    	}
      }
    return attachement;
  }
}
