package com.jaques.projetos.zapp.activity

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import com.jaques.projetos.zapp.R
import com.jaques.projetos.zapp.helper.Base64Custom
import com.jaques.projetos.zapp.helper.Permission
import com.jaques.projetos.zapp.helper.UserFirebase
import com.jaques.projetos.zapp.settings.SettingsFirebase
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.io.OutputStream


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
    private lateinit var storageReference: StorageReference
    private lateinit var idUser: String
    private lateinit var  imageBitmap: Bitmap
    private lateinit var  imageUri: Uri


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)



        iButtonCamera = findViewById(R.id.imageButtonCamera)
        iButtonGallery = findViewById(R.id.imageButtonGallery)
        circleImageView_imagePerfil = findViewById(R.id.circleImageView_image)

        idUser = UserFirebase.getIdUser()
        storageReference = SettingsFirebase.getFirebaseStorage()

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


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {

            when (requestCode) {

                    //get the image with camera
                    SECTIONCAMERA -> {
                        imageBitmap = data?.extras?.get("data") as Bitmap
                        circleImageView_imagePerfil.setImageBitmap(imageBitmap)

                    }
                    //get the image in gallery
                    SECTIONGALLERY -> {
                        imageUri = data?.data!!
                        circleImageView_imagePerfil.setImageURI(imageUri)
                    }

                }

            //get image in firebase
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 70,baos )

            val imageData = baos.toByteArray()
            // Save the image in firebase
            val imageRef: StorageReference = storageReference.child("images")
                .child("profile")
//                .child(idUser)
                .child("${idUser}.jpeg")

            val uploadTask = imageRef.putBytes(imageData)
            uploadTask.addOnFailureListener{
                Toast.makeText(
                    applicationContext,
                    "Error to make upload of image",
                    Toast.LENGTH_LONG
                )
                    .show()
            }.addOnSuccessListener {
                Toast.makeText(
                    applicationContext,
                    "success to make upload of message",
                    Toast.LENGTH_LONG
                )
                    .show()
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

