package com.ksy.Cache.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ksy.Cache.demo.R;
import com.ksy.Cache.demo.model.PreDownloadStatus;

import java.util.ArrayList;

/**
 * Created by xbc on 2017/8/15.
 */

public class PreDownloadAdapter extends BaseAdapter {

    private ArrayList<PreDownloadStatus> mPreDownloadStatus;
    private LayoutInflater mLayoutInflater;

    public PreDownloadAdapter(Context context, ArrayList<PreDownloadStatus> statuses) {
        mLayoutInflater = LayoutInflater.from(context);
        mPreDownloadStatus = statuses;
    }

    @Override
    public int getCount() {
        if (mPreDownloadStatus != null)
            return mPreDownloadStatus.size();

        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (mPreDownloadStatus != null)
            return mPreDownloadStatus.get(i);

        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            holder = new ViewHolder();
            view = mLayoutInflater.inflate(R.layout.item_pre_download, null);
            holder.downloadUrl = (TextView) view.findViewById(R.id.pre_download_url);
            holder.downloadStatus = (TextView) view.findViewById(R.id.pre_download_state);
            holder.downloadProgress = (TextView) view.findViewById(R.id.pre_download_progress);
            view.setTag(holder);
        }

        PreDownloadStatus status = mPreDownloadStatus.get(i);

        holder.downloadUrl.setText(status.url);
        if (status.isCaching)
            holder.downloadStatus.setText(R.string.pre_download_state_caching);
        else
            holder.downloadStatus.setText(R.string.pre_download_state_pausing);
        if (status.progress == 100)
            holder.downloadStatus.setText(R.string.pre_download_state_done);
        holder.downloadProgress.setText(status.progress+"%");

        return view;
    }

    private class ViewHolder {
        TextView downloadUrl;
        TextView downloadStatus;
        TextView downloadProgress;
    }
}
