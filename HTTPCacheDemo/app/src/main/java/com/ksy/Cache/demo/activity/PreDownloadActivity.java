package com.ksy.Cache.demo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kingsoft.media.httpcache.KSYProxyService;
import com.kingsoft.media.httpcache.OnCacheStatusListener;
import com.kingsoft.media.httpcache.OnErrorListener;
import com.ksy.Cache.demo.App;
import com.ksy.Cache.demo.MyVideo;
import com.ksy.Cache.demo.R;
import com.ksy.Cache.demo.Settings;
import com.ksy.Cache.demo.VideoPlayerActivity;
import com.ksy.Cache.demo.adapter.PreDownloadAdapter;
import com.ksy.Cache.demo.fragment.SelectDialogFragment;
import com.ksy.Cache.demo.model.PreDownloadStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by xbc on 2017/8/15.
 */

public class PreDownloadActivity extends Activity {

    private ListView mListView;
    private SelectDialogFragment mSelectDialogFragment;

    private PreDownloadAdapter mPreDownloadAdapter;
    private ArrayList<PreDownloadStatus> mPreDownloadList;

    private KSYProxyService mProxyService;

    private SharedPreferences mSettings;

    private int mSelectIndex;
    private String[] mPlayUrls = {
            "https://ks3-cn-beijing.ksyun.com/mobile/264/264_1200_30_720P_yx.mp4",
            "https://mvvideo5.meitudata.com/571090934cea5517.mp4",
            "https://ks3-cn-beijing.ksyun.com/mobile/170621.MOV"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_url);

        mProxyService = App.getKSYProxy(this);

        mListView = (ListView) findViewById(R.id.list_url);

        mSettings = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);

        setDownloadListInfo();

        mPreDownloadAdapter = new PreDownloadAdapter(this, mPreDownloadList);
        mListView.setAdapter(mPreDownloadAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPreDownloadList.clear();

        for (String  url : mPlayUrls)
            mProxyService.unregisterCacheStatusListener(mCacheStatusListener, url);
        mProxyService.unregisterErrorListener(mOnErrorListener);

        mProxyService.shutDownServer();
    }

    private void setDownloadListInfo() {
        ArrayList<String> urls = new ArrayList<>(Arrays.asList(mPlayUrls));
        mPreDownloadList = new ArrayList<>();

        mProxyService.registerErrorListener(mOnErrorListener);
        String cacheMethod= mSettings.getString("choose_cache","undefind");
        if(cacheMethod.equals(Settings.USENUM)){
            mProxyService.setMaxFilesCount(500);
        }else{
            mProxyService.setMaxCacheSize(1000*1024*1024);
        }

        String url = Environment.getExternalStorageDirectory().toString() + File.separator + "CacheTest";
        File file = new File(url);
        if (!file.exists())
            file.mkdirs();

        mProxyService.setCacheRoot(file);
        mProxyService.startServer();

        Iterator iterator = mProxyService.getCachedFileList().entrySet().iterator();
        while (iterator != null && iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            url = (String)entry.getKey();
            if (urls.contains(url)) {
                PreDownloadStatus status = new PreDownloadStatus(url, 100);
                mPreDownloadList.add(status);
                urls.remove(url);
            }
        }
        for(String url1 : urls) {
            int percent = mProxyService.getCachingPercent(url1);
            PreDownloadStatus status = new PreDownloadStatus(url1, percent);
            mPreDownloadList.add(status);
            mProxyService.registerCacheStatusListener(mCacheStatusListener, url1);
        }
    }

    private void showDialogFragment() {
        if (mSelectDialogFragment == null) {
            mSelectDialogFragment = new SelectDialogFragment();
            mSelectDialogFragment.setOnSelectItemListener(mOnSelectItemListener);
        }

        mSelectDialogFragment.show(getFragmentManager(), "select_dialog_fragment");
    }

    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            mSelectIndex = i;
            showDialogFragment();
        }
    };

    private OnCacheStatusListener mCacheStatusListener = new OnCacheStatusListener() {
        @Override
        public void OnCacheStatus(String url, long sourceLength, int percentsAvailable) {
            for(PreDownloadStatus status : mPreDownloadList) {
                if (url.equals(status.url))
                {
                    status.progress = percentsAvailable;
                    mPreDownloadAdapter.notifyDataSetChanged();
                    break;
                }
            }

            Log.e("PreDownloadActivity", "===== OnCacheStatusListener url:"+url+", percent:"+percentsAvailable);
        }
    };

    private OnErrorListener mOnErrorListener = new OnErrorListener() {
        @Override
        public void OnError(int errCode) {
            Toast.makeText(PreDownloadActivity.this, "HttpCache meet an error:"+errCode, Toast.LENGTH_SHORT).show();
        }
    };

    private SelectDialogFragment.OnSelectItemListener mOnSelectItemListener = new SelectDialogFragment.OnSelectItemListener() {
        @Override
        public void onStartPreLoad() {
            if (mProxyService != null) {
                mPreDownloadList.get(mSelectIndex).isCaching = true;
                mPreDownloadAdapter.notifyDataSetChanged();
                mProxyService.startPreDownload(mPreDownloadList.get(mSelectIndex).url);
            }
        }

        @Override
        public void onStopPreLoad() {
            if (mProxyService != null) {
                mPreDownloadList.get(mSelectIndex).isCaching = false;
                mPreDownloadAdapter.notifyDataSetChanged();
                mProxyService.stopPreDownload(mPreDownloadList.get(mSelectIndex).url);
            }
        }

        @Override
        public void onPlayVideo() {
            Intent intent = new Intent(PreDownloadActivity.this, VideoPlayerActivity.class);
            intent.putExtra("path", mPreDownloadList.get(mSelectIndex).url);
            startActivity(intent);
        }
    };
}
