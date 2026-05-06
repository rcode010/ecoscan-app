package com.ecoscan.app.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.ecoscan.app.data.EcoScanDatabase;
import com.ecoscan.app.data.Pantry.PantryItem;
import com.ecoscan.app.utils.NotificationHelper;

import java.util.List;

public class ExpiryWorker extends Worker {

    public ExpiryWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        EcoScanDatabase db = EcoScanDatabase.getInstance(context);

        long twoDaysFromNow = System.currentTimeMillis() + (2 * 24 * 60 * 60 * 1000);
        List<PantryItem> expiringSoon = db.pantryDao().getItemsExpiringSoonSync(twoDaysFromNow);

        if (expiringSoon != null && !expiringSoon.isEmpty()) {
            for (PantryItem item : expiringSoon) {
                NotificationHelper.sendExpiryNotification(context, item.itemName, item.id);
            }
        }

        return Result.success();
    }
}