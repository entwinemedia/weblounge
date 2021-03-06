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

package ch.entwine.weblounge.common.impl.request;

import ch.entwine.weblounge.common.request.CacheTag;

import java.io.Serializable;

/**
 * Tag used to identify entries in the caching service.
 */
public final class CacheTagImpl implements CacheTag, Serializable {

  /** The serial version uid */
  private static final long serialVersionUID = -1445545271162573855L;

  /** Tag key */
  protected String key = null;

  /** Tag value */
  protected String value = null;

  /**
   * Creates a cache tag with an empty value. This kind of tag can be used to
   * identify cache elements who must not have a tag with the given key defined.
   * 
   * @param key
   *          the tag key
   */
  public CacheTagImpl(String key) {
    this(key, ANY);
  }

  /**
   * Creates a cache tag with the given name and value.
   * <p>
   * Note that a tag value of <code>null</code> is replaced by the static final
   * field {@link #ANY}.
   * 
   * @param key
   *          the tag key
   * @param value
   *          the tag value
   */
  public CacheTagImpl(String key, String value) {
    if (key == null)
      throw new IllegalArgumentException("Tag key must not be null!");
    if (value == null)
      value = ANY;
    this.key = key;
    this.value = value;
  }

  /**
   * {@inheritDoc}
   * 
   * @see ch.entwine.weblounge.common.request.CacheTag#getName()
   */
  public String getName() {
    return key;
  }

  /**
   * {@inheritDoc}
   * 
   * @see ch.entwine.weblounge.common.request.CacheTag#getValue()
   */
  public String getValue() {
    return value;
  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof CacheTagImpl) {
      CacheTagImpl tag = (CacheTagImpl) obj;
      return key.equals(tag.key) && value.equals(tag.value);
    }
    return super.equals(obj);
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return key.hashCode();
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return key + " = " + value;
  }

  /**
   * Sets this tag to match any value with the given name, which makes it a
   * wildcard with respect to the tag value.
   */
  public void setMatchAny() {
    this.value = ANY;
  }

  /**
   * Returns <code>true</code> if this tag matches any other tag with the same
   * name, regardless of its value.
   * 
   * @return <code>true</code> if this tag matches all values
   */
  public boolean matchesAny() {
    return ANY.equals(this.value);
  }

}