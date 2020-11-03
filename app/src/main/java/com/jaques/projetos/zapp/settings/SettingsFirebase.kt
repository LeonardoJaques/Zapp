package com.jaques.projetos.zapp.settings

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


/** author Leonardo Jaques on 02/11/20 */

class SettingsFirebase {

  companion object {
      private val auth: FirebaseAuth = FirebaseAuth.getInstance()
      private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

      //return firebaseAuth instance
      fun getFirebaseAuth() = auth

      //return firebaseDatabase instance
      fun getFirebaseDatabase() = database
  }

}







