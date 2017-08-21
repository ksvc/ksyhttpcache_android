package com.ksy.Cache.demo.model;

/**
 * Created by xbc on 2017/8/15.
 */

public class PreDownloadStatus {
    public String url;
    public boolean isCaching;
    public int progress;

    public PreDownloadStatus(String path, int pro) {
        url = path;
        progress = pro;
        isCaching = false;
    }
}
