package com.jaques.projetos.zapp.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.database.Exclude
import com.jaques.projetos.zapp.settings.SettingsFirebase

/** author Leonardo Jaques on 02/11/20 */

data class User(
    @get:Exclude var id: String = "",
    var name: String = "",
    var email: String = "",
    @get:Exclude var password: String = ""
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun save(user: User) {
        val firebaseRef = SettingsFirebase.getFirebaseDatabase()
        val users = firebaseRef.child("users").child(user.id)

        users.setValue(user)
    }
}