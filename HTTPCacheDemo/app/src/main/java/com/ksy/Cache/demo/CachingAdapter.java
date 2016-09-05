package com.ksy.Cache.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by liubohua on 16/8/25.
 */
public class CachingAdapter extends BaseAdapter{
    private ArrayList<CachingVideo> mycachinglist = new ArrayList<CachingVideo>();
    private LayoutInflater mLayoutInflater;

    public CachingAdapter(Context context,ArrayList<CachingVideo> mycachinglist){
        mLayoutInflater = LayoutInflater.from(context);
        this.mycachinglist.addAll(mycachinglist);

    }
    @Override
    public int getCount() {
        return mycachinglist.size();
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
            view = mLayoutInflater.inflate(R.layout.caching_view, null);
            holder.name = (TextView)view.findViewById(R.id.tittle_caching);
            holder.url = (TextView)view.findViewById(R.id.url_caching);
            holder.num = (TextView) view.findViewById(R.id.hascached_caching);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }
        holder.name.setText(mycachinglist.get(i).getName());
        holder.url.setText(mycachinglist.get(i).getUrl());
        holder.num.setText(mycachinglist.get(i).getNum()+"%");
        return view;
    }

    public final class ViewHolder{
        public TextView name;
        public TextView url;
        public TextView num;
    }
}
