package com.jaques.projetos.zapp.helper

import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


/** author Leonardo Jaques on 10/11/20 */


class Permission(
    val activity: Activity,
    private val list: List<String>,
    private val code: Int
) {

    // Check permissions at runtime
   open fun checkPermissions() {
        if (isPermissionsGranted() != PackageManager.PERMISSION_GRANTED) {
            showAlert()
        }
    }

    // Check permissions status
    private fun isPermissionsGranted(): Int {
//         PERMISSION_GRANTED : Constant Value: 0
//         PERMISSION_DENIED : Constant Value: -1
        var counter = 0;
        for (permission in list) {
            counter += ContextCompat.checkSelfPermission(activity, permission)
        }
        return counter
    }

    // Find the first denied permission
    private fun deniedPermission() {
        for (permission in list) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_DENIED
            ) return checkPermissions()
        }
//        return ""
    }

    // Show alert dialog to request permissions
    private fun showAlert() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Permissões Nessesárias")
        builder.setMessage("Algumas permissões são obrigatórias para continuar")
        builder.setPositiveButton("Confirmar") { dialog, which -> requestPermissions() }
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
    }

    // Request the permissions at run time
    private fun requestPermissions() {
        val permission = deniedPermission()
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.toString())) {
            // Show an explanation asynchronously
            Toast.makeText(
                activity,
                "Infelizmente seu app será encerrado",
                Toast.LENGTH_LONG
            )
                .show()
            activity.finish()
        } else {
            ActivityCompat.requestPermissions(activity, list.toTypedArray(), code)
        }

    }

    // Process permissions result
    fun processPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ): Boolean {
        var result = 0
        if (grantResults.isNotEmpty()) {
            for (item in grantResults) {
                result += item
            }
        }
        if (result == PackageManager.PERMISSION_GRANTED) return true
        return false
    }

}


