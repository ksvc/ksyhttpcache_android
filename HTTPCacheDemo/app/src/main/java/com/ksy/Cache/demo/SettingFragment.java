package com.ksy.Cache.demo;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kingsoft.media.httpcache.KSYProxyService;

import java.io.File;


public class SettingFragment extends Fragment implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{


    private SharedPreferences settings ;
    private SharedPreferences.Editor editor;

    private Button btn_clean;
    private Button btn_save;
    private RadioButton radio_num;
    private RadioButton radio_size;
    private RadioButton radio_hw;
    private RadioButton radio_sw;
    private RadioGroup group_cache;
    private RadioGroup group_decode;

    private String choosecache ;
    private String choosedecode ;

    private KSYProxyService proxy;

    public SettingFragment() {
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        btn_clean = (Button)view.findViewById(R.id.btn_clean);
        radio_size = (RadioButton) view.findViewById(R.id.radio_size);
        radio_num = (RadioButton) view.findViewById(R.id.radio_num);
        radio_hw = (RadioButton) view.findViewById(R.id.radio_hw);
        radio_sw = (RadioButton) view.findViewById(R.id.radio_sw);
        group_cache = (RadioGroup) view.findViewById(R.id.group_cache);
        group_decode = (RadioGroup) view.findViewById(R.id.group_decode);

        group_cache.setOnCheckedChangeListener(this);
        group_decode.setOnCheckedChangeListener(this);

        settings = getActivity().getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        editor = settings.edit();
        choosedecode = settings.getString("choose_decode","信息为空");
        choosecache = settings.getString("choose_cache","信息为空");
        Log.d("decodeaa",choosecache+"  "+choosedecode);

        initSetting(choosecache,choosedecode);

        btn_clean.setOnClickListener(this);

        return view ;
    }

    private void initSetting(String choosecache, String choosedecode) {
        switch (choosedecode){
            case Settings.USEHARD:
                group_decode.check(radio_hw.getId());

                break;
            case Settings.USESOFT:
                group_decode.check(radio_sw.getId());
                break;
            default:
                group_decode.check(radio_hw.getId());
                editor.putString("choose_decode",Settings.USEHARD);
                break;
        }

        switch (choosecache){
            case Settings.USENUM:
                group_cache.check(radio_num.getId());

                break;
            case Settings.USESIZE:
                group_cache.check(radio_size.getId());
                break;
            default:
                group_cache.check(radio_size.getId());
                editor.putString("choose_cache",Settings.USESIZE);
                break;
        }

        editor.commit();

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_clean:
                proxy = App.getKSYProxy(getActivity());
                proxy.setCacheRoot(new File(Environment.getExternalStorageDirectory(),"cachetest"));
                proxy.startServer();
                proxy.cleanCaches();
                proxy.shutDownServer();
                Toast.makeText(getActivity(), "清理缓存成功", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch(i){
            case R.id.radio_hw:
                editor.putString("choose_decode",Settings.USEHARD);
                break;
            case R.id.radio_sw:
                editor.putString("choose_decode",Settings.USESOFT);
                break;
            case R.id.radio_size:
                editor.putString("choose_cache",Settings.USESIZE);
                break;
            case  R.id.radio_num:
                editor.putString("choose_cache",Settings.USENUM);
                break;
            default:
                break;
        }
        editor.commit();

    }
}
