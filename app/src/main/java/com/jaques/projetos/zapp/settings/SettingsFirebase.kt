package com.jaques.projetos.zapp.settings

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


/** author Leonardo Jaques on 02/11/20 */

class SettingsFirebase {

  companion object {
      private val auth: FirebaseAuth = FirebaseAuth.getInstance()
      private val storage: StorageReference = FirebaseStorage.getInstance().reference
      private val database: DatabaseReference = Firebase.database.reference


      //return firebaseAuth instance
      fun getFirebaseAuth() = auth

      //return firebaseDatabase instance
      fun getFirebaseDatabase() = database

      //inserting image in firebase
      fun getFirebaseStorage() = storage
  }

}







