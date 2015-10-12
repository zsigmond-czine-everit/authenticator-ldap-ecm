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

/**
 * Constants of LdapAuthenticator attribute priority.
 */
public final class LdapAuthenticatorAttributePriority {

  public static final int P1_SERVICE_DESCRIPTION = 1;

  public static final int P2_SSL_ENABLED = 2;

  public static final int P3_LDAP_URL = 3;

  public static final int P4_SYSTEM_USER_DN = 4;

  public static final int P5_SYSTEM_USER_PASSWORD = 5;

  public static final int P6_USER_BASE_DN = 6;

  public static final int P7_USER_SEARCH_BASE = 7;

  public static final int P8_USER_DN_TEMPLATE = 8;

  private LdapAuthenticatorAttributePriority() {
  }
}
