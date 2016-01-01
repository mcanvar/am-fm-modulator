package com.mcnvr.amfmmodulator.helpers;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.jjoe64.graphview.series.DataPoint;
import com.mcnvr.amfmmodulator.R;
import com.mcnvr.amfmmodulator.activities.DisplayActivity;
import com.mcnvr.amfmmodulator.models.DoubleParcelable;

/**
 * Created by mevlut on 23.12.2015.
 */
public class PointFactoryTask extends AsyncTask<String, Integer, String> {

    Context context;
    Button buttonPlot;

    DoubleParcelable doublePar;
    DoubleParcelable doubleParC;
    DoubleParcelable doubleParM;
    Integer selection;

    ProgressDialog progressDialog;

    public PointFactoryTask(Context context, Button buttonPlot){
        this.context = context;
        this.buttonPlot = buttonPlot;
    }

    @Override
    protected String doInBackground(String... params) {
        Integer time = Integer.parseInt(params[0]);
        Double amplitude = Double.parseDouble(params[1]);
        Double frequency = Double.parseDouble(params[2]);
        Double phase = Double.parseDouble(params[3]);
        Integer timeC = Integer.parseInt(params[4]);
        Double amplitudeC = Double.parseDouble(params[5]);
        Double frequencyC = Double.parseDouble(params[6]);
        Double phaseC = Double.parseDouble(params[7]);
        selection = Integer.parseInt(params[8]);
        try {
            doublePar = new DoubleParcelable();
            doubleParC = new DoubleParcelable();
            doubleParM = new DoubleParcelable();

            if(selection == 0) {
                DataPoint[] dataPoint = wavePointFactory(time, amplitude, frequency, phase);
                DataPoint[] dataPointC = wavePointFactory(timeC, amplitudeC, frequencyC, phaseC);
                DataPoint[] dataPointM = wavePointFactory(timeC, amplitudeC, frequencyC, phaseC, dataPoint, selection);

                doublePar.setDataPoint(dataPoint);
                doubleParC.setDataPoint(dataPointC);
                doubleParM.setDataPoint(dataPointM);
            }
            else if(selection == 1) {
                DataPoint[] dataPoint = wavePointFactory(time, amplitude, frequency, phase);
                DataPoint[] dataPointC = wavePointFactory(timeC, amplitudeC, frequencyC, phaseC);
                DataPoint[] dataPointM = wavePointFactory(timeC, amplitudeC, frequencyC, phaseC, dataPoint, selection);

                doublePar.setDataPoint(dataPoint);
                doubleParC.setDataPoint(dataPointC);
                doubleParM.setDataPoint(dataPointM);
            }else {
                DataPoint[] dataPoint = wavePointFactory(time, amplitude, frequency, phase);
                DataPoint[] dataPointC = wavePointFactory(timeC, amplitudeC, frequencyC, phaseC);
                DataPoint[] dataPointM = wavePointFactory(timeC, amplitudeC, frequencyC, phaseC, dataPoint, selection);

                doublePar.setDataPoint(dataPoint);
                doubleParC.setDataPoint(dataPointC);
                doubleParM.setDataPoint(dataPointM);
            }
        } catch (Exception e){}

        return "Download complete!";
    }

    @Override
    protected void onPreExecute() {
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB){
            progressDialog = new ProgressDialog(context, R.style.CUSTOM_PROGRESS_DIALOG);
        }else{
            progressDialog = new ProgressDialog(context);
        }
        progressDialog.setTitle("Creating...");
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                PointFactoryTask.this.cancel(true);
                Toast.makeText(context.getApplicationContext(), "Canceled!", Toast.LENGTH_SHORT).show();
            }
        });
        progressDialog.show();

    }

    @Override
    protected void onPostExecute(String result) {

        Intent intentDisplay = new Intent(context, DisplayActivity.class);
        intentDisplay.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentDisplay.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentDisplay.putExtra("data", doublePar);
        intentDisplay.putExtra("datac", doubleParC);
        intentDisplay.putExtra("datam", doubleParM);
        intentDisplay.putExtra("selection", selection);
        progressDialog.hide();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            Bundle bundle = ActivityOptions.makeCustomAnimation(context, R.anim.activity_animation, R.anim.activity_animation2).toBundle();
            context.startActivity(intentDisplay, bundle);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressDialog.setProgress(values[0]);
    }

    //Point maker
    private DataPoint[] wavePointFactory(Integer time, Double amp, Double freq, Double pha) {
        Double startPoint = 0.0;
        Integer arraySize = time * 125;
        DataPoint[] dataPointTemp = new DataPoint[arraySize];

        for (int i=0; i<arraySize; i++) {
            dataPointTemp[i] = new DataPoint(startPoint, (amp * (Math.sin(2 * Math.PI * freq * startPoint + pha ))));
            startPoint = startPoint + 0.008;

            if(isCancelled()) break;
        }
        dataPointTemp[arraySize-1] = new DataPoint(startPoint, (amp * (Math.sin(2 * Math.PI * freq * startPoint + pha ))));

        return dataPointTemp;
    }

    //Overload for MODULATION
    private DataPoint[] wavePointFactory(Integer time, Double amp, Double freq, Double pha, DataPoint[] carrier, Integer selection) {
        Double startPoint = 0.0;
        Integer arraySize = time * 125;
        DataPoint[] dataPointTemp = new DataPoint[arraySize];

        if(selection == 0) {
            for (int i = 0; i < arraySize; i++) {
                dataPointTemp[i] = new DataPoint(startPoint, ((amp + carrier[i].getY()) * (Math.sin(2 * Math.PI * freq * startPoint + pha))));
                startPoint = startPoint + 0.008;

                if(isCancelled()) break;

                int progress = i * 100 / arraySize;
                try {
                    Thread.sleep(2,0);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                publishProgress(progress);
            }
            dataPointTemp[arraySize - 1] = new DataPoint(startPoint, ((amp + carrier[arraySize - 1].getY()) * (Math.sin(2 * Math.PI * freq * startPoint + pha))));
        }
        if(selection == 1) {
            for (int i = 0; i < arraySize; i++) {
                dataPointTemp[i] = new DataPoint(startPoint, (amp * (Math.sin(2 * Math.PI * freq * startPoint + 3 * carrier[i].getY() ))));
                startPoint = startPoint + 0.008;

                if(isCancelled()) break;

                int progress = i * 100 / arraySize;
                publishProgress(progress);
            }
            dataPointTemp[arraySize - 1] = new DataPoint(startPoint, (amp * (Math.sin(2 * Math.PI * freq * startPoint + 3 * carrier[arraySize - 1].getY() ))));
        }
        if(selection == 2) {
            for (int i = 0; i < arraySize-1; i++) {
                dataPointTemp[i] = new DataPoint(startPoint, (amp * (Math.sin(2 * Math.PI * freq * startPoint + 2 * Math.PI * 3 +
                        (carrier[i].getY() - carrier[i+1].getY())/(-0.005) ))));
                startPoint = startPoint + 0.008;

                if(isCancelled()) break;

                int progress = i * 100 / arraySize;
                publishProgress(progress);
            }
            dataPointTemp[arraySize - 1] = new DataPoint(startPoint, (amp * (Math.sin(2 * Math.PI * freq * startPoint +  3 *
                    Math.tan(carrier[arraySize - 1].getY())))));
        }

        return dataPointTemp;
    }
}
