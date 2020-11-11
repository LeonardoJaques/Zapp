package com.jaques.projetos.zapp.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.jaques.projetos.zapp.R
import com.jaques.projetos.zapp.helper.Permission


class SettingsActivity : AppCompatActivity() {

    private val requestCode = 1
    private lateinit var managePermissions: Permission


    private val permissionList = listOf<String>(

        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,

        )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        managePermissions = Permission(this, permissionList, requestCode)

        // Function to check permissions states
        checkVersion()

        val toolbar: Toolbar = findViewById(R.id.toolbarMain)
        toolbar.title = "Configurações"
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }


    private fun checkVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            managePermissions.checkPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            requestCode -> {
                val isPermissionsGranted = managePermissions
                    .processPermissionsResult(requestCode, permissions, grantResults)
                if (isPermissionsGranted) {
                    // Do the task now
                    Toast.makeText(this, "Permissions granted.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Permissions denied.", Toast.LENGTH_LONG).show()

                }
                return
            }
        }

    }
}


