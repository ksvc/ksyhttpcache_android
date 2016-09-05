package com.ksy.Cache.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingsoft.media.httpcache.KSYProxyService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liubohua on 16/8/24.
 */
public class CachedAdapter extends BaseAdapter {
    private ArrayList<MyVideo> mycachedlist = new ArrayList<MyVideo>();
    private LayoutInflater mLayoutInflater;
    private MyVideoThumbLoader mVideoThumbLoader;


    public CachedAdapter(Context context, ArrayList<MyVideo> mycachedlist,KSYProxyService proxy){
        mLayoutInflater = LayoutInflater.from(context);
        this.mycachedlist.addAll(mycachedlist);
        mVideoThumbLoader = new MyVideoThumbLoader();

    }

    @Override
    public int getCount() {
        return mycachedlist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mLayoutInflater.inflate(R.layout.cached_view, null);
            holder.img = (ImageView)view.findViewById(R.id.img_cached);
            holder.title = (TextView)view.findViewById(R.id.tittle_cached);
            holder.url = (TextView)view.findViewById(R.id.url_cached);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }
        String path = mycachedlist.get(i).getPath();
        holder.img.setTag(path);
        mVideoThumbLoader.showThumbByAsynctack(path, holder.img);
        holder.title.setText(mycachedlist.get(i).getName());
        holder.url.setText(mycachedlist.get(i).getUrl());
        return view;
    }

    public final class ViewHolder{
        public ImageView img;
        public TextView title;
        public TextView url;
    }
}
