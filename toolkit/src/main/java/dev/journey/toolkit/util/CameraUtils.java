package dev.journey.toolkit.util;

import android.hardware.Camera;

/**
 * Created by mwp on 16/4/14.
 */
public class CameraUtils {

    /**
     * 找到前置摄像头id
     *
     * @return
     */
    public static int getFrontFacingCameraId() {
        int frontFacingCameraId = 0;
        final int cameraCount = Camera.getNumberOfCameras();
        if (cameraCount >= 2) {
            frontFacingCameraId = 1;//当手机摄像头有2个时，默认认为第二个摄像头是前置摄像头
        }
        for (int i = 0; i < cameraCount; i++) {
            Camera.CameraInfo tempInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, tempInfo);
            if (Camera.CameraInfo.CAMERA_FACING_FRONT == tempInfo.facing) {
                frontFacingCameraId = i;//找到前置摄像头
                break;
            }
        }
        return frontFacingCameraId;
    }
}


