package com.jaques.projetos.zapp.helper

import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.jaques.projetos.zapp.activity.SettingsActivity

/** author Leonardo Jaques on 10/11/20 */
class Permission {
    companion object {
        fun validatePermission(
            permissions: ArrayList<String>,
            settingsActivity: SettingsActivity,
            requestCode: Int
        ): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                val listPermission = ArrayList<String>()

                for (permission: String in permissions) {
                    val getPermission = ContextCompat.checkSelfPermission(
                        settingsActivity,
                        permission
                    ) == PackageManager.PERMISSION_GRANTED
                    if (!getPermission) listPermission.add(permission)
                }
                if (listPermission.isEmpty()) return true
                val newPermissions = Array<String>(listPermission.size){listPermission.toString()}

                ActivityCompat.requestPermissions(settingsActivity, newPermissions, requestCode)
            }
            return true
        }
    }
}





