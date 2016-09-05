package com.ksy.Cache.demo;

import android.content.Context;

import com.kingsoft.media.httpcache.KSYProxyService;


public class App{

    private static KSYProxyService proxyService = null;
    private static Context cc;
    private static App app = null;
    public static KSYProxyService getKSYProxy(Context context) {
        cc = context;
        if(app == null){
            app = new App();
        }
        return app.proxyService == null ? (app.proxyService = newKSYProxy()) : app.proxyService;
    }

    private static KSYProxyService newKSYProxy() {
        return new KSYProxyService(cc);
    }
}
