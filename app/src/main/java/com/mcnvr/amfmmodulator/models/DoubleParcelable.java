package com.mcnvr.amfmmodulator.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.jjoe64.graphview.series.DataPoint;

/**
 * Created by mevlut on 9.12.2015.
 */
public class DoubleParcelable implements Parcelable{

    public DataPoint[] datapoints;

    public DataPoint[] getDataPoints() {
        return datapoints;
    }

    public void setDataPoint(DataPoint[] datapoints) {
        this.datapoints = datapoints;
    }

    public DoubleParcelable() {
        datapoints = new DataPoint[1];
    }

    public DoubleParcelable(Parcel in) {
        datapoints = (DataPoint[]) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(datapoints);

    }
    public static final Parcelable.Creator<DoubleParcelable> CREATOR = new Parcelable.Creator<DoubleParcelable>() {

        @Override
        public DoubleParcelable createFromParcel(Parcel in) {
            return new DoubleParcelable(in);
        }

        @Override
        public DoubleParcelable[] newArray(int size) {
            return new DoubleParcelable[size];
        }
    };
}