/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.authenticator.ldap.ecm.tests;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.everit.authenticator.ldap.ecm.LdapAuthenticatorConstants;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * Configures the LdapAuthenticatorComponent dynamically based on the used random port.
 */
@Component(componentId = "ConfigurationInit", configurationPolicy = ConfigurationPolicy.OPTIONAL)
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@Service(value = ConfigurationInitComponent.class)
public class ConfigurationInitComponent {

  private ConfigurationAdmin configurationAdmin;

  private String ldapAuthenticatorConfigurationPid;

  private LdapPortProvider ldapPortProvider;

  /**
   * Configures the LdapAuthenticatorComponent.
   */
  @Activate
  public void activate() throws IOException {
    Configuration configuration = configurationAdmin.createFactoryConfiguration(
        LdapAuthenticatorConstants.SERVICE_FACTORYPID_LDAP_AUTHENTICATOR, null);
    ldapAuthenticatorConfigurationPid = configuration.getPid();
    Dictionary<String, Object> properties = new Hashtable<>();
    properties.put(LdapAuthenticatorConstants.ATTR_SSL_ENABLED, false);
    properties.put(LdapAuthenticatorConstants.ATTR_LDAP_URL,
        "ldap://localhost:" + ldapPortProvider.getPort());
    properties.put(LdapAuthenticatorConstants.ATTR_SYSTEM_USER_DN, "uid=admin,ou=system");
    properties.put(LdapAuthenticatorConstants.ATTR_SYSTEM_USER_PASSWORD, "secret");
    properties.put(LdapAuthenticatorConstants.ATTR_USER_BASE_DN, "ou=people,o=sevenSeas");
    properties.put(LdapAuthenticatorConstants.ATTR_USER_SEARCH_BASE, "mail={0}");
    properties
        .put(LdapAuthenticatorConstants.ATTR_USER_DN_TEMPLATE, "cn={0},ou=people,o=sevenSeas");
    configuration.update(properties);
  }

  /**
   * Deletes the configuration of the LdapAuthenticatorComponent.
   */
  @Deactivate
  public void deactivate() throws IOException {
    Configuration configuration = configurationAdmin
        .getConfiguration(ldapAuthenticatorConfigurationPid);
    configuration.delete();
  }

  @ServiceRef(defaultValue = "")
  public void setConfigurationAdmin(final ConfigurationAdmin configurationAdmin) {
    this.configurationAdmin = configurationAdmin;
  }

  @ServiceRef(defaultValue = "")
  public void setLdapPortProvider(final LdapPortProvider ldapPortProvider) {
    this.ldapPortProvider = ldapPortProvider;
  }

}
