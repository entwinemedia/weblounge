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

package ch.o2it.weblounge.test;

import ch.o2it.weblounge.common.impl.request.RequestUtils;
import ch.o2it.weblounge.common.impl.site.ActionSupport;
import ch.o2it.weblounge.common.request.RequestFlavor;
import ch.o2it.weblounge.common.request.WebloungeRequest;
import ch.o2it.weblounge.common.request.WebloungeResponse;
import ch.o2it.weblounge.common.site.ActionException;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Simple test action that is able to render a greeting on the site template.
 */
public class GreeterAction extends ActionSupport {

  /** Name of the language parameter */
  public static final String LANGUAGE_PARAM = "language";

  /** Name of the properties file that defines the greetings */
  public static final String GREETING_PROPS = "greetings.properties";

  /** The greetings */
  protected Map<String, String> greetings = new HashMap<String, String>();

  /**
   * {@inheritDoc}
   * 
   * @see ch.o2it.weblounge.common.impl.site.ActionSupport#configure(ch.o2it.weblounge.common.request.WebloungeRequest,
   *      ch.o2it.weblounge.common.request.WebloungeResponse,
   *      ch.o2it.weblounge.common.request.RequestFlavor)
   */
  @Override
  public void configure(WebloungeRequest request, WebloungeResponse response,
      RequestFlavor flavor) throws ActionException {
    super.configure(request, response, flavor);
    try {
      Properties props = new Properties();
      props.load(GreeterAction.class.getResourceAsStream(GREETING_PROPS));
      String language = RequestUtils.getRequiredParameter(request, LANGUAGE_PARAM);
      String greeting = props.getProperty(language);
      if (greeting == null)
        // TODO: How do we indicate a 404 instead of 500? Different exceptions?
        // Like this, a json action could not return an empty resultset
        throw new ActionException("Unfortunately, we are not fluent in " + language);
      greetings.put(language, greeting);
    } catch (IllegalStateException e) {
      throw new ActionException("Language parameter was not specified");
    } catch (IOException e) {
      throw new ActionException("Error reading greetings from properties " + GREETING_PROPS);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.site.Action#startXMLResponse(ch.o2it.weblounge.common.request.WebloungeRequest, ch.o2it.weblounge.common.request.WebloungeResponse)
   */
  @Override
  public void startXMLResponse(WebloungeRequest request, WebloungeResponse response)
      throws ActionException {
    try {
      DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = docBuilder.newDocument();
      Element root = doc.createElement("greetings");
      doc.appendChild(root);
      for (Map.Entry<String, String> greeting : greetings.entrySet()) {
        Element greetingNode = doc.createElement("greeting");
        greetingNode.setAttribute("language", greeting.getKey());
        greetingNode.setNodeValue(greeting.getValue());
        root.appendChild(greetingNode);
      }
      TransformerFactory factory = TransformerFactory.newInstance();
      Transformer transformer = factory.newTransformer();
      transformer.transform(new DOMSource(doc), new StreamResult(response.getOutputStream()));
    } catch (Exception e) {
      throw new ActionException("Unable to create and send xml response", e);
    }
  }

  /**
   * {@inheritDoc}
   *
   * @see ch.o2it.weblounge.common.site.Action#startJSONResponse(ch.o2it.weblounge.common.request.WebloungeRequest, ch.o2it.weblounge.common.request.WebloungeResponse)
   */
  @Override
  public void startJSONResponse(WebloungeRequest request, WebloungeResponse response)
      throws ActionException {
    try {
      JSONObject json = new JSONObject(greetings);
      response.getOutputStream().print(json.toString());
    } catch (IOException e) {
      throw new ActionException("Unable to send json response", e);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @see ch.o2it.weblounge.common.impl.site.ActionSupport#passivate()
   */
  @Override
  public void passivate() {
    super.passivate();
    log_.info("Preparing greeter for idle state");
    greetings.clear();
  }

}
