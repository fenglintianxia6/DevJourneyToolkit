package dev.journey.toolkit.util;

import android.hardware.Camera;

import java.lang.reflect.Field;

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

    /**
     * 判断相机权限
     *
     * @param camera
     * @return
     */
    public static boolean isCameraHasPermission(Camera camera) {
        if (camera == null) {
            return false;
        }

        Class cameraClass = (Class) camera.getClass();
        if (cameraClass == null) {
            return false;
        }

        try {
            Field[] fs = cameraClass.getDeclaredFields();
            if (fs != null) {
                for (int i = 0; i < fs.length; i++) {
                    Field f = fs[i];
                    try {
                        f.setAccessible(true);
                        Object val = f.get(camera);//得到此属性的值
                        if ("mHasPermission".equals(f.getName()) && val instanceof Boolean) {
                            L.d("CameraUtils isCameraHasPermission", "name:" + f.getName() + "\t value = " + val);
                            return (boolean) val;
                        }
                    } catch (Exception e) {
                        // no op
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
}

