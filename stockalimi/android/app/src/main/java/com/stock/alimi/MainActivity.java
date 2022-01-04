package com.stock.alimi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.getcapacitor.BridgeActivity;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginLoadException;
import com.google.firebase.auth.FirebaseAuth;
import com.getcapacitor.community.fcm.FCMPlugin;
import com.capacitorjs.plugins.storage.StoragePlugin;
import java.util.ArrayList;

public class MainActivity extends BridgeActivity {
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            EchoPlugin plugin = (EchoPlugin)this.getBridge().getPlugin("Echo").load();
            plugin.goNotificationList();
        } catch (PluginLoadException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        registerPlugin(com.getcapacitor.community.admob.AdMob.class);
        this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{
            add(EchoPlugin.class);
            add(FCMPlugin.class);
            add(StoragePlugin.class);
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.setLanguageCode("ko");
        }});
    }
}
