package com.ksy.Cache.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
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


public class CacheFragment extends Fragment {

    public static final int UPDATECACHED = 1;

    private KSYProxyService proxy;
    private ListView cached_list;
    private ArrayList<MyVideo> mycachedlist;
    private CachedAdapter adapter;

    public CacheFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cache, container, false);
        cached_list = (ListView) view.findViewById(R.id.cached_list);
        initlist();

        return view;
    }


    public void initlist(){
        proxy = App.getKSYProxy(getActivity());
        proxy.startServer();
        mycachedlist = new ArrayList<MyVideo>();
        new File(Environment.getExternalStorageDirectory(),"cachetest");
        Iterator iter = proxy.getCachedFileList().entrySet().iterator();
        while (iter.hasNext()) {
            MyVideo myVideo = new MyVideo();
            Map.Entry entry = (Map.Entry) iter.next();
            String url = (String)entry.getKey();
            File file = (File)entry.getValue();
            String path = file.getAbsolutePath();
            myVideo.setUrl(url);
            myVideo.setPath(path);
            myVideo.setName(url.substring(url.lastIndexOf('/')+1));
            mycachedlist.add(myVideo);
        }
        adapter = new CachedAdapter(getActivity(),mycachedlist,proxy);
        cached_list.setAdapter(adapter);
        cached_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyVideo v = mycachedlist.get(i);
                Intent intent = new Intent(getActivity(),VideoPlayerActivity.class);
                intent.putExtra("path", v.getUrl());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        proxy.shutDownServer();
    }
}
