/*
 * *
 *  * Created by Rafsan Ahmad on 10/19/21, 1:28 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *
 */

package com.rafsan.androidworkmanagerapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.rafsan.androidworkmanagerapp.databinding.ActivityBlurBinding
import com.rafsan.androidworkmanagerapp.viewmodel.BlurViewModel
import com.rafsan.androidworkmanagerapp.viewmodel.ViewModelFactory

class BlurImageActivity : AppCompatActivity() {

    private lateinit var viewModel: BlurViewModel
    private lateinit var binding: ActivityBlurBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlurBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the ViewModel
        viewModel = ViewModelProvider(
            this, ViewModelFactory(
                owner = this
            )
        )
            .get(BlurViewModel::class.java)
    }
}