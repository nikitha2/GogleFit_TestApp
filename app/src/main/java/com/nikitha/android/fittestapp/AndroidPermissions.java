package com.nikitha.android.fittestapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import androidx.core.app.ActivityCompat;
import static androidx.core.app.ActivityCompat.requestPermissions;
import static com.nikitha.android.fittestapp.Commons.PERMISSION_REQUEST_ACTIVITY_RECOGNITION;

public class AndroidPermissions {

    String LOG_TAG= AndroidPermissions.class.getSimpleName();
    private static View mLayout;
    static Context context;
    static Activity activity;
    public static int getAndroidPermission(Context context1){
        activity = (Activity) context1;
        context=context1;
        mLayout = activity.findViewById(R.id.main_layout);

            // Check if the Camera permission has been granted
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION)== PackageManager.PERMISSION_GRANTED) {
                // Permission is already available, return 0 so that I can get OAuth Autorizations from repository
                //getOAuthAuthorizationPermission(context);
                return 0;
            } else {
                // Permission is missing and must be requested.
                requestActivityRecognitionPermission(context);
            }
            return 1;
    }

    /**
     * Requests the {@link android.Manifest.permission#ACTIVITY_RECOGNITION} permission.
     */
    private static void requestActivityRecognitionPermission(final Context context) {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context,Manifest.permission.ACTIVITY_RECOGNITION)) {
            // Provide an additional rationale to the user if the permission was not granted and the user would benefit from additional context for the use of the permission.
            requestPermissions(activity,new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PERMISSION_REQUEST_ACTIVITY_RECOGNITION);
        } else {
            // Request the permission. The result will be received in onRequestPermissionResult().
            requestPermissions(activity,new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PERMISSION_REQUEST_ACTIVITY_RECOGNITION);
        }
    }
}
