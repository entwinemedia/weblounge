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

package ch.o2it.weblounge.common.content;

/**
 * A tag consists of a key value pair which may be added to objects declaring
 * the {@link Taggable} interface.
 */
public interface Tag {

  /**
   * Returns the tag's name.
   * 
   * @return the tag name
   */
  String getName();

  /**
   * Returns the tag value. Although values may be arbitrary objects, a few
   * special values exist:
   * <ul>
   * <li><i>*</i> - Matches any value for the given key</li>
   * <li><i><code>Null</code></i> - Means that a tag with this key must not be
   * defined</li>
   * </ul>
   * 
   * @return the value
   */
  Object getValue();

}