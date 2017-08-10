package com.ksy.Cache.demo;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

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
        viewPager.setCurrentItem(2);
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
        verifyStoragePermissions(this);
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

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    public static void verifyStoragePermissions(Activity activity) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (allRequestsPermitted(grantResults))
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean allRequestsPermitted(int[] grantResults) {
        for (int result : grantResults)
            if (result == PackageManager.PERMISSION_DENIED)
                return false;
        return true;
    }
}
