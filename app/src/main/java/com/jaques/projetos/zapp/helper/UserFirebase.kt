package com.jaques.projetos.zapp.helper

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.jaques.projetos.zapp.settings.SettingsFirebase

/** author Leonardo Jaques on 28/01/21 */
class UserFirebase {

    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        fun getIdUser(): String{
            val user: FirebaseAuth = SettingsFirebase.getFirebaseAuth()
            val email = user.currentUser!!.email.toString()
            val idUser = Base64Custom.codeBase64(email)

            return idUser
        }
    }

}