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

package ch.entwine.weblounge.common.impl.content;

import ch.entwine.weblounge.common.Times;
import ch.entwine.weblounge.common.impl.security.AbstractSecurityContext;
import ch.entwine.weblounge.common.impl.security.ActionImpl;
import ch.entwine.weblounge.common.impl.security.AuthorityImpl;
import ch.entwine.weblounge.common.impl.security.UserImpl;
import ch.entwine.weblounge.common.impl.util.xml.WebloungeSAXHandler;
import ch.entwine.weblounge.common.security.Action;
import ch.entwine.weblounge.common.security.Authority;
import ch.entwine.weblounge.common.security.Securable;
import ch.entwine.weblounge.common.security.User;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Utility class used to parse generic content including language sensitive
 * information, creation, modification and publication data as well as security
 * settings.
 * <p>
 * The implementation is intended to be used by the <code>PageReader</code> and
 * <code>PageletReader</code>.
 * 
 * @see ch.entwine.weblounge.common.impl.content.page.PageReader
 * @see ch.entwine.weblounge.common.impl.content.page.PageletReader
 */
public abstract class WebloungeContentReader extends WebloungeSAXHandler {

  /** Parser context */
  protected enum Context {
    Unknown, Security, Creation, Modification, Publish, Content
  };

  /** The initial parser context */
  protected Context contentReaderContext = Context.Unknown;

  /**
   * Creates a new content reader that will parse the XML data.
   */
  public WebloungeContentReader() {
    clipboard = new HashMap<String, Object>();
    characters = new StringBuffer();
  }

  /**
   * This method is called if the creation date and user was found.
   * 
   * @param creator
   *          the creator
   * @param creationDate
   *          the creation date
   */
  protected abstract void setCreated(User creator, Date creationDate);

  /**
   * This method is called if the user was found that modified this element at a
   * given date.
   * 
   * @param modifier
   *          the modifying user
   * @param modificationDate
   *          the modification date
   */
  protected abstract void setModified(User modifier, Date modificationDate);

  /**
   * This method is called when the publishing data was read in.
   * 
   * @param publisher
   *          the user who published the resource
   * @param startDate
   *          the start date of publication
   * @param endDate
   *          the end date of publication
   */
  protected abstract void setPublished(User publisher, Date startDate,
      Date endDate);

  /**
   * This callback is used if an owner was detected inside a security section.
   * 
   * @param owner
   *          the owner
   */
  protected abstract void setOwner(User owner);

  /**
   * Defines the evaluation order for allow/deny rules.
   * 
   * @param order
   *          the evaluation order
   */
  protected abstract void setAllowDenyOrder(Securable.Order order);

  /**
   * This method is called if an allow rule was found.
   * 
   * @param action
   *          the action to grant
   * @param authority
   *          the authority that this action is granted to
   */
  protected abstract void allow(Action action, Authority authority);

  /**
   * This method is called if a deny rule was found.
   * 
   * @param action
   *          the action to deny
   * @param authority
   *          the authority that this action is denied
   */
  protected abstract void deny(Action action, Authority authority);

  /**
   * {@inheritDoc}
   * <p>
   * <b>Note:</b> This implementation is not suitable for mixed content, since
   * the characters buffer is cleared at the beginning of each tag.
   * 
   * @param uri
   *          information about the namespace
   * @param local
   *          the local name of the element
   * @param raw
   *          the raw name of the element
   * @param attrs
   *          the element's attributes
   */
  public void startElement(String uri, String local, String raw,
      Attributes attrs) throws SAXException {
    characters = new StringBuffer();

    // security context
    if ("security".equals(raw)) {
      contentReaderContext = Context.Security;
      return;
    }

    // access control list
    else if (contentReaderContext == Context.Security && "acl".equals(raw)) {
      clipboard.put("order", attrs.getValue("order"));
    }

    // allow rules
    else if (contentReaderContext == Context.Security && "allow".equals(raw)) {
      clipboard.put("id", attrs.getValue("id"));
      clipboard.put("type", attrs.getValue("type"));
    }

    // deny rules
    else if (contentReaderContext == Context.Security && "deny".equals(raw)) {
      clipboard.put("id", attrs.getValue("id"));
      clipboard.put("type", attrs.getValue("type"));
    }

    // creation context
    else if ("created".equals(raw)) {
      contentReaderContext = Context.Creation;
      return;
    }

    // modification context
    else if ("modified".equals(raw)) {
      contentReaderContext = Context.Modification;
      return;
    }

    // publishing context
    else if ("published".equals(raw)) {
      contentReaderContext = Context.Publish;
      return;
    }

    // user
    else if ("user".equals(raw)) {
      clipboard.put("user", attrs.getValue("id"));
      clipboard.put("realm", attrs.getValue("realm"));
    }

  }

  /**
   * {@inheritDoc}
   * 
   * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
   *      java.lang.String, java.lang.String)
   */
  public void endElement(String uri, String local, String raw)
      throws SAXException {

    // creator, modifier
    if ("user".equals(raw)) {
      String login = (String) clipboard.remove("user");
      String realm = (String) clipboard.remove("realm");
      String name = getCharacters();
      User user = new UserImpl(login, realm, name);
      clipboard.put("user", user);
    }

    // date
    else if ("date".equals(raw)) {
      try {
        Date d = dateFormat.parse(getCharacters());
        clipboard.put("date", d);
      } catch (ParseException e) {
        throw new IllegalStateException("Reading date failed: '" + getCharacters() + "'");
      }
    }

    // publishing start date
    else if (contentReaderContext == Context.Publish && "from".equals(raw)) {
      try {
        Date d = dateFormat.parse(getCharacters());
        clipboard.put("publish.start", d);
      } catch (ParseException e) {
        throw new IllegalStateException("Reading publishing start date failed: '" + getCharacters() + "'");
      }
    }

    // publishing end date
    else if (contentReaderContext == Context.Publish && "to".equals(raw)) {
      try {
        Date d = dateFormat.parse(getCharacters());
        if (d.getTime() < Times.MAX_DATE)
          clipboard.put("publish.end", d);
      } catch (ParseException e) {
        throw new IllegalStateException("Reading publishing end date failed: '" + getCharacters() + "'");
      }
    }

    // created
    else if (contentReaderContext == Context.Creation && "created".equals(raw)) {
      User owner = (User) clipboard.remove("user");
      Date date = (Date) clipboard.remove("date");
      if (date == null)
        throw new IllegalStateException("Creation date not found");
      setCreated(owner, date);
      contentReaderContext = Context.Unknown;
    }

    // modified
    else if (contentReaderContext == Context.Modification && "modified".equals(raw)) {
      User modifier = (User) clipboard.remove("user");
      Date date = (Date) clipboard.remove("date");
      if (date == null)
        throw new IllegalStateException("Modification date not found");
      setModified(modifier, date);
      contentReaderContext = Context.Unknown;
    }

    // published
    else if (contentReaderContext == Context.Publish && "published".equals(raw)) {
      User publisher = (User) clipboard.remove("user");
      Date startDate = (Date) clipboard.remove("publish.start");
      if (startDate == null)
        throw new IllegalStateException("Publication start date not found");
      Date endDate = (Date) clipboard.remove("publish.end");
      setPublished(publisher, startDate, endDate);
      contentReaderContext = Context.Unknown;
    }

    // owner
    else if (contentReaderContext == Context.Security && "owner".equals(raw)) {
      User owner = (User) clipboard.remove("user");
      if (owner == null)
        throw new IllegalStateException("Owner not found");
      setOwner(owner);
    }

    // access control list
    else if (contentReaderContext == Context.Security && "acl".equals(raw)) {
      String order = (String) clipboard.remove("order");
      if ("deny,allow".equalsIgnoreCase(order))
        setAllowDenyOrder(Securable.Order.DenyAllow);
      else
        setAllowDenyOrder(Securable.Order.AllowDeny);
    }
    
    // allow rule
    else if (contentReaderContext == Context.Security && "allow".equals(raw)) {
      String id = (String) clipboard.remove("id");
      Action action = new ActionImpl(id);
      String type = (String) clipboard.remove("type");
      if (type != null) {
        type = AbstractSecurityContext.resolveAuthorityTypeShortcut(type);
        StringTokenizer tok = new StringTokenizer(getCharacters(), " ,;");
        while (tok.hasMoreTokens()) {
          String authorityId = tok.nextToken();
          Authority authority = new AuthorityImpl(type, authorityId);
          allow(action, authority);
        }
      }
    }

    // deny rule
    else if (contentReaderContext == Context.Security && "deny".equals(raw)) {
      String id = (String) clipboard.remove("id");
      Action action = new ActionImpl(id);
      String type = (String) clipboard.remove("type");
      if (type != null) {
        type = AbstractSecurityContext.resolveAuthorityTypeShortcut(type);
        StringTokenizer tok = new StringTokenizer(getCharacters(), " ,;");
        while (tok.hasMoreTokens()) {
          String authorityId = tok.nextToken();
          Authority authority = new AuthorityImpl(type, authorityId);
          deny(action, authority);
        }
      }
    }

  }
}