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

package ch.o2it.weblounge.dispatcher.impl.handler;

import ch.o2it.weblounge.common.ConfigurationException;
import ch.o2it.weblounge.common.Times;
import ch.o2it.weblounge.common.impl.request.Http11ProtocolHandler;
import ch.o2it.weblounge.common.impl.request.Http11ResponseType;
import ch.o2it.weblounge.common.impl.request.Http11Utils;
import ch.o2it.weblounge.common.impl.util.Env;
import ch.o2it.weblounge.common.request.RequestHandler;
import ch.o2it.weblounge.common.request.RequestHandlerConfiguration;
import ch.o2it.weblounge.common.request.WebloungeRequest;
import ch.o2it.weblounge.common.request.WebloungeResponse;
import ch.o2it.weblounge.dispatcher.impl.request.WebloungeDispatcher;

import org.apache.jasper.JasperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO: Comment StaticContentHandler
 */
public class StaticContentHandler implements RequestHandler, Times {

	/** the path rules */
	private static final PathRule url_rules[] = {
		new PathRule("^/(?:sites/[^/]+|shared)/module/[^/]+/[^/]+/", 
			new String[] { "^/(?:sites/[^/]+|shared)/module/[^/]+/(?:lib|classes|conf|doc)/" }),
		new PathRule("^/(?:sites/[^/]+|shared)/[^/]+/",
			new String[] { "^/(?:sites/[^/]+|shared)/(?:lib|classes|module)/" })
	};
	
	/** the logger */
	private static final Logger log = LoggerFactory.getLogger(StaticContentHandler.class);
	
	/**
	 * Service the static content located at url (relative to the webapp root).
	 *  
	 * @param req the http servlet request
	 * @param resp the http servlet response
	 * @param url the url to include
	 */
	public static void service(ServletRequest req, ServletResponse resp, String url) {
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		try { 
			request = (HttpServletRequest) req;
			response = (HttpServletResponse) resp;
		} catch (ClassCastException e) {
			return;
		}
		
		// check the request method
		String method = request.getMethod();
		if (!Http11Utils.checkDefaultMethods(method, response))
			return;
		
		try {
			log.debug("static request for: " + url);
			// check the url
			if (url == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			// check the resource
			String path = Env.getRealPath(url);
			File file = new File(path);
			if (!file.exists() || !file.canRead()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			if (!file.isFile()) {
				// we do not provide directory listings!
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
			log.debug("serving file: " + path);
			/* analyze the HTTP request */
			Http11ResponseType responseType =
				Http11ProtocolHandler.analyzeRequest(
					request,
					file.lastModified(),
					MS_PER_DAY + System.currentTimeMillis(),
					file.length());

			/* generate the headers */
			Http11ProtocolHandler.generateHeaders(response, responseType);

			/* define the content type */
			if (!Http11ProtocolHandler.isError(responseType))
				response.setContentType(Env.getMimeType(file.getAbsolutePath()));

			/* write the response */
			InputStream is = new FileInputStream(file);
			try {
				Http11ProtocolHandler.generateResponse(response, responseType, is);
			} finally {
				is.close();
			}
		} catch (IOException e) {
			log.error("Exception while handling static request for '" + url);
		}
	}
	
	/**
	 * @see ch.o2it.weblounge.api.request.RequestHandler#service(ch.o2it.weblounge.api.request.WebloungeRequest, ch.o2it.weblounge.api.request.WebloungeResponse)
	 */
	public boolean service(WebloungeRequest request, WebloungeResponse response) {
		String pathInfo = request.getPathInfo();
		if (pathInfo == null)
			return false;

		for (int i = 0; i < url_rules.length; i++) {
			PathRule rule = url_rules[i];
			if (rule.includes(pathInfo)) {
				if (rule.excludes(pathInfo))
					return false;
				try {
					log.debug("Handling request " + request.getUrl());
					WebloungeDispatcher.forward(request, response, pathInfo);
				} catch (Exception e) {
					if (e instanceof JasperException && ((JasperException)e).getRootCause() != null) {
						String msg = "Error while dispatching static request to " + pathInfo + ": ";
						msg += e.getMessage();
						request.getSite().getLogger().error(msg, ((JasperException)e).getRootCause());
					} else {
						String msg = "Error while dispatching static request " + pathInfo + ": ";
						request.getSite().getLogger().error(msg, e);
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * @see ch.o2it.weblounge.api.request.RequestHandler#configure(ch.o2it.weblounge.api.request.RequestHandlerConfiguration)
	 */
	public void configure(RequestHandlerConfiguration config)
		throws ConfigurationException {
	}

	/**
	 * @see ch.o2it.weblounge.api.request.RequestHandler#getIdentifier()
	 */
	public String getIdentifier() {
		return "static";
	}

	/**
	 * @see ch.o2it.weblounge.api.request.RequestHandler#getName()
	 */
	public String getName() {
		return "static content handler";
	}

	/**
	 * @see ch.o2it.weblounge.api.request.RequestHandler#getDescription()
	 */
	public String getDescription() {
		return "handles all content that isn't handled by any other handler";
	}

	/**
	 * TODO Comment PathRule
	 */
	private static class PathRule {

		/** the include pattern */
		private Pattern include;
		
		/** the exclude patten */
		private Pattern[] excludes;
		
		/**
		 * Creates a new <code>PathRule</code>.
		 * 
		 * @param include the include pattern
		 * @param excludes the exclude pattern
		 */
		public PathRule(String include, String excludes[]) {
			this.include = Pattern.compile(include);
			this.excludes = new Pattern[excludes.length];
			for (int i = 0; i < excludes.length; i++) {
				this.excludes[i] = Pattern.compile(excludes[i]);
			}
		}
		
		/**
		 * Checks whether this <code>PathRule</code> includes the given path. 
		 *
		 * @param path the path to check
		 * @return <code>true</code> if the path is included
		 */
		public boolean includes(String path) {
			return include.matcher(path).lookingAt();
		}
		
		/**
		 * Checks whether this <code>PathRule</code> excludes the given path. 
		 *
		 * @param path the path to check
		 * @return <code>true</code> if the path is excluded
		 */
		public boolean excludes(String path) {
			for (int i = 0; i < excludes.length; i++)
				if (excludes[i].matcher(path).lookingAt())
					return true;
			return false;
		}

	}

}