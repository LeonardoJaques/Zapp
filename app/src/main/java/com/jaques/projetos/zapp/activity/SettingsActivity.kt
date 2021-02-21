package com.jaques.projetos.zapp.activity


import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.storage.StorageReference
import com.jaques.projetos.zapp.R
import com.jaques.projetos.zapp.helper.Permission
import com.jaques.projetos.zapp.helper.UserFirebase
import com.jaques.projetos.zapp.helper.UserFirebase.Companion.updateUserPicture
import com.jaques.projetos.zapp.settings.SettingsFirebase
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream


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


	@RequiresApi(Build.VERSION_CODES.O)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_settings)

		iButtonCamera = findViewById(R.id.imageButtonCamera)
		iButtonGallery = findViewById(R.id.imageButtonGallery)
		circleImageView_imagePerfil = findViewById(R.id.circleImageView_image)

		idUser = UserFirebase.getIdUser()
		storageReference = SettingsFirebase.getFirebaseStorage()

		// Download directly from StorageReference using Glide

		// TODO: 01/02/21 find a away to know if this image is in the store and return a boolean
		// TODO: 09/02/21 I use the same design of course, but i don't like of result because
		//  its do not show me when url have a error. I need search a best away to do this.
		// TODO: 13/02/21 after search the error, i found thats firebase sell the service of bucket.


		val storageURL =  storageReference.child("images")
										  .child("profile")
			                              .child("${idUser}.jpeg")

		storageURL.downloadUrl.addOnSuccessListener{
			Glide.with(this)
				.load(it)
				.into(circleImageView_imagePerfil)
		}.addOnFailureListener {
		    circleImageView_imagePerfil.setImageResource(R.drawable.padrao)
		}










		Log.i("Rapadura-user", storageURL.toString())




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
				startActivityForResult(mediaIntent, this.SECTIONCAMERA)
			}

		}

		iButtonGallery.setOnClickListener {
			val mediaIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
			if (mediaIntent.resolveActivity(packageManager) != null) {
				startActivityForResult(mediaIntent, this.SECTIONGALLERY)
			}

		}

	}





	@RequiresApi(Build.VERSION_CODES.P)
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
			try {
				var imageBitmap: Bitmap? = null
			when (requestCode) {

				//get the image with camera
				SECTIONCAMERA -> {
					imageBitmap = data?.extras?.get("data") as Bitmap
					circleImageView_imagePerfil.setImageBitmap(imageBitmap)

				}
				//get the image in gallery
				SECTIONGALLERY -> {
					val imageUri = data?.data
					circleImageView_imagePerfil.setImageURI(imageUri)
				}
			}




				// Save the image in firebase
				val imageRef: StorageReference = storageReference.child("images").child("profile").child("${idUser}.jpeg")
				circleImageView_imagePerfil.isDrawingCacheEnabled = true
				circleImageView_imagePerfil.buildDrawingCache()
				val bitmap = (circleImageView_imagePerfil.drawable as BitmapDrawable).bitmap

				val baos = ByteArrayOutputStream()
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
				val imageData = baos.toByteArray()

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
						"Success to make upload of message",
						Toast.LENGTH_LONG
					)
						.show()


					Log.i("Rapadura-url", it.toString())
//						 updatePictureWithUrl(it.storage.downloadUrl.result)

				}



		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	@RequiresApi(Build.VERSION_CODES.O)
	private fun updatePictureWithUrl(url: Uri?) {
		updateUserPicture(url)
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






