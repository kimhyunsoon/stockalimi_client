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

                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "콜백:완료" + credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "콜백:실패", e);


                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                  // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                  // The SMS quota for the project has been exceeded
                }
              // Show a message and update the UI

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                    @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "콜백:발송성공:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

            }
        };
    }


    @PluginMethod()
    public void echo(PluginCall call) {
        JSObject ret = new JSObject();
        ret.put("value", "err");
        call.resolve(ret);
    }
    
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

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, PluginCall call) {
        JSObject ret = new JSObject();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            Log.d(TAG, String.valueOf(user));
                            ret.put("value", "success");
                            call.resolve(ret);
                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                            ret.put("value", "fail");
                            call.resolve(ret);
                        }
                    }
                });
    }
}