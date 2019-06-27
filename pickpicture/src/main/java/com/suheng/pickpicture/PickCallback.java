package com.suheng.pickpicture;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


public class PickCallback implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public static final Parcelable.Creator<PickCallback> CREATOR = new Parcelable.Creator<PickCallback>() {
        @Override
        public PickCallback createFromParcel(Parcel source) {
            return new PickCallback(source);
        }

        @Override
        public PickCallback[] newArray(int size) {
            return new PickCallback[size];
        }
    };

    public PickCallback(Parcel in) {
        in.readParcelable(PickCallback.class.getClassLoader());
    }

    public PickCallback() {
    }

    protected void obtainPicture(String path) {
        Log.d("WBJ", "parent, choose picture = " + path);
    }
}
