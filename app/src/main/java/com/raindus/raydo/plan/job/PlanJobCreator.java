package com.raindus.raydo.plan.job;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by Raindus on 2018/4/10.
 */

public class PlanJobCreator implements JobCreator {

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case PlanJob.TAG:
                return new PlanJob();
            case PlanRemindJob.TAG:
                return new PlanRemindJob();
            default:
                return null;
        }
    }
}
