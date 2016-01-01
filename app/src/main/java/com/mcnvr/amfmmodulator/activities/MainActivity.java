package com.mcnvr.amfmmodulator.activities;

import android.content.SharedPreferences;
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

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hide keyboard at the beginning
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Components
        buttonPlot = (Button) findViewById(R.id.buttonPlot);
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
        });

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
}