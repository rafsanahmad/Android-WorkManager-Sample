/*
 * *
 *  * Created by Rafsan Ahmad on 10/19/21, 1:28 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *
 */

package com.rafsan.androidworkmanagerapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rafsan.androidworkmanagerapp.R
import com.rafsan.androidworkmanagerapp.databinding.ActivitySelectBinding
import com.rafsan.androidworkmanagerapp.utils.KEY_IMAGE_URI
import timber.log.Timber
import java.util.*

class SelectImageActivity : AppCompatActivity() {

    private val REQUEST_CODE_IMAGE = 100
    private val REQUEST_CODE_PERMISSIONS = 101

    private val KEY_PERMISSIONS_REQUEST_COUNT = "KEY_PERMISSIONS_REQUEST_COUNT"
    private val MAX_NUMBER_REQUEST_PERMISSIONS = 2

    private val permissions = Arrays.asList(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var permissionRequestCount: Int = 0
    private lateinit var binding: ActivitySelectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.let {
            permissionRequestCount = it.getInt(KEY_PERMISSIONS_REQUEST_COUNT, 0)
        }

        // Make sure the app has correct permissions to run
        requestPermissionsIfNecessary()

        // Create request to get image from filesystem when button clicked
        binding.selectImage.setOnClickListener {
            val chooseIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(chooseIntent, REQUEST_CODE_IMAGE)
        }
    }

    /** Save the permission request count on a rotate  */

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_PERMISSIONS_REQUEST_COUNT, permissionRequestCount)
    }

    /**
     * Request permissions twice - if the user denies twice then show a toast about how to update
     * the permission for storage. Also disable the button if we don't have access to pictures on
     * the device.
     */
    private fun requestPermissionsIfNecessary() {
        if (!checkAllPermissions()) {
            if (permissionRequestCount < MAX_NUMBER_REQUEST_PERMISSIONS) {
                permissionRequestCount += 1
                ActivityCompat.requestPermissions(
                    this,
                    permissions.toTypedArray(),
                    REQUEST_CODE_PERMISSIONS
                )
            } else {
                Toast.makeText(
                    this,
                    R.string.set_permissions_in_settings,
                    Toast.LENGTH_LONG
                ).show()
                binding.selectImage.isEnabled = false
            }
        }
    }

    /** Permission Checking  */
    private fun checkAllPermissions(): Boolean {
        var hasPermissions = true
        for (permission in permissions) {
            hasPermissions = hasPermissions and isPermissionGranted(permission)
        }
        return hasPermissions
    }

    private fun isPermissionGranted(permission: String) =
        ContextCompat.checkSelfPermission(this, permission) ==
                PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            requestPermissionsIfNecessary() // no-op if permissions are granted already.
        }
    }

    /** Image Selection  */
    @SuppressLint("TimberArgCount")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> data?.let { handleImageRequestResult(data) }
                else -> Timber.d("Unknown request code.")
            }
        } else {
            Timber.e(String.format("Unexpected Result code %s", resultCode))
        }
    }

    private fun handleImageRequestResult(intent: Intent) {
        // If clipdata is available, we use it, otherwise we use data
        val imageUri: Uri? = intent.clipData?.let {
            it.getItemAt(0).uri
        } ?: intent.data

        if (imageUri == null) {
            Timber.e("Invalid input image Uri.")
            return
        }

        val filterIntent = Intent(this, BlurImageActivity::class.java)
        filterIntent.putExtra(KEY_IMAGE_URI, imageUri.toString())
        startActivity(filterIntent)
    }
}