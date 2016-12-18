package com.mcnvr.amfmmodulator.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.madx.updatechecker.lib.UpdateRunnable;
import com.mcnvr.amfmmodulator.R;
import com.mcnvr.amfmmodulator.helpers.PointFactoryTask;


public class MainActivity extends AppCompatActivity {

    EditText editTextAmplitude;
    EditText editTextFrequency;
    EditText editTextTime;
    EditText editTextPhase;
    EditText editTextCAmplitude;
    EditText editTextCFrequency;
    TextView textViewCTime;
    EditText editTextCPhase;
    Spinner spinnerWaveType;
    Button buttonPlot;
    Button buttonInfo;
    InterstitialAd mInterstitialAd;
    private AdView mAdView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new UpdateRunnable(this, new Handler()).start();


        MobileAds.initialize(this, "ca-app-pub-2926708254200421~2569596021");
        mAdView = (AdView) findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2926708254200421/6999795622");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                startPlotting();
            }
        });

        requestNewInterstitial();

        //Hide keyboard at the beginning
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Components
        buttonPlot = (Button) findViewById(R.id.buttonPlot);
        buttonInfo = (Button) findViewById(R.id.buttonInfo);
        editTextAmplitude = (EditText)findViewById(R.id.editTextAmplitude);
        editTextFrequency = (EditText)findViewById(R.id.editTextFrequency);
        editTextTime = (EditText)findViewById(R.id.editTextTime);
        editTextPhase = (EditText)findViewById(R.id.editTextPhase);
        editTextCAmplitude = (EditText)findViewById(R.id.editTextCAmplitude);
        editTextCFrequency = (EditText)findViewById(R.id.editTextCFrequency);
        textViewCTime = (TextView)findViewById(R.id.editTextCTime);
        editTextCPhase = (EditText)findViewById(R.id.editTextCPhase);
        spinnerWaveType = (Spinner) findViewById(R.id.spinnerWaveType);

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        Boolean isUsed = sharedPreferences.contains("is_saved");
        if(isUsed){
            spinnerWaveType.setSelection(sharedPreferences.getInt("spinnerWaveType", 2));
            editTextAmplitude.setText(sharedPreferences.getString("editTextAmplitude", ""));
            editTextFrequency.setText(sharedPreferences.getString("editTextFrequency", ""));
            editTextTime.setText(sharedPreferences.getString("editTextTime", ""));
            editTextPhase.setText(sharedPreferences.getString("editTextPhase", ""));
            editTextCAmplitude.setText(sharedPreferences.getString("editTextCAmplitude", ""));
            editTextCFrequency.setText(sharedPreferences.getString("editTextCFrequency", ""));
            textViewCTime.setText(sharedPreferences.getString("textViewCTime", ""));
            editTextCPhase.setText(sharedPreferences.getString("editTextCPhase", ""));
        }

        //Keep sync the times
        editTextTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textViewCTime.setText(editTextTime.getText());
            }
        });

        //Plot button action
        buttonPlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    startPlotting();
                }
            }
        });
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String data = "Amplitude modulation (AM) is a modulation technique used in electronic communication, most commonly for transmitting information via a radio carrier wave. In amplitude modulation, the amplitude (signal strength) of the carrier wave is varied in proportion to the waveform being transmitted.\n" +
                        "\n" +
                        "c(t) = A . sin( 2.π.fc.t)\n" +
                        "m(t) = M . cos( 2.π.fm.t + φ)\n" +
                        "y(t) = [1 + m(t)] . c(t)\n" +
                        "\n" +
                        "Angle modulation is a class of analog modulation. These techniques are based on altering the angle (or phase) of a carrier signal to transmit data. This as opposed to varying the amplitude of the carrier, such as in amplitude modulation transmission.\n" +
                        "\n" +
                        "s(t) = A . cos( 2.π.fc.t + φ(t) )\n" +
                        "PM: φ(t) = np . m(t), where np is parameter called phase modulation index.\n" +
                        "FM: φ'(t) = nf . m(t), where nf is the frequency modulation index and φ’(t).";

                final Snackbar snackbar = Snackbar.make(view, data, Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("GOT IT", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                View snackbarView = snackbar.getView();
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setMaxLines(50);
                snackbar.setActionTextColor(Color.RED).show();
            }
        });

    }

    private void startPlotting() {
        if(editTextTime.getText().toString().matches("") ||
                editTextAmplitude.getText().toString().matches("") ||
                editTextFrequency.getText().toString().matches("") ||
                editTextPhase.getText().toString().matches("") ||
                textViewCTime.getText().toString().matches("") ||
                editTextCAmplitude.getText().toString().matches("") ||
                editTextCFrequency.getText().toString().matches("") ||
                editTextCPhase.getText().toString().matches(""))
            Toast.makeText(getApplicationContext(),"Empty Value(s)!",Toast.LENGTH_LONG).show();
        else {
            Integer selection = spinnerWaveType.getSelectedItemPosition();
            PointFactoryTask pointFactoryTask = new PointFactoryTask(MainActivity.this, buttonPlot);

            pointFactoryTask.execute(editTextTime.getText().toString(), editTextAmplitude.getText().toString(),
                    editTextFrequency.getText().toString(), editTextPhase.getText().toString(),
                    textViewCTime.getText().toString(), editTextCAmplitude.getText().toString(),
                    editTextCFrequency.getText().toString(), editTextCPhase.getText().toString(),
                    selection.toString());
        }
    }

    //clear form
    public void onButtonClick(View v){

        switch (v.getId()){
            case R.id.buttonReset:
                editTextAmplitude.setText("");
                editTextFrequency.setText("");
                editTextTime.setText("");
                editTextPhase.setText("");
                editTextCAmplitude.setText("");
                editTextCFrequency.setText("");
                textViewCTime.setText("");
                editTextCPhase.setText("");
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor outState = preferences.edit();

        outState.putBoolean("is_saved", true);
        outState.putInt("spinnerWaveType", spinnerWaveType.getSelectedItemPosition());
        outState.putString("editTextAmplitude", editTextAmplitude.getText().toString());
        outState.putString("editTextFrequency", editTextFrequency.getText().toString());
        outState.putString("editTextTime", editTextTime.getText().toString());
        outState.putString("editTextPhase", editTextPhase.getText().toString());
        outState.putString("editTextCAmplitude", editTextCAmplitude.getText().toString());
        outState.putString("editTextCFrequency", editTextCFrequency.getText().toString());
        outState.putString("textViewCTime", textViewCTime.getText().toString());
        outState.putString("editTextCPhase", editTextCPhase.getText().toString());
        outState.commit();
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
