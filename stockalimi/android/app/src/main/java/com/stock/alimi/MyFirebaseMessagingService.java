package com.stock.alimi;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;

import com.getcapacitor.JSObject;
import com.getcapacitor.PluginHandle;
import com.getcapacitor.BridgeActivity;
import com.getcapacitor.Bridge;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

//node server api 요청시 토큰 확인을 위한 클래스 (실제 수신까지만 하고 수신 후 처리는 하지않음)
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
    }
}