/*
 * *
 *  * Created by Rafsan Ahmad on 10/20/21, 4:45 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *
 */

package com.rafsan.androidworkmanagerapp.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.rafsan.androidworkmanagerapp.utils.OUTPUT_PATH
import com.rafsan.androidworkmanagerapp.utils.makeStatusNotification
import com.rafsan.androidworkmanagerapp.utils.sleep
import timber.log.Timber
import java.io.File

class CleanupWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {


    /**
     * Cleans up temporary files generated during blurring process
     */
    override fun doWork(): Result {
        // Makes a notification when the work starts and slows down the work so that
        // it's easier to see each WorkRequest start, even on emulated devices
        makeStatusNotification("Cleaning up old temporary files", applicationContext)
        sleep()

        return try {
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                if (entries != null) {
                    for (entry in entries) {
                        val name = entry.name
                        if (name.isNotEmpty() && name.endsWith(".png")) {
                            val deleted = entry.delete()
                            Timber.i("Deleted $name - $deleted")
                        }
                    }
                }
            }
            Result.success()
        } catch (exception: Exception) {
            Timber.e(exception)
            Result.failure()
        }
    }
}