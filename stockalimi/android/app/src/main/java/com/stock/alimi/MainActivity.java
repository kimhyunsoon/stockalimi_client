package com.stock.alimi;

import android.os.Bundle;
import android.webkit.WebView;

import com.getcapacitor.BridgeActivity;
import com.getcapacitor.Plugin;
import com.google.firebase.auth.FirebaseAuth;
import com.getcapacitor.community.fcm.FCMPlugin;
import com.capacitorjs.plugins.storage.StoragePlugin;
import java.util.ArrayList;

public class MainActivity extends BridgeActivity {
  @Override 
  public void onCreate(Bundle savedInstanceState) {
      setTheme(R.style.AppTheme);
      super.onCreate(savedInstanceState);
      this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{
          add(EchoPlugin.class);
          add(FCMPlugin.class);
          add(StoragePlugin.class);
          FirebaseAuth auth = FirebaseAuth.getInstance();
          auth.setLanguageCode("ko");
      }});
  }
}
