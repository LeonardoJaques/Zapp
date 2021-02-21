package com.jaques.projetos.zapp.helper

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.jaques.projetos.zapp.settings.SettingsFirebase

/** author Leonardo Jaques on 28/01/21 */
class UserFirebase {
	companion object {
		internal val user: FirebaseAuth = SettingsFirebase.getFirebaseAuth()

		@RequiresApi(Build.VERSION_CODES.O)
		fun getIdUser(): String {

			val email = user.currentUser!!.email.toString()
			val idUser = Base64Custom.codeBase64(email)

			return idUser
		}

		fun getCurrentUser(): FirebaseUser = user.currentUser!!


		fun updateUserName(name: String): Boolean {
			return try {
				val user: FirebaseUser = getCurrentUser()
				val profile = UserProfileChangeRequest.Builder()
					.setDisplayName(name)
					.build()
				user.updateProfile(profile).isSuccessful
			}catch (e: Exception) {
				Log.d("Profile","Error in update name of profile")
				e.stackTrace
				false
			}
		}


		fun updateUserPicture(url: Uri?): Boolean {
			return try {
				val user: FirebaseUser = getCurrentUser()
				val profile = UserProfileChangeRequest.Builder().setPhotoUri(url).build()
				user.updateProfile(profile).isSuccessful
			}catch (e: Exception) {
				Log.d("Profile","Error in update picture of profile")
				e.stackTrace
				false
			}
		}

	}

}