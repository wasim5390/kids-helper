package com.uiu.helper.model.ParseTools;

import com.parse.ParseUser;
import com.uiu.helper.CompanionApp;
import com.uiu.helper.WiserHelper.model.ParseTools.ParseApi;
import com.uiu.helper.WiserHelper.model.ParseTools.SimpleCallback;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by home on 02/03/2017.
 */
public class ParseApiTest {

    private CompanionApp app;

    @Before
    public void setUpParse() {
        app = new CompanionApp();
    }

    @Test
    public void testSignUpNewCompanion() {

        ParseApi.signUpNewCompanion("autotest1@test.com", "autotest1@test.com", app.getBaseContext(),
                new SimpleCallback<ParseUser>() {
                    @Override
                    public void onDone(ParseUser result) {
                        System.out.println("here");
                    }
                });
        }

}