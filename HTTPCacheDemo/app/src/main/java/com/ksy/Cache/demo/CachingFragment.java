package com.ksy.Cache.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kingsoft.media.httpcache.KSYProxyService;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class CachingFragment extends Fragment {
    private ListView caching_list;
    public static final int UPDATECACHING = 2;

    private KSYProxyService proxy;
    private ArrayList<CachingVideo> mycachinglist;
    private CachingAdapter adapter;

    public CachingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_caching, container, false);
        caching_list = (ListView) view.findViewById(R.id.list_caching);
        initView();
        return view ;
    }

    private void initView() {
        proxy = App.getKSYProxy(getActivity());
        proxy.startServer();
        mycachinglist = new ArrayList<CachingVideo>();
        new File(Environment.getExternalStorageDirectory(),"cachetest");
        Iterator iter = proxy.getCachingPercentsList().entrySet().iterator();
        while (iter.hasNext()) {
            CachingVideo myVideo = new CachingVideo();
            Map.Entry entry = (Map.Entry) iter.next();
            String url = (String)entry.getKey();
            int num = (Integer) entry.getValue();
            Log.d("cachetest",num+"");
            String name = url.substring(url.lastIndexOf('/')+1);
            myVideo.setUrl(url);
            myVideo.setNum(num);
            myVideo.setName(name);
            mycachinglist.add(myVideo);
        }
        adapter = new CachingAdapter(getActivity(),mycachinglist);
        caching_list.setAdapter(adapter);
        caching_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CachingVideo v = mycachinglist.get(i);
                Intent intent = new Intent(getActivity(),VideoPlayerActivity.class);
                intent.putExtra("path", v.getUrl());
                startActivity(intent);
            }
        });
    }
}
