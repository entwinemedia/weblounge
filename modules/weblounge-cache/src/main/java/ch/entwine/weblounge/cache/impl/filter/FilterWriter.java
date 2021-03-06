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

package ch.entwine.weblounge.cache.impl.filter;

import ch.entwine.weblounge.cache.StreamFilter;

import java.io.IOException;
import java.io.Writer;

/**
 * TODO Comment FilterWriter
 */
public class FilterWriter extends Writer {

  private Writer out;
  private StreamFilter filter;
  private String contentType;

  /**
   * Creates a new <code>FilterWriter</code>.
   * 
   * @param out
   */
  public FilterWriter(Writer out, StreamFilter filter, String contentType) {
    if (filter == null || out == null)
      throw new NullPointerException("out and filter cannot be null");
    this.out = out;
    this.filter = filter;
    this.contentType = contentType;
  }

  /**
   * @see java.io.Writer#close()
   */
  public void close() throws IOException {
    filter.close();
    out.close();
  }

  /**
   * @see java.io.Writer#flush()
   */
  public void flush() throws IOException {
    StringBuffer b = filter.flush();
    if (b != null)
      out.write(b.toString());
    out.flush();
  }

  /**
   * @see java.io.Writer#write(char[], int, int)
   */
  public void write(char[] cbuf, int off, int len) throws IOException {
    StringBuffer b = filter.filter(new StringBuffer().append(cbuf, off, len), contentType);
    if (b != null)
      out.write(b.toString());
  }

}