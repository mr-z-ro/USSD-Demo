package com.mattgrasser.ussddemo;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class UssdScreenActivity extends AppCompatActivity implements EditText.OnEditorActionListener {

    private EditText et;
    private UssdScreen screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ussd_screen);

        et = (EditText) findViewById(R.id.menu_choice);
        et.setOnEditorActionListener(this);

        LinearLayout menuOptionsLayout = (LinearLayout) findViewById(R.id.menu_options);

        String name = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("menu_name", null);
        }

        try {
            screen = new UssdScreen(this, name);
            menuOptionsLayout.removeAllViews();

            if (!screen.requiresInput) {
                et.setVisibility(View.GONE);
            }

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) LinearLayout.LayoutParams.MATCH_PARENT,(int)LinearLayout.LayoutParams.WRAP_CONTENT);
            int i = 1;
            for (String o : screen.options) {
                TextView tv = new TextView(this);
                if (screen.isStrict) {
                    tv.setText(i + ". " + o);
                } else {
                    tv.setText(o);
                }
                tv.setTextColor(Color.parseColor("#ffffff"));
                tv.setTextSize((float) 16);
                tv.setLayoutParams(lp);
                menuOptionsLayout.addView(tv);
                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            String choice = et.getText().toString();
            String newMenuKey = null;
            if (screen.isStrict) {
                if (Integer.parseInt(choice) <= 0 || Integer.parseInt(choice) > screen.options.size()) {
                    Toast.makeText(this, "Your selection was out of range.", Toast.LENGTH_LONG).show();
                    return false;
                }
                newMenuKey = screen.screenKey + "." + choice;
            } else {
                newMenuKey = screen.screenKey + ".1";
            }

            if (newMenuKey != null) {
                Intent i = new Intent(UssdScreenActivity.this, UssdScreenActivity.class);
                i.putExtra("menu_name", newMenuKey);
                startActivity(i);
                finish();
                return true;
            }
        }
        return false;
    }
}
