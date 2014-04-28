package com.swampmobile.scribelinkedin.model;


import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;

import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Matt on 4/15/14.
 */
public class LinkedInOAuthConfig extends OAuthConfig
{
    private String state;

    public LinkedInOAuthConfig(String key, String secret)
    {
        super(key, secret);
        init();
    }

    public LinkedInOAuthConfig(String key, String secret, String callback, SignatureType type, String scope, OutputStream stream)
    {
        super(key, secret, callback, type, scope, stream);
        init();
    }

    private void init()
    {
        state = UUID.randomUUID().toString();
    }

    public String getState()
    {
        return state;
    }
}
