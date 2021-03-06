/*
 *  Weblounge: Web Content Management System
 *  Copyright (c) 2003 - 2011 The Weblounge Team
 *  http://entwinemedia.com/weblounge
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

package ch.entwine.weblounge.common.security;

import ch.entwine.weblounge.common.language.Localizable;

import java.security.Principal;

/**
 * A role models an entity that can be assigned a number of actions.
 */
public interface Role extends Principal, Authority, Localizable {

  /** Guest */
  String GUEST = "guest";

  /** Editor */
  String EDITOR = "editor";

  /** Publisher */
  String PUBLISHER = "publisher";

  /** Site administrator */
  String SITEADMIN = "siteadmin";

  /** Remove role */
  String SYSTEMADMIN = "systemadmin";

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
   * Makes this role an extension of <code>ancestor</code>.
   * 
   * @param ancestor
   *          the role to extend
   */
  void extend(Role ancestor);

  /**
   * Returns <code>true</code> if this role is a direct or indirect extension of
   * <code>ancestor</code>.
   * 
   * @param ancestor
   *          the extension in question
   * @return <code>true</code> if this role extends <code>ancestor</code>
   */
  boolean isExtensionOf(Role ancestor);

  /**
   * Returns all roles that are directly being extended by this roles.
   * 
   * @return the extended roles
   */
  Role[] getExtendedRoles();

  /**
   * Returns all roles that are included by this role, including this role.
   * <p>
   * If this role does not extend any other role, then the closure is equal to
   * this role only. Otherwise, the closure consists of all of the extended
   * roles plus this one.
   * 
   * @return the closure of this role
   */
  Role[] getClosure();

}