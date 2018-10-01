package com.mcnvr.amfmmodulator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.mcnvr.amfmmodulator.R;


public class PrivacyActivity extends AppCompatActivity {
    TextView textPrivacy;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);


        textPrivacy = findViewById(R.id.textView12);

        textPrivacy.setText(
                Html.fromHtml(getString(R.string.privacy_text))
        );
    }


    public void backToMain(View view)
    {
        Intent intent = new Intent(PrivacyActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
