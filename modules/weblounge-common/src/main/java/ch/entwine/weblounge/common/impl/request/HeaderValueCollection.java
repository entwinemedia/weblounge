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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Internal helper class that serves as value holder for request headers.
 */
public class HeaderValueCollection {

  /** The list of header values */
  private final List<String> values = new LinkedList<>();

  /**
   * Sets the header values to <code>value</code>. Any existing values are
   * removed beforehand.
   * 
   * @param value
   *          the new header value
   */
  public void setValue(String value) {
    this.values.clear();
    this.values.add(value);
  }

  /**
   * Adds <code>value</code> to the header values.
   * 
   * @param value
   *          the value to add
   */
  public void addValue(String value) {
    this.values.add(value);
  }

  /**
   * Adds all values to the current list of header values.
   * 
   * @param values
   *          the values to add
   */
  public void addValues(Collection<String> values) {
    this.values.addAll(values);
  }

  /**
   * Adds an object array
   * 
   * @param values
   */
  public void addValueArray(Object values) {
    if (values instanceof String[]) {
      this.values.addAll(Arrays.asList((String[]) values));
    }
  }

  /**
   * Returns a list of the header values.
   * 
   * @return the header values
   */
  public List<String> getValues() {
    return Collections.unmodifiableList(this.values);
  }

  /**
   * Returns the header value. If there is more than one value, the first one is
   * returned.
   * 
   * @return the header value
   */
  public String getValue() {
    return (!this.values.isEmpty() ? this.values.get(0) : null);
  }

  /**
   * Find a HeaderValueHolder by name, ignoring casing.
   * 
   * @param headers
   *          the Map of header names to HeaderValueHolders
   * @param name
   *          the name of the desired header
   * @return the corresponding HeaderValueHolder, or <code>null</code> if none
   *         found
   */
  public static HeaderValueCollection getByName(
      Map<String, HeaderValueCollection> headers, String name) {
    if (name == null)
      throw new IllegalArgumentException("Header name must not be null");
    for (Iterator<String> it = headers.keySet().iterator(); it.hasNext();) {
      String headerName = it.next();
      if (headerName.equalsIgnoreCase(name)) {
        return headers.get(headerName);
      }
    }
    return null;
  }

}
