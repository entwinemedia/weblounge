/*
 * Weblounge: Web Content Management System Copyright (c) 2009 The Weblounge
 * Team http://weblounge.o2it.ch
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package ch.o2it.weblounge.common.impl.site;

import ch.o2it.weblounge.common.ConfigurationException;
import ch.o2it.weblounge.common.impl.language.LanguageSupport;
import ch.o2it.weblounge.common.impl.language.LocalizableContent;
import ch.o2it.weblounge.common.impl.util.Arguments;
import ch.o2it.weblounge.common.impl.util.ServletConfiguration;
import ch.o2it.weblounge.common.impl.util.ServletMapping;
import ch.o2it.weblounge.common.impl.util.classloader.SiteClassLoader;
import ch.o2it.weblounge.common.impl.util.classloader.WebloungeClassLoader;
import ch.o2it.weblounge.common.impl.util.config.XMLConfigurator;
import ch.o2it.weblounge.common.impl.util.xml.XMLUtilities;
import ch.o2it.weblounge.common.impl.util.xml.XPathHelper;
import ch.o2it.weblounge.common.language.Language;
import ch.o2it.weblounge.common.renderer.Renderer;
import ch.o2it.weblounge.common.service.Service;
import ch.o2it.weblounge.common.site.Action;
import ch.o2it.weblounge.common.site.ImageStyle;
import ch.o2it.weblounge.common.site.Job;
import ch.o2it.weblounge.common.site.ModuleConfiguration;
import ch.o2it.weblounge.common.site.Site;
import ch.o2it.weblounge.common.site.WizardHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.xpath.XPath;

/**
 * <code>ModuleConfiguration</code> represents the contents of the
 * <code>&lt;module&gt;</code> node from a module configuration file
 * <code>module.xml</code>.
 * <p>
 * A module configuration looks like this:
 * <p>
 * 
 * <pre>
 * 	&lt;module id=&quot;edit&quot;&gt;
 * 		&lt;enable&gt;true&lt;/enable&gt;
 * 		&lt;description&gt;Main site&lt;/description&gt;
 * 		.
 * 		.
 * 		.
 * 	&lt;/module&gt;
 * </pre>
 * 
 * @author Tobias Wunden
 * @version 1.0
 * @since Weblounge 2.0
 */

public final class ModuleConfigurationImpl extends XMLConfigurator implements ModuleConfiguration {

  /** Module identifier */
  String identifier;

  /** Module description */
  LocalizableContent descriptions;

  /** True if the site is enabled */
  boolean isEnabled;

  /** The associated site */
  Site site;

  /** The renderers */
  Map<String, Renderer> renderers;

  /** The action handlers */
  Map<String, Action> actions;

  /** The services */
  Map<String, Service> services;

  /** Module jobs */
  Map<String, Job> jobs;

  /** The image styles */
  Map<String, ImageStyle> imagestyles;

  /** The module load factor */
  int loadfactor;

  /** The module class implementation to load */
  String moduleClass;

  /** The base module */
  String baseModule;

  /** The module class loader */
  ClassLoader classLoader = null;

  // Logging

  /** Logging facility */
  private final static Logger log_ = LoggerFactory.getLogger(ModuleConfigurationImpl.class.getName());

  /**
   * Creates a new module configuration.
   * 
   * @param config
   *          the configuration file
   * @param site
   *          the associated site
   */
  public ModuleConfigurationImpl(File config, Site site) {
    super(config, "Module configuration");
    this.site = site;
    actions = new HashMap<String, Action>();
    imagestyles = new HashMap<String, ImageStyle>();
    renderers = new HashMap<String, Renderer>();
    services = new HashMap<String, Service>();
    jobs = new HashMap<String, Job>();
    classLoader = Thread.currentThread().getContextClassLoader();
  }

  /**
   * Returns the module's document base.
   * 
   * @return the module's document base
   */
  public String getPath() {
    return super.getFile().getParentFile().getPath();
  }

  /**
   * Reads the module configuration from the given xml configuration node.
   * 
   * @param config
   *          the configuration node
   * @throws ConfigurationException
   *           if there are errors in the configuration
   */
  public void configure(Document config) throws ConfigurationException {
    Arguments.checkNull(config, "config");
    try {
      XPath path = XMLUtilities.getXPath();
      readMainSettings(path, XPathHelper.select(path, config, "/module"));
      readOptions(path, XPathHelper.select(path, config, "/module/options"));
      readPerformanceSettings(path, XPathHelper.select(path, config, "/module/performance"));
      readRenderers(path, XPathHelper.select(path, config, "/module/renderers"));
      readActions(path, XPathHelper.select(path, config, "/module/actions"));
      readImagestyles(path, XPathHelper.select(path, config, "/module/imagestyles"));
      readServices(path, XPathHelper.select(path, config, "/module/services"));
      readJobs(path, XPathHelper.select(path, config, "/module/jobs"));
      super.init(path, XPathHelper.select(path, config, "/module"));
    } catch (ConfigurationException e) {
      throw e;
    } catch (Exception e) {
      log_.error("Error when reading module configuration '" + super.getFile() + "':" + e.getMessage(), e);
      throw new ConfigurationException("Error when reading module configuration!", e);
    }
  }

  /**
   * Returns the module identifier, e. g. <tt>forum</tt>.
   * 
   * @return the module identifier
   */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * Returns the module description, e. g. <tt>Forum</tt>.
   * 
   * @param l
   *          the language used to return the description
   * @return the module description
   */
  public String getDescription(Language l) {
    return descriptions.toString(l);
  }

  /**
   * Returns <code>true</code> if <code>o</code> is a <code>ModuleConfiguration
	 * </code> and maches this
   * configuration in every aspect.
   * 
   * @return <code>true</code> if o is equal to this configuration
   */
  public boolean equals(Object o) {
    if (o instanceof ModuleConfiguration) {
      ModuleConfigurationImpl s = (ModuleConfigurationImpl) o;
      return (identifier.equals(s.identifier) && descriptions.equals(s.descriptions) && isEnabled == s.isEnabled);
    }
    return false;
  }

  /**
   * Returns <code>true</code> if the module is configured to be enabled.
   * 
   * @return <code>true</code> if the module is enabled
   */
  public boolean isEnabled() {
    return isEnabled;
  }

  /**
   * Returns the module load factor. The factor denotes the load that is
   * excpected to be put on the module.
   * 
   * @return the module load factor
   */
  public int getLoadFactor() {
    return loadfactor;
  }

  /**
   * Returns the renderers that are defined for the module.
   * 
   * @return the module renderer
   */
  public Map<String, Renderer> getRenderers() {
    return renderers;
  }

  /**
   * Returns the action registry containing all registered actions.
   * 
   * @return the actions
   */
  public Map<String, Action> getActions() {
    return actions;
  }

  /**
   * Returns the image styles containing all registered styles.
   * 
   * @return the image styles
   */
  public Map<String, ImageStyle> getImageStyles() {
    return imagestyles;
  }

  /**
   * Returns the module services.
   * 
   * @return the module services
   */
  public Map<String, Service> getServices() {
    return services;
  }

  /**
   * Returns the module jobs.
   * 
   * @return the module jobs
   */
  public Map<String, Job> getJobs() {
    return jobs;
  }

  /**
   * Returns the module identifier.
   * 
   * @return the module identifier
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return identifier;
  }

  /**
   * Reads the main module settings like identifier, name and description from
   * the module configuration.
   * 
   * @param config
   *          module configuration node
   */
  private void readMainSettings(XPath path, Node config)
      throws ConfigurationException {
    identifier = XPathHelper.valueOf(path, config, "@id");
    baseModule = XPathHelper.valueOf(path, config, "@extends");
    if (!shared) {
      SiteClassLoader classLoader = ((SiteImpl) site).getClassLoader();
      classLoader.addModule(getModuleFolderName());
    } else {
      WebloungeClassLoader classLoader = WebloungeClassLoader.getInstance();
      classLoader.addExtendedClassPath(getFile().getParentFile().getAbsolutePath());
    }
    moduleClass = XPathHelper.valueOf(path, config, "class");
    if (moduleClass == null)
      moduleClass = "ch.o2it.weblounge.core.module.ModuleImpl";
    descriptions = new LocalizableContent();
    LanguageSupport.addDescriptions(path, config, null, descriptions, true);
    String enable = XPathHelper.valueOf(path, config, "enable");
    isEnabled = (enable == null || "true".equals(enable.toLowerCase()));
  }

  /**
   * Reads the performance settings from the module configuration.
   * 
   * @param config
   *          performance configuration node
   * @param path
   *          the XPath object used to parse the configuration
   */
  private void readPerformanceSettings(XPath path, Node config)
      throws ConfigurationException {
    loadfactor = 1;
    if (config == null) {
      log_.debug("No performance settings found for module '" + identifier + "'");
      return;
    }
    String factor = XPathHelper.valueOf(path, config, "loadfactor");
    if (factor != null) {
      try {
        loadfactor = Integer.parseInt(factor);
        if (loadfactor < 1) {
          String msg = "Error when reading action configuration: Loadfactor must be >= 1!";
          log_.error(msg);
        }
      } catch (NumberFormatException e) {
        log_.warn("Loadfactor " + factor + " is not a number! Adjusting to 1");
        loadfactor = -1;
      }
    }
  }

  /**
   * Reads the renderer definitions.
   * 
   * @param config
   *          renderer configuration node
   * @param path
   *          the XPath object used to parse the configuration
   */
  private void readRenderers(XPath path, Node config)
      throws ConfigurationException {
    if (config == null) {
      log_.debug("No renderer definitions found for module '" + identifier + "'");
      return;
    }
    NodeList bundleNodes = XPathHelper.selectList(path, config, "renderer");
    for (int i = 0; i < bundleNodes.getLength(); i++) {
      Node node = bundleNodes.item(i);
      String id = null;
      try {
        id = XPathHelper.valueOf(path, node, "@id");
        log_.debug("Reading renderer bundle '" + id + "'");
        RendererBundleConfiguration bundleConfig = new RendererBundleConfiguration(id, getFile());
        bundleConfig.read(path, node);
        LanguageSupport.addDescriptions(path, node, null, bundleConfig.getDescriptions());

        // Read jsp renderer definitions
        NodeList jspRenderers = XPathHelper.selectList(path, node, "jsp");
        for (int j = 0; j < jspRenderers.getLength(); j++) {
          Node jspNode = jspRenderers.item(j);
          RendererConfigurationImpl rendererConfig = new RendererConfigurationImpl(bundleConfig);
          rendererConfig.init(path, jspNode);
          bundleConfig.define(JSPRenderer.class, rendererConfig);
        }

        // Read xsl renderer definitions
        NodeList xslRenderers = XPathHelper.selectList(path, node, "xsl");
        for (int j = 0; j < xslRenderers.getLength(); j++) {
          Node xslNode = xslRenderers.item(j);
          RendererConfigurationImpl rendererConfig = new RendererConfigurationImpl(bundleConfig);
          rendererConfig.init(path, xslNode);
          bundleConfig.define(XSLRenderer.class, rendererConfig);
        }

        // Read custom renderer definitions
        NodeList customRenderers = XPathHelper.selectList(path, node, "custom");
        for (int j = 0; j < customRenderers.getLength(); j++) {
          Node customNode = customRenderers.item(j);
          RendererConfigurationImpl rendererConfig = new RendererConfigurationImpl(bundleConfig);
          rendererConfig.init(path, customNode);
          try {
            Class<?> clazz = classLoader.loadClass(rendererConfig.getClassName());
            bundleConfig.define(clazz, rendererConfig);
          } catch (ClassNotFoundException e) {
            log_.error("Unable to load custom renderer, since class " + rendererConfig.getClassName() + " was not found!", e);
          }
        }
        renderers.put(id, bundleConfig);
      } catch (Exception e) {
        log_.warn("Error when reading renderer bundle '" + id + "': " + e.getMessage());
      }
    }
  }

  /**
   * Configures the image styles.
   * 
   * @param config
   *          the services node
   * @param path
   *          the XPath object used to parse the configuration
   */
  private void readImagestyles(XPath path, Node config) {
    if (config == null) {
      log_.debug("No image styles found");
      return;
    }
    log_.debug("Configuring imagestyles");
    NodeList styleNodes = XPathHelper.selectList(path, config, "imagestyle");
    for (int j = 0; j < styleNodes.getLength(); j++) {
      Node styleNode = styleNodes.item(j);
      String id = XPathHelper.valueOf(path, styleNode, "@id");
      String composeableAttribute = XPathHelper.valueOf(path, styleNode, "@composeable");
      boolean composeable = composeableAttribute == null || "true".equalsIgnoreCase(composeableAttribute);
      try {
        String mode = XPathHelper.valueOf(path, styleNode, "scalingmode");
        String width = XPathHelper.valueOf(path, styleNode, "width");
        String height = XPathHelper.valueOf(path, styleNode, "height");
        int m = ImageStyle.SCALE_NONE;
        int h = -1;
        int w = -1;
        if (mode != null && !mode.equals("none")) {
          w = Integer.parseInt(width);
          h = Integer.parseInt(height);
          if ((mode != null) && mode.equals("fit")) {
            m = ImageStyle.SCALE_TO_FIT;
          } else if ((mode != null) && mode.equals("fill")) {
            m = ImageStyle.SCALE_TO_FILL;
          } else {
            String msg = "Found unknown scalingmode for imagestyle '" + id + "': " + mode;
            log_.warn(msg);
            throw new ConfigurationException(msg);
          }
        }
        ImageStyleImpl style = new ImageStyleImpl(id, w, h, composeable);
        style.setScalingMode(m);
        LanguageSupport.addDescriptions(path, styleNode, null, style);
        imagestyles.put(id, style);
      } catch (ConfigurationException e) {
        String msg = "Configuration error when reading imagestyle '" + id + "': ";
        log_.warn(msg + e.getCause());
      } catch (Exception e) {
        String msg = "Error reading imagestyle '" + id + "': ";
        log_.warn(msg, e);
      }
    }
    log_.debug("Imagestyles configured");
  }

  /**
   * Reads the action definitions.
   * 
   * @param config
   *          renderer configuration node
   * @param path
   *          the XPath object used to parse the configuration
   */
  private void readActions(XPath path, Node config)
      throws ConfigurationException, ConfigurationException {
    if (config == null) {
      log_.debug("No action definitions found for module '" + identifier + "'");
      return;
    }
    NodeList actionNodes = XPathHelper.selectList(path, config, "action");
    for (int i = 0; i < actionNodes.getLength(); i++) {
      Node node = actionNodes.item(i);
      String id = null;
      try {
        id = XPathHelper.valueOf(path, node, "@id");
        log_.debug("Reading action bundle '" + id + "'");
        ActionBundleConfiguration bundleConfig = new ActionBundleConfiguration(id, this);
        LanguageSupport.addDescriptions(path, node, null, bundleConfig.getDescription());
        bundleConfig.read(path, node);

        // Read handler definitions
        NodeList handlerNodes = XPathHelper.selectList(path, node, "handler");
        for (int j = 0; j < handlerNodes.getLength(); j++) {
          Node handlerNode = handlerNodes.item(j);
          ActionConfigurationImpl actionConfig = new ActionConfigurationImpl(bundleConfig);
          actionConfig.init(path, handlerNode);
          try {
            Class handlerClass = classLoader.loadClass(actionConfig.getClassName());
            handlerClass.newInstance();
            bundleConfig.define(handlerClass, actionConfig);
          } catch (InstantiationException e) {
            log_.error("Error instantiating action handler of type '" + id + "'");
            log_.error("InstatiationException when instantiating action '" + id + "' of module '" + identifier + "': " + e.getMessage());
            continue;
          } catch (IllegalAccessException e) {
            log_.error("Access violation when instantiating action '" + id + "' of module '" + identifier + "': " + e.getMessage());
            log_.error("Error when reading wizard '" + id + "' of module '" + identifier + "': " + e.getMessage());
            continue;
          } catch (NoClassDefFoundError e) {
            log_.error("Class '" + e.getMessage() + "' cannot be found but is required by " + actionConfig.getClassName() + "' in action '" + id + "' of module '" + identifier + "'!");
            continue;
          } catch (ClassNotFoundException e) {
            log_.error("Handler class '" + actionConfig.getClassName() + "' for action '" + id + "' of module '" + identifier + "' not found!");
            continue;
          } catch (Throwable e) {
            if (e.getCause() != null)
              e = e.getCause();
            log_.error("Error creating action handler '" + id + "' of module '" + identifier + "': " + e.getMessage());
            continue;
          }
        }
        this.actions.put(id, bundleConfig);
      } catch (Exception e) {
        log_.error("Error when reading action bundle '" + id + "' of module '" + identifier + "': " + e.getMessage());
        log_.debug("Error when reading action bundle '" + id + "'!", e);
      }
    }
  }

  /**
   * Configures the service settings, such as the preprocessor.
   * 
   * @param config
   *          the services node
   * @param path
   *          the XPath object used to parse the configuration
   */
  private void readServices(XPath path, Node config) {
    if (config == null) {
      log_.debug("No service definitions found for module '" + identifier + "'");
      return;
    }
    log_.debug("Configuring services");
    NodeList serviceNodes = XPathHelper.selectList(path, config, "service");
    for (int i = 0; i < serviceNodes.getLength(); i++) {
      Node serviceNode = serviceNodes.item(i);
      ServiceConfigurationImpl serviceConfig = null;
      try {
        serviceConfig = new ServiceConfigurationImpl();
        serviceConfig.init(path, serviceNode);
        services.put(serviceConfig.getIdentifier(), serviceConfig);
      } catch (ConfigurationException e) {
        log_.error("Error configuring service '" + serviceConfig.getIdentifier() + "' of module '" + identifier + "': " + e.getMessage(), e);
      }
    }
    log_.debug("Services configured");
  }

  /**
   * Configures the job settings.
   * 
   * @param config
   *          the jobs node
   * @param path
   *          the XPath object used to parse the configuration
   */
  private void readJobs(XPath path, Node config) {
    if (config == null) {
      log_.debug("No job definitions found");
      return;
    }
    log_.debug("Configuring cron jobs");

    NodeList jobNodes = XPathHelper.selectList(path, config, "job");
    for (int i = 0; i < jobNodes.getLength(); i++) {
      Node jobNode = jobNodes.item(i);
      ModuleJob job = null;

      // See if job is enabled
      String enabled = XPathHelper.valueOf(path, jobNode, "@enabled");
      if (enabled != null && !"true".equals(enabled))
        continue;

      String className = XPathHelper.valueOf(path, jobNode, "class");
      try {
        job = (ModuleJob) classLoader.loadClass(className).newInstance();
        job.init(path, jobNode);
        jobs.put(job.getIdentifier(), job);
      } catch (ConfigurationException e) {
        log_.debug("Error configuring service!", e.getCause());
        log_.error("Error configuring cronjob '" + job.getName() + "' of module '" + identifier + "': " + e.getMessage());
      } catch (InstantiationException e) {
        log_.error("Error instantiating cronjob '" + className + "' of module '" + identifier + "': " + e.getMessage());
      } catch (IllegalAccessException e) {
        log_.error("Access error instantiating cronjob '" + className + "' of module '" + identifier + "': " + e.getMessage());
      } catch (ClassNotFoundException e) {
        log_.error("Class '" + className + "' for cronjob of module '" + identifier + "' not found: " + e.getMessage());
      } catch (NoClassDefFoundError e) {
        log_.error("Required class '" + className + "' for cronjob of module '" + identifier + "' not found: " + e.getMessage());
      } catch (Exception e) {
        log_.debug("Error configuring job!", e);
        log_.error("Error configuring job '" + ((job != null) ? job.getIdentifier() : "?") + "' of module '" + identifier + "': " + e.getMessage());
      }
    }
    log_.debug("Jobs configured");
  }

  /**
   * Returns the name of the module folder.
   * 
   * @return the module's folder name
   */
  private String getModuleFolderName() {
    return (new File(getFile().getParent())).getName();
  }

}