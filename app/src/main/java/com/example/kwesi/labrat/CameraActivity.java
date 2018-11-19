package com.example.kwesi.labrat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import java.util.Collections;

import static com.example.kwesi.labrat.RightingReflex.REQUEST_VIDEO_CAPTURE;

public class CameraActivity extends AppCompatActivity {

    private CameraManager cameraManager;
    private int cameraOrientation;
    private Size previewSize;
    private String cameraId;
    private CameraDevice.StateCallback stateCallback;
    private Handler backgroundHandler;
    private HandlerThread backgroundThread;
    private CameraDevice cameraDevice;
    private TextureView textureView;
    private TextureView.SurfaceTextureListener stl;
    private CameraCaptureSession captureSession;
    private CaptureRequest.Builder captureRequestBuilder;
    private CaptureRequest captureRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

       requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_VIDEO_CAPTURE);

       textureView = findViewById(R.id.texture_view);
        cameraManager = (CameraManager) getSystemService(this.CAMERA_SERVICE);
        cameraOrientation = CameraCharacteristics.LENS_FACING_BACK;
        stl = new TextureView.SurfaceTextureListener(){

            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                try {
                    setupCamera();
                    openCamera();
                }
                catch(CameraAccessException cae){
                    cae.printStackTrace();
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        };
        stateCallback = createStateCallback();

    }

    @Override
    protected void onResume(){
        super.onResume();
        openBackgroundThread();
        if (textureView.isAvailable()) {
            try {
                setupCamera();
                openCamera();
            }
            catch (CameraAccessException cae){
                cae.printStackTrace();
            }

        } else {
            textureView.setSurfaceTextureListener(stl);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        closeCamera();
        closeBackgroundThread();
    }

    private void closeCamera() {
        if (captureSession != null) {
            captureSession.close();
            captureSession = null;
        }

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    private void closeBackgroundThread() {
        if (backgroundHandler != null) {
            backgroundThread.quitSafely();
            backgroundThread = null;
            backgroundHandler = null;
        }
    }



    private void createPreviewSession() {
        try {
            SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            Surface previewSurface = new Surface(surfaceTexture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(previewSurface);

            cameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                            if (cameraDevice == null) {
                                return;
                            }

                            try {
                                captureRequest = captureRequestBuilder.build();
                                CameraActivity.this.captureSession = cameraCaptureSession;
                                CameraActivity.this.captureSession.setRepeatingRequest(captureRequest,
                                        null, backgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                        }
                    }, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private CameraDevice.StateCallback createStateCallback(){
        CameraDevice.StateCallback scb = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(CameraDevice cameraDevice) {
                CameraActivity.this.cameraDevice = cameraDevice;
                createPreviewSession();
            }

            @Override
            public void onDisconnected(CameraDevice cameraDevice) {
                cameraDevice.close();
                CameraActivity.this.cameraDevice = null;
            }

            @Override
            public void onError(CameraDevice cameraDevice, int error) {
                cameraDevice.close();
                CameraActivity.this.cameraDevice = null;
            }

        };
            return scb;
    }
    private void setupCamera() throws CameraAccessException {

        for(int i = 0;i < cameraManager.getCameraIdList().length;i++){
            CameraCharacteristics currChars = cameraManager.getCameraCharacteristics(cameraManager.getCameraIdList()[i]);
            if(currChars.get(CameraCharacteristics.LENS_FACING) == cameraOrientation){
                StreamConfigurationMap streamConfigurationMap = currChars.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                previewSize = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];
                cameraId = cameraManager.getCameraIdList()[i];
            }
        }
    }

    private void openCamera() throws CameraAccessException{
        if (checkSelfPermission(android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            cameraManager.openCamera(cameraId, stateCallback, backgroundHandler);
        }
    }

    private void openBackgroundThread() {
        backgroundThread = new HandlerThread("camera_background_thread");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }
}
