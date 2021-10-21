/*
 * *
 *  * Created by Rafsan Ahmad on 10/21/21, 7:46 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *
 */

package com.rafsan.androidworkmanagerapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import com.rafsan.androidworkmanagerapp.utils.copyFileFromTestToTargetCtx
import com.rafsan.androidworkmanagerapp.utils.uriFileExists
import com.rafsan.androidworkmanagerapp.workers.CleanupWorker
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class CleanupWorkerTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var wmRule = WorkManagerTestRule()

    @Test
    fun testCleanupWork() {
        val testUri = copyFileFromTestToTargetCtx(
            wmRule.testContext, wmRule.targetContext, "test_image.png"
        )
        assertThat(uriFileExists(wmRule.targetContext, testUri.toString()), `is`(true))

        // Create request
        val request = OneTimeWorkRequestBuilder<CleanupWorker>().build()

        // Enqueue and wait for result. This also runs the Worker synchronously
        // because we are using a SynchronousExecutor.
        wmRule.workManager.enqueue(request).result.get()
        // Get WorkInfo
        val workInfo = wmRule.workManager.getWorkInfoById(request.id).get()

        // Assert
        assertThat(uriFileExists(wmRule.targetContext, testUri.toString()), `is`(false))
        assertThat(workInfo.state, `is`(WorkInfo.State.SUCCEEDED))
    }
}