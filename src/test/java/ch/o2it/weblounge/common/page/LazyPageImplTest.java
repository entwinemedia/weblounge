/*
 *  Weblounge: Web Content Management System
 *  Copyright (c) 2010 The Weblounge Team
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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import ch.o2it.weblounge.common.TestUtils;
import ch.o2it.weblounge.common.impl.page.LazyPageImpl;

import org.junit.Before;

/**
 * Test case for {@link LazyPageImpl}.
 */
public class LazyPageImplTest extends PageImplTest {

  /** Name of the page test file */
  protected String pageTestFile = "/page.xml";

  /** Name of the page header test file */
  protected String headerTestFile = "/pageheader.xml";

  /** Name of the page preview test file */
  protected String previewTestFile = "/pagepreview.xml";

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception {
    setupPrerequisites();
    String pageXml = TestUtils.loadXmlFromResource(pageTestFile);
    String headerXml = TestUtils.loadXmlFromResource(headerTestFile);
    String previewXml = TestUtils.loadXmlFromResource(previewTestFile);
    page = new LazyPageImpl(pageURI, pageXml, headerXml, previewXml);
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testAddPageletPageletString()
   */
  @Override
  public void testAddPageletPageletString() {
    super.testAddPageletPageletString();
    assertTrue(((LazyPageImpl)page).isBodyLoaded());
    assertFalse(((LazyPageImpl)page).isHeaderLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testAddPageletPageletStringInt()
   */
  @Override
  public void testAddPageletPageletStringInt() {
    super.testAddPageletPageletStringInt();
    assertTrue(((LazyPageImpl)page).isBodyLoaded());
    assertFalse(((LazyPageImpl)page).isHeaderLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testAllow()
   */
  @Override
  public void testAllow() {
    super.testAllow();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testCheckAll()
   */
  @Override
  public void testCheckAll() {
    super.testCheckAll();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testCheckOne()
   */
  @Override
  public void testCheckOne() {
    super.testCheckOne();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testCheckPermissionAuthority()
   */
  @Override
  public void testCheckPermissionAuthority() {
    super.testCheckPermissionAuthority();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testCheckPermissionSetAuthority()
   */
  @Override
  public void testCheckPermissionSetAuthority() {
    super.testCheckPermissionSetAuthority();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testCompareTo()
   */
  @Override
  public void testCompareTo() {
    super.testCompareTo();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testDeny()
   */
  @Override
  public void testDeny() {
    super.testDeny();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testEqualsObject()
   */
  @Override
  public void testEqualsObject() {
    super.testEqualsObject();
    assertFalse(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetCoverage()
   */
  @Override
  public void testGetCoverage() {
    super.testGetCoverage();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetCoverageLanguage()
   */
  @Override
  public void testGetCoverageLanguage() {
    super.testGetCoverageLanguage();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetCoverageLanguageBoolean()
   */
  @Override
  public void testGetCoverageLanguageBoolean() {
    super.testGetCoverageLanguageBoolean();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetCreationDate()
   */
  @Override
  public void testGetCreationDate() {
    super.testGetCreationDate();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetCreator()
   */
  @Override
  public void testGetCreator() {
    super.testGetCreator();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetDescription()
   */
  @Override
  public void testGetDescription() {
    super.testGetDescription();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetDescriptionLanguage()
   */
  @Override
  public void testGetDescriptionLanguage() {
    super.testGetDescriptionLanguage();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetDescriptionLanguageBoolean()
   */
  @Override
  public void testGetDescriptionLanguageBoolean() {
    super.testGetDescriptionLanguageBoolean();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetLayout()
   */
  @Override
  public void testGetLayout() {
    super.testGetLayout();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetLockOwner()
   */
  @Override
  public void testGetLockOwner() {
    super.testGetLockOwner();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetModificationDate()
   */
  @Override
  public void testGetModificationDate() {
    super.testGetModificationDate();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetModifier()
   */
  @Override
  public void testGetModifier() {
    super.testGetModifier();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetOwner()
   */
  @Override
  public void testGetOwner() {
    super.testGetOwner();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetPageletsString()
   */
  @Override
  public void testGetPageletsString() {
    super.testGetPageletsString();
    assertTrue(((LazyPageImpl)page).isBodyLoaded());
    assertFalse(((LazyPageImpl)page).isHeaderLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetPageletsStringStringString()
   */
  @Override
  public void testGetPageletsStringStringString() {
    super.testGetPageletsStringStringString();
    assertTrue(((LazyPageImpl)page).isBodyLoaded());
    assertFalse(((LazyPageImpl)page).isHeaderLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetPublisher()
   */
  @Override
  public void testGetPublisher() {
    super.testGetPublisher();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetPublishFrom()
   */
  @Override
  public void testGetPublishFrom() {
    super.testGetPublishFrom();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetPublishTo()
   */
  @Override
  public void testGetPublishTo() {
    super.testGetPublishTo();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetRights()
   */
  @Override
  public void testGetRights() {
    super.testGetRights();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetRightsLanguage()
   */
  @Override
  public void testGetRightsLanguage() {
    super.testGetRightsLanguage();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetRightsLanguageBoolean()
   */
  @Override
  public void testGetRightsLanguageBoolean() {
    super.testGetRightsLanguageBoolean();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetSite()
   */
  @Override
  public void testGetSite() {
    super.testGetSite();
    assertFalse(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetSubjects()
   */
  @Override
  public void testGetSubjects() {
    super.testGetSubjects();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetTemplate()
   */
  @Override
  public void testGetTemplate() {
    super.testGetTemplate();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetTitle()
   */
  @Override
  public void testGetTitle() {
    super.testGetTitle();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetTitleLanguage()
   */
  @Override
  public void testGetTitleLanguage() {
    super.testGetTitleLanguage();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetTitleLanguageBoolean()
   */
  @Override
  public void testGetTitleLanguageBoolean() {
    super.testGetTitleLanguageBoolean();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetType()
   */
  @Override
  public void testGetType() {
    super.testGetType();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetURI()
   */
  @Override
  public void testGetURI() {
    super.testGetURI();
    assertFalse(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testGetVersion()
   */
  @Override
  public void testGetVersion() {
    super.testGetVersion();
    assertFalse(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testHashCode()
   */
  @Override
  public void testHashCode() {
    super.testHashCode();
    assertFalse(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testHasSubject()
   */
  @Override
  public void testHasSubject() {
    super.testHasSubject();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testIsCreatedAfter()
   */
  @Override
  public void testIsCreatedAfter() {
    super.testIsCreatedAfter();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testIsIndexed()
   */
  @Override
  public void testIsIndexed() {
    super.testIsIndexed();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testIsLocked()
   */
  @Override
  public void testIsLocked() {
    super.testIsLocked();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testIsPromoted()
   */
  @Override
  public void testIsPromoted() {
    super.testIsPromoted();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testIsPublished()
   */
  @Override
  public void testIsPublished() {
    super.testIsPublished();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testIsPublishedDate()
   */
  @Override
  public void testIsPublishedDate() {
    super.testIsPublishedDate();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testPermissions()
   */
  @Override
  public void testPermissions() {
    super.testPermissions();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testRemovePagelet()
   */
  @Override
  public void testRemovePagelet() {
    super.testRemovePagelet();
    assertTrue(((LazyPageImpl)page).isBodyLoaded());
    assertFalse(((LazyPageImpl)page).isHeaderLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testRemoveSubject()
   */
  @Override
  public void testRemoveSubject() {
    super.testRemoveSubject();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.page.PageImplTest#testSetUnlocked()
   */
  @Override
  public void testSetUnlocked() {
    super.testSetUnlocked();
    assertTrue(((LazyPageImpl)page).isHeaderLoaded());
    assertFalse(((LazyPageImpl)page).isBodyLoaded());
  }
  
}
