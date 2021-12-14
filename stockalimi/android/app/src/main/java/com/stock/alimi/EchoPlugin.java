package com.stock.alimi;

import android.util.Log;

import androidx.annotation.NonNull;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

@CapacitorPlugin(name = "Echo")
public class EchoPlugin extends Plugin {

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    String TAG = "echoPlugin";
    boolean sent;

    @Override
    public void load() {
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "콜백:완료" + credential);
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "콜백:실패", e);
            }
            @Override
            public void onCodeSent(@NonNull String verificationId,
                                    @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "콜백:발송성공:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    //인증코드 체크
    @PluginMethod()
    public void codeCheck(PluginCall call) {
        String code = call.getString("code");
        JSObject ret = new JSObject();

        if(mVerificationId == null){
            ret.put("value", "err");
        }else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential, call);
            Log.d(TAG, String.valueOf(credential));
        }
    }

    //클라이언트에서 firebase 토큰 가져오기
    @PluginMethod
    public void getTokenEcho(PluginCall call) {
      FirebaseMessaging.getInstance().getToken()
      .addOnCompleteListener(new OnCompleteListener<String>() {
          @Override
          public void onComplete(@NonNull Task<String> task) {
            if (!task.isSuccessful()) {
              Log.w(TAG, "Fetching FCM registration token failed", task.getException());
              return;
            }

            // Get new FCM registration token
            String token = task.getResult();
            JSObject ret = new JSObject();
            ret.put("value", token);
            call.resolve(ret);
            // Log and toast
            Log.d(TAG, token);
          }
      });
    }

    //인증코드 발송 
    @PluginMethod()
    public void sendSms(PluginCall call) {
        String phoneNumber = call.getString("phone");
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(getActivity())
                        .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        JSObject ret = new JSObject();
        ret.put("value", "complete");
        call.resolve(ret);
    }

    //사용자 인증
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, PluginCall call) {
        JSObject ret = new JSObject();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            Log.d(TAG, String.valueOf(user));
                            ret.put("value", "success");
                            call.resolve(ret);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            }
                            ret.put("value", "fail");
                            call.resolve(ret);
                        }
                    }
                });
    }
}