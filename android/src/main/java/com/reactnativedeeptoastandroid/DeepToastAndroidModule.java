package com.reactnativedeeptoastandroid;

import androidx.annotation.NonNull;

import com.facebook.common.logging.FLog;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.module.annotations.ReactModule;


import android.view.Gravity;



@ReactModule(name = DeepToastAndroidModule.NAME)
public class DeepToastAndroidModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
    public static final String NAME = "DeepToastAndroid";

    public DeepToastAndroidModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }



  private android.widget.Toast mostRecentToast;

  // note that webView.isPaused() is not Xwalk compatible, so tracking it poor-man style
  private boolean isPaused;

  @ReactMethod
  public void show(ReadableMap options) throws Exception {
    if (this.isPaused) {
      return;
    }


    final String message = options.getString("message");
    final String duration = options.getString("duration");
    final String position = options.getString("position");
    final int addPixelsY = options.hasKey("addPixelsY") ? options.getInt("addPixelsY") : 0;

    UiThreadUtil.runOnUiThread(new Runnable() {
      public void run() {
        android.widget.Toast toast = android.widget.Toast.makeText(
          getReactApplicationContext(),
          message,
          "short".equals(duration) ? android.widget.Toast.LENGTH_SHORT : android.widget.Toast.LENGTH_LONG);

        if ("top".equals(position)) {
          toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 20 + addPixelsY);
        } else if ("bottom".equals(position)) {
          toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 20 - addPixelsY);
        } else if ("center".equals(position)) {
          toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, addPixelsY);
        } else {
          FLog.e("RCTToast", "invalid position. valid options are 'top', 'center' and 'bottom'");
          return;
        }

        toast.show();
        mostRecentToast = toast;
      }
    });
  }

  @ReactMethod
  public void hide() throws Exception {
    if (mostRecentToast != null) {
      mostRecentToast.cancel();
    }
  }

  @Override
  public void initialize() {
    getReactApplicationContext().addLifecycleEventListener(this);
  }

  @Override
  public void onHostPause() {
    if (mostRecentToast != null) {
      mostRecentToast.cancel();
    }
    this.isPaused = true;
  }

  @Override
  public void onHostResume() {
    this.isPaused = false;
  }

  @Override
  public void onHostDestroy() {
    this.isPaused = true;
  }
}
