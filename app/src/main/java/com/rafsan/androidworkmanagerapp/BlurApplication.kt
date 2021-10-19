/*
 * *
 *  * Created by Rafsan Ahmad on 10/19/21, 1:28 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *
 */

package com.rafsan.androidworkmanagerapp

import android.app.Application
import timber.log.Timber

class BlurApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}