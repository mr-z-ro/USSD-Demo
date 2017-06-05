package com.mattgrasser.ussddemo;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;

/**
 * Created by matt on 2/18/16.
 */
public class UssdScreen {

    public String screenKey;
    public boolean isStrict;
    public LinkedHashSet<String> options;
    public String title;
    public boolean requiresInput;

    public UssdScreen(Activity a, String screenKey) throws JSONException {
        JSONObject ussdScreens = new JSONObject(loadJSONFromAsset(a));

        this.screenKey = (screenKey == null) ? ussdScreens.keys().next() : screenKey;
        JSONObject ussdScreen = ussdScreens.getJSONObject(this.screenKey);

        this.title = ussdScreen.getString("title");
        this.options = new LinkedHashSet<String>();
        if (ussdScreen.has("options")) {
            this.isStrict = ussdScreen.getBoolean("strict");
            JSONArray optionsJsonArr = ussdScreen.getJSONArray("options");
            for (int i = 0; i < optionsJsonArr.length(); i++) {
                options.add(optionsJsonArr.getString(i));
            }
            this.requiresInput = true;
        } else if (ussdScreen.has("confirmation_message")) {
            this.isStrict = false;
            this.options.add(ussdScreens.getJSONObject("confirmation_messages").getString(ussdScreen.getString("confirmation_message")));
            this.requiresInput = false;
        }
    }

    private String loadJSONFromAsset(Activity a) {
        String json = null;
        try {
            InputStream is = a.getAssets().open("ussd_screens.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
