/*
 * *
 *  * Created by Rafsan Ahmad on 10/21/21, 8:04 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *
 */

package com.rafsan.androidworkmanagerapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import com.rafsan.androidworkmanagerapp.utils.KEY_IMAGE_URI
import com.rafsan.androidworkmanagerapp.utils.WorkManagerTestRule
import com.rafsan.androidworkmanagerapp.utils.copyFileFromTestToTargetCtx
import com.rafsan.androidworkmanagerapp.utils.uriFileExists
import com.rafsan.androidworkmanagerapp.workers.SaveImageToFileWorker
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test

class SaveImageWorkerTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var wmRule = WorkManagerTestRule()

    @Test
    fun testSaveImageWork() {

        val testUri = copyFileFromTestToTargetCtx(
            wmRule.testContext, wmRule.targetContext, "test_image.png"
        )
        MatcherAssert.assertThat(
            uriFileExists(wmRule.targetContext, testUri.toString()),
            CoreMatchers.`is`(true)
        )
        // Create request
        val worker = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()

        //get input data from Uri
        val builder = Data.Builder()
        builder.putString(KEY_IMAGE_URI, testUri.toString())
        val inputData = builder.build()
        worker.setInputData(inputData)
        val request = worker.build()

        // Enqueue and wait for result. This also runs the Worker synchronously
        // because we are using a SynchronousExecutor.
        wmRule.workManager.enqueue(request).result.get()
        // Get WorkInfo
        val workInfo = wmRule.workManager.getWorkInfoById(request.id).get()

        // Assert
        MatcherAssert.assertThat(
            uriFileExists(wmRule.targetContext, testUri.toString()),
            CoreMatchers.`is`(true)
        )
        MatcherAssert.assertThat(workInfo.state, CoreMatchers.`is`(WorkInfo.State.SUCCEEDED))
    }
}