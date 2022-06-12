package com.bangkit.yourpetcare

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat

class PermissionUtility(private val context: Context, var permissions: Array<String>) {
    private val PERMISSIONS: Array<String> = permissions
    fun arePermissionsEnabled(): Boolean {
        for (permission in PERMISSIONS) {
            Log.d("permission ","status is "+ActivityCompat.checkSelfPermission(
                context,
                permission
            ))
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) return false
        }
        return true
    }

    fun requestMultiplePermissions() {
        val remainingPermissions: MutableList<String> = ArrayList()
        for (permission in PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                remainingPermissions.add(permission)
            }
        }
        ActivityCompat.requestPermissions(
            (context as Activity),
            remainingPermissions.toTypedArray(),
            1011
        )
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: MutableList<String>,
        grantResults: IntArray
    ): Boolean {
        if (requestCode == 1011) {
            for (i in grantResults.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            (context as Activity),
                            permissions[i]!!
                        )
                    ) {
                        requestMultiplePermissions()
                    }
                    return false
                }
            }
        }
        return true
    }

}