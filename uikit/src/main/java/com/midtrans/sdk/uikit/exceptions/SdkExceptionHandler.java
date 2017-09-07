package com.midtrans.sdk.uikit.exceptions;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.midtrans.sdk.uikit.utilities.UiKitConstants;

/**
 * Created by ziahaqi on 9/4/17.
 */

public class SdkExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = SdkExceptionHandler.class.getSimpleName();
    public static final String EXTRA_CRASH_CODE = "extra.crash";

    private final Activity activity;
    private final Thread.UncaughtExceptionHandler rootHandler;

    public SdkExceptionHandler(Activity activity) {
        this.activity = activity;
        this.rootHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        Log.e(TAG, "error:" + thread.getName() + " | " + ex.getMessage());
        if (activity != null && ex instanceof RuntimeException) {
            PackageManager packageManager = activity.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(activity.getPackageName());

            intent.putExtra(EXTRA_CRASH_CODE, UiKitConstants.INTENT_CODE_CRASH);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(activity.getBaseContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
            AlarmManager mgr = (AlarmManager) activity.getBaseContext().getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent);

            activity.finish();
            System.exit(2);
        } else {
            rootHandler.uncaughtException(thread, ex);
        }
    }
}
