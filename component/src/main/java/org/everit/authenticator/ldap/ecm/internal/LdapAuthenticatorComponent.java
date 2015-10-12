/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.biz)
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
package org.everit.authenticator.ldap.ecm.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import org.everit.authenticator.Authenticator;
import org.everit.authenticator.ldap.LdapAuthenticator;
import org.everit.authenticator.ldap.ecm.LdapAuthenticatorConstants;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.ManualService;
import org.everit.osgi.ecm.annotation.attribute.BooleanAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ComponentContext;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * An {@link Authenticator} that authenticates users on LDAP protocol.
 */
@Component(componentId = LdapAuthenticatorConstants.SERVICE_FACTORYPID_LDAP_AUTHENTICATOR,
    configurationPolicy = ConfigurationPolicy.FACTORY,
    label = "Everit LDAP Authenticator Component",
    description = "LDAP implementation of the authenticator-api. Authenticates the given principal "
        + "and credential on the configured LDAP.")
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = LdapAuthenticatorConstants.DEFAULT_SERVICE_DESCRIPTION_LDAP_AUTHENTICATOR,
        priority = LdapAuthenticatorAttributePriority.P1_SERVICE_DESCRIPTION,
        label = "Service Description",
        description = "The description of this component configuration. It is used to easily "
            + "identify the service registered by this component.") })
@ManualService(Authenticator.class)
public class LdapAuthenticatorComponent {

  private String ldapUrl;

  private ServiceRegistration<Authenticator> serviceRegistration;

  private boolean sslEnabled;

  private String systemUserDn;

  private String systemUserPassword;

  private String userBaseDn;

  private String userDnTemplate;

  private String userSearchBase;

  /**
   * Component activator method.
   */
  @Activate
  public void activate(final ComponentContext<LdapAuthenticatorComponent> componentContext) {
    LdapAuthenticator ldapAuthenticator = new LdapAuthenticator(sslEnabled, ldapUrl, systemUserDn,
        systemUserPassword, userBaseDn, userSearchBase, userDnTemplate);

    Dictionary<String, Object> serviceProperties =
        new Hashtable<>(componentContext.getProperties());

    serviceRegistration =
        componentContext.registerService(Authenticator.class, ldapAuthenticator, serviceProperties);
  }

  /**
   * Component deactivate method.
   */
  @Deactivate
  public void deactivate() throws Exception {
    if (serviceRegistration != null) {
      serviceRegistration.unregister();
    }
  }

  @StringAttribute(attributeId = LdapAuthenticatorConstants.ATTR_LDAP_URL,
      priority = LdapAuthenticatorAttributePriority.P3_LDAP_URL, label = "LDAP URL",
      description = "The LDAP URL to connect to. "
          + "(e.g. ldap://<ldapDirectoryHostname>:<port>, ldap://localhost:10389).")
  public void setLdapUrl(final String ldapUrl) {
    this.ldapUrl = ldapUrl;
  }

  @BooleanAttribute(attributeId = LdapAuthenticatorConstants.ATTR_SSL_ENABLED, defaultValue = false,
      priority = LdapAuthenticatorAttributePriority.P2_SSL_ENABLED, label = "SSL enabled",
      description = "Use SSL during LDAP communication.")
  public void setSslEnabled(final boolean sslEnabled) {
    this.sslEnabled = sslEnabled;
  }

  @StringAttribute(attributeId = LdapAuthenticatorConstants.ATTR_SYSTEM_USER_DN,
      priority = LdapAuthenticatorAttributePriority.P4_SYSTEM_USER_DN, label = "System User DN",
      description = "The DN of the system user used to search for other users. "
          + "(e.g. uid=admin,ou=system).")
  public void setSystemUserDn(final String systemUserDn) {
    this.systemUserDn = systemUserDn;
  }

  @StringAttribute(attributeId = LdapAuthenticatorConstants.ATTR_SYSTEM_USER_PASSWORD,
      priority = LdapAuthenticatorAttributePriority.P5_SYSTEM_USER_PASSWORD,
      label = "System User Password", description = "The password of the system user.")
  public void setSystemUserPassword(final String systemUserPassword) {
    this.systemUserPassword = systemUserPassword;
  }

  @StringAttribute(attributeId = LdapAuthenticatorConstants.ATTR_USER_BASE_DN,
      priority = LdapAuthenticatorAttributePriority.P6_USER_BASE_DN, label = "User Base DN",
      description = "The base DN of the users to search for. (e.g. ou=people,o=sevenSeas).")
  public void setUserBaseDn(final String userBaseDn) {
    this.userBaseDn = userBaseDn;
  }

  @StringAttribute(attributeId = LdapAuthenticatorConstants.ATTR_USER_DN_TEMPLATE,
      priority = LdapAuthenticatorAttributePriority.P8_USER_DN_TEMPLATE, label = "User DN Template",
      description = "The DN template used to create user DN if its authentication succeeds. "
          + "Must contain exactly one substitution token '{0}' that will be replaced by the CN of "
          + "the authenticated user. (e.g. cn={0},ou=people,o=sevenSeas).")
  public void setUserDnTemplate(final String userDnTemplate) {
    this.userDnTemplate = userDnTemplate;
  }

  @StringAttribute(attributeId = LdapAuthenticatorConstants.ATTR_USER_SEARCH_BASE,
      priority = LdapAuthenticatorAttributePriority.P7_USER_SEARCH_BASE, label = "User Serach Base",
      description = "The filter expression to use for the search. Must contain exactly one "
          + "substitution token '{0}' that will be replaced by the users principal. "
          + "(e.g. mail={0})")
  public void setUserSearchBase(final String userSearchBase) {
    this.userSearchBase = userSearchBase;
  }

}
