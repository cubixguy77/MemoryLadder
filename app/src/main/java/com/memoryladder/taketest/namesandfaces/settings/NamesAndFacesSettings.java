package com.memoryladder.taketest.namesandfaces.settings;

import android.os.Parcel;
import android.os.Parcelable;

public class NamesAndFacesSettings implements Parcelable {

    private final int numFaces;
    private final boolean fullFaceDataSet;

    public NamesAndFacesSettings(int numFaces, boolean fullFaceDataSet) {
        this.numFaces = numFaces;
        this.fullFaceDataSet = fullFaceDataSet;
    }

    public int getFaceCount() { return this.numFaces; }
    public boolean isFullFaceDataSet() { return fullFaceDataSet; }

    private NamesAndFacesSettings(Parcel in) {
        numFaces = in.readInt();
        fullFaceDataSet = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numFaces);
        dest.writeByte((byte) (fullFaceDataSet ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Creator<NamesAndFacesSettings> CREATOR = new Creator<NamesAndFacesSettings>() {
        @Override
        public NamesAndFacesSettings createFromParcel(Parcel in) {
            return new NamesAndFacesSettings(in);
        }

        @Override
        public NamesAndFacesSettings[] newArray(int size) {
            return new NamesAndFacesSettings[size];
        }
    };
}