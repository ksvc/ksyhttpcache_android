package com.ksy.Cache.demo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    private CacheFragment cacheFragment;
    private SettingFragment settingFragment;
    private URLFragment urlFragment;
    private CachingFragment cachingFragment;

    private PagerTabStrip pager_tab;

    private ArrayList<String> titlelist;
    private ArrayList<Fragment> fraglist;

    @Override
    protected void onResume() {
        super.onResume();
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titlelist = new ArrayList<String>();

        fraglist = new ArrayList<Fragment>();
        urlFragment = new URLFragment();
        cacheFragment = new CacheFragment();
        settingFragment = new SettingFragment();
        cachingFragment = new CachingFragment();
        fraglist.add(cacheFragment);
        fraglist.add(cachingFragment);
        fraglist.add(urlFragment);
        fraglist.add(settingFragment);


        pager_tab = (PagerTabStrip)findViewById(R.id.pager_tab);
        pager_tab.setTabIndicatorColor(getResources().getColor(R.color.colorAccent));

        pager_tab.setDrawFullUnderline(false);
        pager_tab.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        pager_tab.setTextSpacing(50);

        titlelist.add("已完成");
        titlelist.add("未完成");
        titlelist.add("播放");
        titlelist.add("设置");

        viewPager = (ViewPager)findViewById(R.id.my_viewpager);

    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fraglist.size();
        }


        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titlelist.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return fraglist.get(position);
        }

    }
}
