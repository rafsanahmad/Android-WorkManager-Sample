/*
 * *
 *  * Created by Rafsan Ahmad on 10/19/21, 1:28 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *
 */

package com.rafsan.androidworkmanagerapp.viewmodel

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.rafsan.androidworkmanagerapp.BlurApplication

/**
 * Factory for ViewModels
 */
class ViewModelFactory(
    owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(BlurViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BlurApplication.instance?.let { BlurViewModel(it) } as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
