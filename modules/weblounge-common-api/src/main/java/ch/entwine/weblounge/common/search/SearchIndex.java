package ch.entwine.weblounge.common.search;

import ch.entwine.weblounge.common.content.Resource;
import ch.entwine.weblounge.common.content.ResourceURI;
import ch.entwine.weblounge.common.content.SearchQuery;
import ch.entwine.weblounge.common.content.SearchResult;
import ch.entwine.weblounge.common.repository.ContentRepositoryException;
import ch.entwine.weblounge.common.site.Site;

import java.io.IOException;
import java.util.List;

public interface SearchIndex {

  /** Version of this index */
  int INDEX_VERSION = 3009;

  /**
   * Makes a request and returns the result set.
   * 
   * @param query
   *          the search query
   * @return the result set
   * @throws ContentRepositoryException
   *           if executing the search operation fails
   */
  SearchResult getByQuery(SearchQuery query) throws ContentRepositoryException;

  /**
   * Removes the entry with the given <code>id</code> from the database.
   * 
   * @param resourceId
   *          identifier of the resource or resource
   * @throws ContentRepositoryException
   *           if removing the resource from solr fails
   */
  boolean delete(ResourceURI uri) throws ContentRepositoryException;

  /**
   * Posts the resource to the search index.
   * 
   * @param resource
   *          the resource to add to the index
   * @throws ContentRepositoryException
   *           if posting the new resource to solr fails
   */
  boolean add(Resource<?> resource) throws ContentRepositoryException;

  /**
   * Posts the updated resource to the search index.
   * 
   * @param resource
   *          the resource to update
   * @throws ContentRepositoryException
   *           if posting the updated resource to solr fails
   */
  boolean update(Resource<?> resource) throws ContentRepositoryException;

  /**
   * Move the resource identified by <code>uri</code> to the new location.
   * 
   * @param uri
   *          the resource uri
   * @param path
   *          the new path
   * @return
   */
  boolean move(ResourceURI uri, String path) throws ContentRepositoryException;

  /**
   * Returns the suggestions as returned from the selected dictionary based on
   * <code>seed</code>.
   * 
   * @param dictionary
   *          the dictionary
   * @param seed
   *          the seed used for suggestions
   * @param onlyMorePopular
   *          whether to return only more popular results
   * @param count
   *          the maximum number of suggestions
   * @param collate
   *          whether to provide a query collated with the first matching
   *          suggestion
   */
  List<String> suggest(String dictionary, String seed, boolean onlyMorePopular,
      int count, boolean collate) throws ContentRepositoryException;
  
  /**
   * Clears the search index of the given site.
   * 
   * @param site the site
   * @throws IOException if clearing of the site index fails
   */
  void clear(Site site) throws IOException;

  /**
   * Clears the search indices of all sites.
   * 
   * @throws IOException
   *           if clearing the index fails
   */
  void clear() throws IOException;

  /**
   * Returns the index's version number. If that number is different from
   * {@link #INDEX_VERSION}, a reindex is needed, since the index's structure
   * could have changed significantly.
   * 
   * @return the index version
   */
  int getIndexVersion();

}