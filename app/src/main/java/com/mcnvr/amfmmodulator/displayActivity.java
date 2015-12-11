package com.mcnvr.amfmmodulator;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class displayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Button buttonShowDWave = (Button) findViewById(R.id.buttonShowDWave);
        Button buttonShowCWave = (Button) findViewById(R.id.buttonShowCWave);
        Button buttonShowMWave = (Button) findViewById(R.id.buttonShowMWave);
        Button buttonShowMixWave = (Button) findViewById(R.id.buttonShowMixWave);
        final TextView textViewShow = (TextView) findViewById(R.id.textViewShow);

        final GraphView graph = (GraphView) findViewById(R.id.graph);

        final DoubleParcelable parcelable = getIntent().getExtras().getParcelable("data");
        final DoubleParcelable parcelableCarrier = getIntent().getExtras().getParcelable("datac");
        final DoubleParcelable parcelableModulated = getIntent().getExtras().getParcelable("datam");
        Integer selection = new Integer(getIntent().getExtras().getInt("selection"));

        if(selection == 0)
            textViewShow.setText(getResources().getString(R.string.showWaveTypeAM));
        else if(selection == 1)
            textViewShow.setText(getResources().getString(R.string.showWaveTypePM));
        else
            textViewShow.setText(getResources().getString(R.string.showWaveTypeFM));

        buttonShowDWave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeGraph(graph, parcelable);
                graph.setTitle(getResources().getString(R.string.showDataWave));
            }
        });

        buttonShowCWave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeGraph(graph, parcelableCarrier);
                graph.setTitle(getResources().getString(R.string.showCWave));
            }
        });

        buttonShowMWave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeGraph(graph, parcelableModulated);
                graph.setTitle(getResources().getString(R.string.showMWave));
            }
        });

        buttonShowMixWave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeGraph(graph, parcelable, parcelableModulated);
                graph.setTitle(getResources().getString(R.string.showMixWave));
            }
        });

        initializeGraph(graph, parcelable);

    }

    private void initializeGraph(GraphView graph, DoubleParcelable parcelable) {
        graph.removeAllSeries();

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(parcelable.getDataPoints());
        graph.addSeries(series);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
    }

    //Override for mixed type
    private void initializeGraph(GraphView graph, DoubleParcelable parcelable,  DoubleParcelable parcelable2) {
        graph.removeAllSeries();

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(parcelable.getDataPoints());
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(parcelable2.getDataPoints());

        series.setColor(Color.RED);

        graph.addSeries(series);
        graph.addSeries(series2);

        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
    }
}
