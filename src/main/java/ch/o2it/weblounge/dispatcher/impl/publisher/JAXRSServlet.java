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

package ch.o2it.weblounge.dispatcher.impl.publisher;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * This custom implementation of the <code>CXFNonSpringJaxrsServlet</code> was
 * done following the <a href=
 * "http://cxf.apache.org/docs/jax-rs.html#JAX-RS-WithCXFNonSpringJaxrsServlet"
 * >cxf documentation</a>.
 */
public class JAXRSServlet extends CXFNonSpringJaxrsServlet {

  /** The serial version uid */
  private static final long serialVersionUID = 7336130764437993613L;

  private String address = null;

  private Object service = null;

  /**
   * Creates a new servlet that maps <code>service</code> to the given address.
   * 
   * @param address
   *          the servlet address
   * @param service
   *          the service implementation
   */
  JAXRSServlet(String address, Object service) {
    if (address == null)
      throw new IllegalArgumentException("Address can't be null");
    if (service == null)
      throw new IllegalArgumentException("Service impleemntation can't be null");
    this.address = address;
    this.service = service;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet#loadBus(javax.servlet.ServletConfig)
   */
  @Override
  public void loadBus(ServletConfig servletConfig) throws ServletException {
    super.loadBus(servletConfig);

    ClassLoader bundleClassLoader = Thread.currentThread().getContextClassLoader();
    ClassLoader delegateClassLoader = JAXRSServerFactoryBean.class.getClassLoader();

    try {

      // The JAXRSServerFactoryBean needs access to resources on it's own
      // bundle classpath (META-INF/cxf.xml, META-INF/cxf/osgi.xml). Therefore
      // we need to adjust the context class loader
      Thread.currentThread().setContextClassLoader(delegateClassLoader);

      // Create the endpoint
      JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
      sf.setAddress(address);
      sf.setResourceClasses(service.getClass());
      sf.setResourceProvider(service.getClass(), new SingletonResourceProvider(service));
      sf.create();

    } finally {
      Thread.currentThread().setContextClassLoader(bundleClassLoader);
    }

  }

  /**
   * {@inheritDoc}
   * 
   * Note that this method is intentionally <i>not</i> calling its super
   * implementation, since this will throw an exception because no service
   * classes have been registered previously.
   * 
   * @see org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet#getServiceClasses(javax.servlet.ServletConfig,
   *      boolean)
   */
  @SuppressWarnings("rawtypes")
  @Override
  protected List<Class> getServiceClasses(ServletConfig servletConfig,
      boolean modelAvailable) throws ServletException {
    List<Class> classes = new ArrayList<Class>();
    classes.add(service.getClass());
    return classes;
  }

  /**
   * {@inheritDoc}
   * 
   * @see org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet#getResourceProviders(javax.servlet.ServletConfig,
   *      java.util.List)
   */
  @SuppressWarnings("rawtypes")
  @Override
  protected Map<Class, ResourceProvider> getResourceProviders(
      ServletConfig servletConfig, List<Class> resourceClasses)
      throws ServletException {
    Map<Class, ResourceProvider> providers = super.getResourceProviders(servletConfig, resourceClasses);
    providers.put(service.getClass(), new SingletonResourceProvider(service));
    return providers;
  }

}
