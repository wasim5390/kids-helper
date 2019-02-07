package com.uiu.helper.parse;

import android.test.ApplicationTestCase;


import com.uiu.helper.CompanionApp;

import org.junit.After;
import org.junit.BeforeClass;

import unittests.api.ParseApiClient;
import unittests.util.Constants;

/**
 * @author android
 */
public abstract class BaseTestCase extends ApplicationTestCase<CompanionApp> {

    private final static ParseApiClient parseApiClient = new ParseApiClient();

    public BaseTestCase() {
        super(CompanionApp.class);
    }

    @BeforeClass
    public static void setUpOnce() {
        parseApiClient.setIsDebug(Constants.DEBUG);
    }
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
    /**
     * @return {@link ParseApiClient} instance.
     */
    protected final ParseApiClient getParseApiClient() {
        return parseApiClient;
    }
}
