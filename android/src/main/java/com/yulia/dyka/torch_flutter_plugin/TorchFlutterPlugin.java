package com.yulia.dyka.torch_flutter_plugin;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** TorchFlutterPlugin */
public class TorchFlutterPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;
  private CameraManager cameraManager = null;
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "torch_flutter_plugin");
    context = flutterPluginBinding.getApplicationContext();
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      cameraManager = (CameraManager)context.getSystemService(Context.CAMERA_SERVICE);
    }
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    switch (call.method) {
      case "getPlatformVersion":
        result.success("Android " + VERSION.RELEASE);
        break;
      case "checkTorchAvailable":
        result.success(hasFlashlight());
        break;
      case "turnTorchOn":
        turnTorch(result, true);
        break;
      case "turnTorchOff":
        turnTorch(result, false);
        break;
      default:
        result.notImplemented();
        break;
    }
  }
  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  private boolean hasFlashlight() {
    try {
      if (VERSION.SDK_INT >= VERSION_CODES.M) {
        if (cameraManager.getCameraIdList().length > 0) {
          String cameraID = cameraManager.getCameraIdList()[0];
          CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraID);
          return Boolean.TRUE.equals(characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE));
        } else {
          return false;
        }
      } else {
        return context.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
      }
    } catch (CameraAccessException e) {
      System.out.println("Camera access error: " + e);
      return false;
    }
  }

  //@RequiresApi(api = VERSION_CODES.LOLLIPOP)
  private void turnTorch(Result result, boolean isOn) {
    if (!hasFlashlight()) {
      result.error("NO_FLASHLIGHT_ERROR", "This device doesn't have the flashlight", null);
      return;
    }

    if (VERSION.SDK_INT >= VERSION_CODES.M) {
      try {
        cameraManager.setTorchMode(cameraManager.getCameraIdList()[0], isOn);
        result.success(null);
      } catch (CameraAccessException e) {
        result.error("CAMERA_ACCESS_ERROR", e.toString(), e);
      }
    } else {
      try {
        Camera camera = Camera.open();
        Camera.Parameters params = camera.getParameters();
        params.setFlashMode(isOn ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.startPreview();
        result.success(null);
      } catch (Exception e) {
        result.error("CAMERA_ACCESS_ERROR", e.toString(), null);
      }
    }
  }
}
