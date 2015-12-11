package com.mcnvr.amfmmodulator;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.jjoe64.graphview.series.DataPoint;


public class MainActivity extends AppCompatActivity {

    EditText editTextAmplitude;
    EditText editTextFrequency;
    EditText editTextTime;
    EditText editTextPhase;
    EditText editTextCAmplitude;
    EditText editTextCFrequency;
    TextView textViewCTime;
    EditText editTextCPhase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hide keyboard at the beginning
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Components
        Button buttonPlot = (Button) findViewById(R.id.buttonPlot);
        editTextAmplitude = (EditText)findViewById(R.id.editTextAmplitude);
        editTextFrequency = (EditText)findViewById(R.id.editTextFrequency);
        editTextTime = (EditText)findViewById(R.id.editTextTime);
        editTextPhase = (EditText)findViewById(R.id.editTextPhase);
        editTextCAmplitude = (EditText)findViewById(R.id.editTextCAmplitude);
        editTextCFrequency = (EditText)findViewById(R.id.editTextCFrequency);
        textViewCTime = (TextView)findViewById(R.id.editTextCTime);
        editTextCPhase = (EditText)findViewById(R.id.editTextCPhase);
        final Spinner spinnerWaveType = (Spinner) findViewById(R.id.spinnerWaveType);

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
                Toast.makeText(getApplicationContext(),"Empty Value(s)",Toast.LENGTH_LONG).show();
                else {
                    //Load progress window
                    final ProgressDialog progress = ProgressDialog.show(MainActivity.this,
                            "Prepairing Data", "Graphs data is loading...", true, false);

                    //run when load on screen
                    new Thread(new Runnable() {
                        public void run() {

                            //continue based on spinner selected item

                            loadWave(spinnerWaveType.getSelectedItemPosition());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    progress.cancel();
                                }
                            });

                        }
                    }).start();
                }
            }
        });

    }

    //create Wave points as array and change view
    private void loadWave(int selection) {
        Integer time = Integer.parseInt(editTextTime.getText().toString());
        Double amplitude = Double.parseDouble(editTextAmplitude.getText().toString());
        Double frequency = Double.parseDouble(editTextFrequency.getText().toString());
        Double phase = Double.parseDouble(editTextPhase.getText().toString());
        Integer timeC = Integer.parseInt(textViewCTime.getText().toString());
        Double amplitudeC = Double.parseDouble(editTextCAmplitude.getText().toString());
        Double frequencyC = Double.parseDouble(editTextCFrequency.getText().toString());
        Double phaseC = Double.parseDouble(editTextCPhase.getText().toString());

        Intent intentDisplay = new Intent(MainActivity.this, displayActivity.class);
        DoubleParcelable doublePar = new DoubleParcelable();
        DoubleParcelable doubleParC = new DoubleParcelable();
        DoubleParcelable doubleParM = new DoubleParcelable();

        if(selection == 0) {
            DataPoint[] dataPoint = wavePointFactory(time, amplitude, frequency, phase);
            DataPoint[] dataPointC = wavePointFactory(timeC, amplitudeC, frequencyC, phaseC);
            DataPoint[] dataPointM = wavePointFactory(timeC, amplitudeC, frequencyC, phaseC, dataPoint, selection);

            doublePar.setDataPoint(dataPoint);
            doubleParC.setDataPoint(dataPointC);
            doubleParM.setDataPoint(dataPointM);

            intentDisplay.putExtra("data", doublePar);
            intentDisplay.putExtra("datac", doubleParC);
            intentDisplay.putExtra("datam", doubleParM);
            intentDisplay.putExtra("selection", selection);
            startActivity(intentDisplay);
        }
        else if(selection == 1) {
            DataPoint[] dataPoint = wavePointFactory(time, amplitude, frequency, phase);
            DataPoint[] dataPointC = wavePointFactory(timeC, amplitudeC, frequencyC, phaseC);
            DataPoint[] dataPointM = wavePointFactory(timeC, amplitudeC, frequencyC, phaseC, dataPoint, selection);

            doublePar.setDataPoint(dataPoint);
            doubleParC.setDataPoint(dataPointC);
            doubleParM.setDataPoint(dataPointM);

            intentDisplay.putExtra("data", doublePar);
            intentDisplay.putExtra("datac", doubleParC);
            intentDisplay.putExtra("datam", doubleParM);
            intentDisplay.putExtra("selection", selection);
            startActivity(intentDisplay);
        }else {
            DataPoint[] dataPoint = wavePointFactory(time, amplitude, frequency, phase);
            DataPoint[] dataPointC = wavePointFactory(timeC, amplitudeC, frequencyC, phaseC);
            DataPoint[] dataPointM = wavePointFactory(timeC, amplitudeC, frequencyC, phaseC, dataPoint, selection);

            doublePar.setDataPoint(dataPoint);
            doubleParC.setDataPoint(dataPointC);
            doubleParM.setDataPoint(dataPointM);

            intentDisplay.putExtra("data", doublePar);
            intentDisplay.putExtra("datac", doubleParC);
            intentDisplay.putExtra("datam", doubleParM);
            intentDisplay.putExtra("selection", selection);
            startActivity(intentDisplay);
        }
    }

    //Point maker
    private DataPoint[] wavePointFactory(Integer time, Double amp, Double freq, Double pha) {
        Double startPoint = 0.0;
        Integer arraySize = time * 200;
        DataPoint[] dataPointTemp = new DataPoint[arraySize];

        for (int i=0; i<arraySize; i++) {
            dataPointTemp[i] = new DataPoint(startPoint, (amp * (Math.sin(2 * Math.PI * freq * startPoint + pha ))));
            startPoint = startPoint + 0.005;
        }
        dataPointTemp[arraySize-1] = new DataPoint(startPoint, (amp * (Math.sin(2 * Math.PI * freq * startPoint + pha ))));

        return dataPointTemp;
    }

    //Overload for MODULATION
    private DataPoint[] wavePointFactory(Integer time, Double amp, Double freq, Double pha, DataPoint[] carrier, Integer selection) {
        Double startPoint = 0.0;
        Integer arraySize = time * 200;
        DataPoint[] dataPointTemp = new DataPoint[arraySize];

        if(selection == 0) {
            for (int i = 0; i < arraySize; i++) {
                dataPointTemp[i] = new DataPoint(startPoint, ((amp + carrier[i].getY()) * (Math.sin(2 * Math.PI * freq * startPoint + pha))));
                startPoint = startPoint + 0.005;
            }
            dataPointTemp[arraySize - 1] = new DataPoint(startPoint, ((amp + carrier[arraySize - 1].getY()) * (Math.sin(2 * Math.PI * freq * startPoint + pha))));
        }
        if(selection == 1) {
            for (int i = 0; i < arraySize; i++) {
                dataPointTemp[i] = new DataPoint(startPoint, (amp * (Math.sin(2 * Math.PI * freq * startPoint + 3 * carrier[i].getY() ))));
                startPoint = startPoint + 0.005;
            }
            dataPointTemp[arraySize - 1] = new DataPoint(startPoint, (amp * (Math.sin(2 * Math.PI * freq * startPoint + 3 * carrier[arraySize - 1].getY() ))));
        }
        if(selection == 2) {
            for (int i = 0; i < arraySize-1; i++) {
                dataPointTemp[i] = new DataPoint(startPoint, (amp * (Math.sin(2 * Math.PI * freq * startPoint + 2 * Math.PI * 3 +
                        (carrier[i].getY() - carrier[i+1].getY())/(-0.005) ))));
                startPoint = startPoint + 0.005;
            }
            dataPointTemp[arraySize - 1] = new DataPoint(startPoint, (amp * (Math.sin(2 * Math.PI * freq * startPoint +  3 *
                    Math.tan(carrier[arraySize - 1].getY())))));
        }

        return dataPointTemp;
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

}
