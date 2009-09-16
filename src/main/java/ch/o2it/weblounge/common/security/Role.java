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

package ch.o2it.weblounge.common.security;

import ch.o2it.weblounge.common.language.Localizable;
import ch.o2it.weblounge.common.site.Site;

/**
 * A role models an entity that can be assigned a number of permissions.
 */
public interface Role extends Authority, Localizable {

  /** The system context */
  String CTXT_SYSTEM = "system";

  /**
   * Returns the role identifier.
   * 
   * @return the identifier
   */
  String getIdentifier();

  /**
   * Returns the role context.
   * 
   * @return the role context
   */
  String getContext();

  /**
   * Returns the site or <code>null</code> if this is a system role.
   * 
   * @return the associated site
   */
  Site getSite();

  /**
   * Makes this role an extension of <code>ancestor</code>.
   * 
   * @param ancestor
   *          the role to extend
   */
  void extend(Role ancestor);

  /**
   * Returns <code>true</code> if this role is a direct or indirec extension of
   * <code>ancestor</code>.
   * 
   * @param ancestor
   *          the extension in question
   * @return <code>true</code> if this role extends <code>ancestor</code>
   */
  boolean isExtensionOf(Role ancestor);

}