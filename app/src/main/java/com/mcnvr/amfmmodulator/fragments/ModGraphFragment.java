package com.mcnvr.amfmmodulator.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.mcnvr.amfmmodulator.R;
import com.mcnvr.amfmmodulator.activities.DisplayActivity;
import com.mcnvr.amfmmodulator.models.DoubleParcelable;


public class ModGraphFragment extends Fragment {
    GraphView graphView;
    DoubleParcelable parcelable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_graph, container, false);

        TextView textView = (TextView) view.findViewById(R.id.textViewTitle);
        textView.setText(getResources().getString(R.string.modulatedSignalText));
        graphView = (GraphView) view.findViewById(R.id.graphViewData);
        DisplayActivity displayActivity = (DisplayActivity) getActivity();
        parcelable = displayActivity.getParcelableModulated();
        displayActivity.initializeGraph(graphView, parcelable);

        return view;
    }
}
