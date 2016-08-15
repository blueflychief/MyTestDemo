package com.example.administrator.mytestdemo.appupdate;

import java.io.Serializable;


public class UpdateBean implements Serializable{
    private int build;
    private String desc;
    private String fileurl;
    private boolean force;
    private String version;

    public int getBuild() {
        return build;
    }

    public UpdateBean setBuild(int build) {
        this.build = build;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public UpdateBean setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public String getFileurl() {
        return fileurl;
    }

    public UpdateBean setFileurl(String fileurl) {
        this.fileurl = fileurl;
        return this;
    }

    public boolean isForce() {
        return force;
    }

    public UpdateBean setForce(boolean force) {
        this.force = force;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public UpdateBean setVersion(String version) {
        this.version = version;
        return this;
    }

    @Override
    public String toString() {
        return "UpdateBean{" +
                "build=" + build +
                ", desc='" + desc + '\'' +
                ", fileurl='" + fileurl + '\'' +
                ", force=" + force +
                ", version='" + version + '\'' +
                '}';
    }
}
