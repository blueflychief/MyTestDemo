package com.example.administrator.mytestdemo.appupdate;

import android.os.Parcel;
import android.os.Parcelable;

public class UpdateBean implements Parcelable {
    public int build;
    public String desc;
    public String fileurl;
    public boolean force;
    public String version;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.build);
        dest.writeString(this.desc);
        dest.writeString(this.fileurl);
        dest.writeByte(this.force ? (byte) 1 : (byte) 0);
        dest.writeString(this.version);
    }

    public UpdateBean() {
    }

    protected UpdateBean(Parcel in) {
        this.build = in.readInt();
        this.desc = in.readString();
        this.fileurl = in.readString();
        this.force = in.readByte() != 0;
        this.version = in.readString();
    }

    public static final Parcelable.Creator<UpdateBean> CREATOR = new Parcelable.Creator<UpdateBean>() {
        @Override
        public UpdateBean createFromParcel(Parcel source) {
            return new UpdateBean(source);
        }

        @Override
        public UpdateBean[] newArray(int size) {
            return new UpdateBean[size];
        }
    };
}
