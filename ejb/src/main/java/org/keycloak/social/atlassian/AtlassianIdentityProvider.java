/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.social.atlassian;

import com.fasterxml.jackson.databind.JsonNode;
import org.jboss.logging.Logger;
import org.keycloak.broker.oidc.AbstractOAuth2IdentityProvider;
import org.keycloak.broker.oidc.mappers.AbstractJsonUserAttributeMapper;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityBrokerException;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.broker.social.SocialIdentityProvider;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.ErrorPageException;
import org.keycloak.services.messages.Messages;

import javax.ws.rs.core.Response;
import java.util.Set;

/**
 * @author <a href="mailto:wadahiro@gmail.com">Hiroyuki Wada</a>
 */
public class AtlassianIdentityProvider extends AbstractOAuth2IdentityProvider<AtlassianIdentityProviderConfig>
        implements SocialIdentityProvider<AtlassianIdentityProviderConfig> {

    private static final Logger log = Logger.getLogger(AtlassianIdentityProvider.class);

    public static final String AUTH_URL = "https://auth.atlassian.com/authorize";
    public static final String TOKEN_URL = "https://auth.atlassian.com/oauth/token";
    public static final String PROFILE_URL = "https://api.atlassian.com/me";
    public static final String DEFAULT_SCOPE = "read:me";
    public static final String DEFAULT_FORWARD_PARAMETER = "audience=api.atlassian.com";

    public AtlassianIdentityProvider(KeycloakSession session, AtlassianIdentityProviderConfig config) {
        super(session, config);
        config.setAuthorizationUrl(AUTH_URL);
        config.setTokenUrl(TOKEN_URL);
        config.setUserInfoUrl(PROFILE_URL);
        config.setForwardParameters(DEFAULT_FORWARD_PARAMETER);
    }

    @Override
    protected boolean supportsExternalExchange() {
        return true;
    }

    @Override
    protected String getProfileEndpointForValidation(EventBuilder event) {
        return PROFILE_URL;
    }

    @Override
    protected BrokeredIdentityContext extractIdentityFromProfile(EventBuilder event, JsonNode profile) {
        BrokeredIdentityContext user = new BrokeredIdentityContext(getJsonProperty(profile, "account_id"));

        user.setUsername(getJsonProperty(profile, "account_id"));
        user.setEmail(getJsonProperty(profile, "email"));
        user.setIdpConfig(getConfig());
        user.setIdp(this);

        AbstractJsonUserAttributeMapper.storeUserProfileForMapper(user, profile, getConfig().getAlias());

        return user;
    }

    @Override
    protected BrokeredIdentityContext doGetFederatedIdentity(String accessToken) {
        log.debug("doGetFederatedIdentity()");
        JsonNode profile = null;
        try {
            profile = SimpleHttp.doGet(PROFILE_URL, session).header("Authorization", "Bearer " + accessToken).asJson();
        } catch (Exception e) {
            throw new IdentityBrokerException("Could not obtain user profile from atlassian.", e);
        }
        return extractIdentityFromProfile(null, profile);
    }


    @Override
    protected String getDefaultScopes() {
        return DEFAULT_SCOPE;
    }
}
