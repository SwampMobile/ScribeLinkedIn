package com.swampmobile.scribelinkedin.service;


import android.util.Log;

import com.swampmobile.scribelinkedin.model.LinkedInOAuthConfig;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuth20ServiceImpl;

/**
 * Created by Matt on 4/15/14.
 */
public class LinkedInOAuth20ServiceImpl extends OAuth20ServiceImpl
{
    private static final String TAG = "LinkedInOAuth20ServiceImpl";

    private DefaultApi20 api;
    private LinkedInOAuthConfig config;

    /**
     * Default constructor
     *
     * @param api    OAuth2.0 api information
     * @param config OAuth 2.0 configuration param object
     */
    public LinkedInOAuth20ServiceImpl(DefaultApi20 api, LinkedInOAuthConfig config)
    {
        super(api, config);

        Log.i(TAG, "being constructed");

        this.api = api;
        this.config = config;
    }

    @Override
    public Token getAccessToken(Token requestToken, Verifier verifier)
    {
        OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        Log.i(TAG, "Initial access token request url: " + request.toString());
        request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
        request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        request.addQuerystringParameter("grant_type", "authorization_code");
        if(config.hasScope()) request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
        applyLinkedInHeadersToRequest(request);
        Log.i(TAG, "Access token request url: " + request.toString());
        Response response = request.send();
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    @Override
    public void signRequest(Token accessToken, OAuthRequest request)
    {
        request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getToken());
        applyLinkedInHeadersToRequest(request);
    }

    private void applyLinkedInHeadersToRequest(OAuthRequest request)
    {
        request.addHeader("Accept", "application/json, application/*+json");
        request.addHeader("x-li-format", "json");
        request.addHeader("Connection", "Keep-Alive");
//        request.addBodyParameter("grant_type", "authorization_code");
    }
}
