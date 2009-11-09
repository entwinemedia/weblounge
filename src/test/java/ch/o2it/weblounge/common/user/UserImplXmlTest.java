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

package ch.o2it.weblounge.common.user;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.fail;

import ch.o2it.weblounge.common.impl.user.UserImpl;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Tests loading and serializing of {@link UserImpl} objects from and to
 * <code>XML</code>.
 */
public class UserImplXmlTest extends UserImplTest {

  /** File path and name */
  String testFile = "/user.xml";

  /**
   * @throws java.lang.Exception
   */
  @Before
  @Override
  public void setUp() throws Exception {
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    URL testContext = this.getClass().getResource(testFile);
    Document doc = docBuilder.parse(testContext.openStream());
    user = UserImpl.fromXml(doc.getFirstChild());
  }

  /**
   * Test method for {@link ch.o2it.weblounge.common.impl.user.UserImpl#toXml()}
   * .
   */
  @Test
  public void testToXml() {
    File templateFile = new File(this.getClass().getResource(testFile).getPath());
    String template = null;
    try {
      byte[] buffer = new byte[(int) templateFile.length()];
      FileInputStream f = new FileInputStream(templateFile);
      f.read(buffer);
      template = new String(buffer).replaceAll("(>\\s*)+", ">").replaceAll("(\\s*<)+", "<");
    } catch (IOException e) {
      fail("Error reading test resource " + templateFile.getPath());
    }
    assertEquals(template, user.toXml());
  }

}
