/*
 * *
 *  * Created by Rafsan Ahmad on 10/19/21, 1:28 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *
 */

package com.rafsan.androidworkmanagerapp

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import timber.log.Timber

class BlurApplication : Application(), Configuration.Provider {

    init {
        instance = this
    }

    companion object {
        public var instance: BlurApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return if (BuildConfig.DEBUG) {
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.DEBUG)
                .build()
        } else {
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.ERROR)
                .build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}