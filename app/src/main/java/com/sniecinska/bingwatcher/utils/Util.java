package com.sniecinska.bingwatcher.utils;

/**
 * Created by ewasniecinska on 04.08.2018.
 */

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class Util {

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(18 * 1000); // wait at least
        builder.setOverrideDeadline(20 * 1000); // maximum delay
        builder.setRequiresCharging(false);
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }

}