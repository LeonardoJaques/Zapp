package com.jaques.projetos.zapp.activity

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.jaques.projetos.zapp.R
import com.jaques.projetos.zapp.helper.Permission
import de.hdodenhof.circleimageview.CircleImageView


class SettingsActivity : AppCompatActivity() {

    private val requestCode = 1
    private val SECTIONCAMERA = 100
    private val SECTIONGALLERY = 200

    private val permissionList = listOf(

        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,

        )

    private lateinit var circleImageView_imagePerfil: CircleImageView
    private lateinit var managePermissions: Permission
    private lateinit var iButtonGallery: ImageButton
    private lateinit var iButtonCamera: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        iButtonCamera = findViewById(R.id.imageButtonCamera)
        iButtonGallery = findViewById(R.id.imageButtonGallery)
        circleImageView_imagePerfil = findViewById(R.id.circleImageView_image)

        managePermissions = Permission(this, permissionList, requestCode)

        // Function to check permissions states
        checkVersion()

        val toolbar: Toolbar = findViewById(R.id.toolbarMain)
        toolbar.title = "Configurações"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        iButtonCamera.setOnClickListener {
            val mediaIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (mediaIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(mediaIntent, SECTIONCAMERA)
            }
        }

        iButtonGallery.setOnClickListener {
            val mediaIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            if (mediaIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(mediaIntent, SECTIONGALLERY)
            }
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
                when (requestCode) {

                    //get the image with camera
                    SECTIONCAMERA -> {
                        val imageBitmap = data?.extras?.get("data") as Bitmap
                        circleImageView_imagePerfil.setImageBitmap(imageBitmap)

                    }
                    //get the image in gallery
                    SECTIONGALLERY -> {
                        val imageUri = data?.data
                        circleImageView_imagePerfil.setImageURI(imageUri) }
                }

        } catch (e: Exception){
                 e.printStackTrace()
        }
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
                val isPermissionsGranted = managePermissions.processPermissionsResult(
                    requestCode,
                    permissions,
                    grantResults
                )
                if (isPermissionsGranted) {
                    // Do the task now
                    Toast.makeText(this, "Permissão concedida.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Permissão revogada.", Toast.LENGTH_LONG).show()
                }
                return
            }
        }

    }


    }

