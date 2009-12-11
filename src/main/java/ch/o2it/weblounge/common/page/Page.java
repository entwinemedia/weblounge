/*
 *  Weblounge: Web Content Management System
 *  Copyright (c) 2009 The Weblounge Team
 *  http://weblounge.o2it.ch
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software Foundation
 *  Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package ch.o2it.weblounge.common.page;

import ch.o2it.weblounge.common.content.Creatable;
import ch.o2it.weblounge.common.content.LocalizedModifiable;
import ch.o2it.weblounge.common.content.Publishable;
import ch.o2it.weblounge.common.language.Language;
import ch.o2it.weblounge.common.language.Localizable;
import ch.o2it.weblounge.common.security.Permission;
import ch.o2it.weblounge.common.security.Securable;
import ch.o2it.weblounge.common.security.SystemPermission;
import ch.o2it.weblounge.common.user.User;

/**
 * A <code>Page</code> encapsulates all data that is attached with a site url.
 */
public interface Page extends Localizable, Creatable, LocalizedModifiable, Publishable, Securable {

  /** Request page identifier */
  String ID = "page";

  /** Page headlines in request */
  static final String HEADLINES = "headlines";

  /** Live version of a page */
  long LIVE = 0;

  /** Original version of a page */
  long ORIGINAL = 1;

  /** Work version of a page */
  long WORK = 2;

  /** The page's permissions */
  static final Permission[] permissions = new Permission[] {
      SystemPermission.READ,
      SystemPermission.WRITE,
      SystemPermission.TRANSLATE,
      SystemPermission.PUBLISH,
      SystemPermission.MANAGE };

  /**
   * Returns the page uri.
   * 
   * @return the page url
   */
  PageURI getURI();

  /**
   * Returns the page type, which is used to include this page into news lists
   * etc.
   * 
   * @return the page type
   */
  String getType();

  /**
   * Makes this page an anchor page. Specifying a collection of anchor pages
   * throughout a site will allow you to build a sitemap or a list of points of
   * entrance.
   * 
   * @param anchor
   *          <code>true</code> to make this an achor page
   */
  void setAnchorpage(boolean anchor);

  /**
   * Returns <code>true</code> if this page is considered important enough to
   * include it in a sitemap and similar listings.
   * 
   * @return <code>true</code> if this is an anchor page
   */
  boolean isAnchorpage();

  /**
   * Sets this page to be either included or excluded from the search index.
   * Setting this property to <code>false</code> enables pages that can only be
   * found by people who know the link.
   * 
   * @param index
   *          <code>true</code> to have this page indexed
   */
  void setIndexed(boolean index);

  /**
   * Returns <code>true</code> if the page should be added to the search index.
   * 
   * @return <code>true</code> if the page is indexed
   */
  boolean isIndexed();

  /**
   * Returns <code>true</code> if the page is locked.
   * 
   * @return <code>true</code> if this page is locked
   */
  boolean isLocked();

  /**
   * Returns the user holding the editing lock for this page.
   * 
   * @return the user holding the editing lock for this page
   */
  User getLockOwner();

  /**
   * Adds <tt>subject</tt> to the set of subjects if it is not already
   * contained.
   * 
   * @param subject
   *          the subject (or tag) to add
   */
  void addSubject(String subject);

  /**
   * Removes <tt>subject</tt> from the set of subjects.
   * 
   * @param subject
   *          the subject to remove
   */
  void removeSubject(String subject);

  /**
   * Returns the topics that are defined for this page. Page topics are also
   * known as tags.
   * 
   * @return the page topics
   */
  String[] getSubjects();

  /**
   * Returns the page title in the current language.
   * 
   * @return the content
   * @see #switchTo(Language)
   * @see #getTitle(Language)
   * @see #getTitle(Language, boolean)
   */
  String getTitle();

  /**
   * Returns the page title in the specified language or <code>null</code> if
   * this language version is not available.
   * 
   * @param language
   *          the language identifier
   * @return the page title
   * @see #getTitle()
   * @see #getTitle(Language, boolean)
   */
  String getTitle(Language language);

  /**
   * Returns the page title in the specified language. If that title can't be
   * found, it will be looked up in the default language (unless
   * <code>force</code> is set to <code>true</code>). If that doesn't produce a
   * result as well, <code>null</code> is returned.
   * 
   * @param language
   *          the title language
   * @param force
   *          <code>true</code> to force the language
   * @return the content
   * @see #getTitle()
   * @see #getTitle(Language)
   */
  String getTitle(Language language, boolean force);

  /**
   * Sets the page title in the given language.
   * 
   * @param title
   *          the page title
   * @param language
   *          the language
   */
  void setTitle(String title, Language language);

  /**
   * Returns the page description in the current language.
   * 
   * @return the content
   * @see #switchTo(Language)
   * @see #getDescription(Language)
   * @see #getDescription(Language, boolean)
   */
  String getDescription();

  /**
   * Returns the page description in the specified language or <code>null</code>
   * if this language version is not available.
   * 
   * @param language
   *          the language identifier
   * @return the page description
   * @see #getDescription()
   * @see #getDescription(Language, boolean)
   */
  String getDescription(Language language);

  /**
   * Returns the page description in the specified language. If that description
   * can't be found, it will be looked up in the default language (unless
   * <code>force</code> is set to <code>true</code>). If that doesn't produce a
   * result as well, <code>null</code> is returned.
   * 
   * @param language
   *          the description language
   * @param force
   *          <code>true</code> to force the language
   * @return the content
   * @see #getDescription()
   * @see #getDescription(Language)
   */
  String getDescription(Language language, boolean force);

  /**
   * Sets the page description in the given language.
   * 
   * @param description
   *          the page description
   * @param language
   *          the language
   */
  void setDescription(String description, Language language);

  /**
   * Returns the page coverage in the current language.
   * 
   * @return the content
   * @see #switchTo(Language)
   * @see #getCoverage(Language)
   * @see #getCoverage(Language, boolean)
   */
  String getCoverage();

  /**
   * Returns the page coverage in the specified language or <code>null</code> if
   * this language version is not available.
   * 
   * @param language
   *          the language identifier
   * @return the page coverage
   * @see #getCoverage()
   * @see #getCoverage(Language, boolean)
   */
  String getCoverage(Language language);

  /**
   * Returns the page coverage in the specified language. If that coverage can't
   * be found, it will be looked up in the default language (unless
   * <code>force</code> is set to <code>true</code>). If that doesn't produce a
   * result as well, <code>null</code> is returned.
   * 
   * @param language
   *          the coverage language
   * @param force
   *          <code>true</code> to force the language
   * @return the content
   * @see #getCoverage()
   * @see #getCoverage(Language)
   */
  String getCoverage(Language language, boolean force);

  /**
   * Sets the page coverage in the given language.
   * 
   * @param coverage
   *          the page coverage
   * @param language
   *          the language
   */
  void setCoverage(String coverage, Language language);

  /**
   * Returns the page rights in the current language.
   * 
   * @return the content
   * @see #switchTo(Language)
   * @see #getRights(Language)
   * @see #getRights(Language, boolean)
   */
  String getRights();

  /**
   * Returns the page rights in the specified language or <code>null</code> if
   * this language version is not available.
   * 
   * @param language
   *          the language identifier
   * @return the page rights
   * @see #getRights()
   * @see #getRights(Language, boolean)
   */
  String getRights(Language language);

  /**
   * Returns the pages rights declaration in the specified language. If no
   * rights declaration can be found in that language, it will be looked up in
   * the default language (unless <code>force</code> is set to <code>true</code>
   * ). If that doesn't produce a result as well, <code>null</code> is returned.
   * 
   * @param language
   *          the rights declaration language
   * @param force
   *          <code>true</code> to force the language
   * @return the content
   * @see #getRights()
   * @see #getRights(Language)
   */
  String getRights(Language language, boolean force);

  /**
   * Sets the pages rights declaration in the given language.
   * 
   * @param rights
   *          the pages rights declaration
   * @param language
   *          the language
   */
  void setRights(String rights, Language language);

  /**
   * Returns the headline pagelets
   * 
   * @return the headline pagelets
   */
  Pagelet[] getHeadlines();

  /**
   * Sets the layout that should be applied to this page. The layout controls
   * which pagelets to place into a composer by default, which ones to protect
   * and which ones to allow for editing.
   * 
   * @param layout
   *          the page layout
   */
  void setLayout(String layout);

  /**
   * Returns the identifier of the layout associated with this page.
   * 
   * @return the associated layout
   */
  String getLayout();

  /**
   * Sets the page template. The parameter <code>template</code> represents the
   * identifier of a renderer that is used to render the page.
   * 
   * @param template
   *          the template to use
   */
  void setTemplate(String template);

  /**
   * Returns the identifier of the template that is used to render this page.
   * 
   * @return the template
   */
  String getTemplate();

  /**
   * Adds <code>pagelet</code> as the last pagelet in the specified composer and
   * returns it with an updated {@link PageletURI}.
   * 
   * @param pagelet
   *          the pagelet to add
   * @param composer
   *          the composer to put the pagelet
   * @return the updated pagelet
   */
  Pagelet addPagelet(Pagelet pagelet, String composer);

  /**
   * Adds <code>pagelet</code> as the last pagelet in the specified composer and
   * returns it with an updated {@link PageletURI}.
   * 
   * @param pagelet
   *          the pagelet to add
   * @param composer
   *          the composer to put the pagelet
   * @param index
   *          the position where the pagelets needs to be put
   * @return the updated pagelet
   * @throws IndexOutOfBoundsException
   *           if <code>index</code> is either smaller than <code>zero</code> or
   *           equals or larger than the number of pagelets already contained in
   *           the composer
   */
  Pagelet addPagelet(Pagelet pagelet, String composer, int index)
      throws IndexOutOfBoundsException;

  /**
   * Removes the pagelet at position <code>index</code> from the specified
   * composer and returns it.
   * <p>
   * <b>Note:</b> The uris of all subsequent pagelet will be updated with their
   * new position (<code>current - 1</code>).
   * 
   * @param composer
   *          the composer
   * @param index
   *          position of the pagelet within the composer
   * @return the removed pagelet
   * @throws IndexOutOfBoundsException
   *           if <code>index</code> is either smaller than <code>zero</code> or
   *           equals or larger than the number of pagelets already contained in
   *           the composer
   */
  Pagelet removePagelet(String composer, int index)
      throws IndexOutOfBoundsException;

  /**
   * Returns the pagelets that are contained in the specified composer.
   * 
   * @param composer
   *          the composer identifier
   * @return the pagelets
   */
  Pagelet[] getPagelets(String composer);

  /**
   * Returns the pagelets of the given module and renderer that are contained in
   * the specified composer.
   * 
   * @param composer
   *          the composer identifier
   * @param module
   *          the module identifier
   * @param id
   *          the renderer id
   * @return the pagelets
   */
  Pagelet[] getPagelets(String composer, String module, String id);

  /**
   * Adds a <code>PageContentListener</code> to this page, who will be notified
   * (amongst others) about new, moved, deleted or altered pagelets.
   * 
   * @param listener
   *          the new page content listener
   */
  void addPageContentListener(PageContentListener listener);

  /**
   * Removes a <code>PageContentListener</code> from this page.
   * 
   * @param listener
   *          the page content listener
   */
  void removePageContentListener(PageContentListener listener);

  /**
   * Returns an XML representation of this page header.
   * 
   * @return an XML representation of this page header
   */
  String toXml();

}