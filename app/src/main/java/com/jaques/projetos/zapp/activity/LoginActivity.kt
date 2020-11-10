package com.jaques.projetos.zapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.*
import com.jaques.projetos.zapp.R
import com.jaques.projetos.zapp.model.User
import com.jaques.projetos.zapp.settings.SettingsFirebase


class LoginActivity : AppCompatActivity() {


    private lateinit var fieldEmail: EditText
    private lateinit var fieldPassword: EditText
    private lateinit var buttonEnterLogin: Button

    private val user = User()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        fieldEmail = findViewById(R.id.editEmailLogin)
        fieldPassword = findViewById(R.id.editPasswordLogin)
        buttonEnterLogin = findViewById(R.id.buttonEnterLoginView)

        buttonEnterLogin.setOnClickListener {
            val textEmail = fieldEmail.text.toString()
            val textPassword = fieldPassword.text.toString()

            //validate
            when {
                textEmail.isEmpty() -> Toast.makeText(this, "Preencha o email", Toast.LENGTH_SHORT)
                    .show()
                textPassword.isEmpty() -> Toast.makeText(
                    this,
                    "Preencha a senha",
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                    user.email = textEmail
                    user.password = textPassword
                    loginUser()
                }
            }
        }
    }

    private fun loginUser() {
        val auth: FirebaseAuth = SettingsFirebase.getFirebaseAuth()
        auth.signInWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    updateUI(user)
                } else {
                    val exception: String
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        exception = "Digite uma senha mais forte!"
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        exception = "Por favor, digite um e-mail válido"
                    } catch (e: FirebaseAuthUserCollisionException) {
                        exception = "Essa conta já foi cadastrada"
                    } catch (e: Exception) {
                        exception = "Erro ao cadastrar o usuário ${e.message}"
                        e.printStackTrace()
                    }
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, exception,
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                    finish()
                }
            }
    }

    fun openRegister(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun openMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun updateUI(user: User?) {
        Toast.makeText(this, "Usuario logado com sucesso", Toast.LENGTH_LONG).show()
        openMain()
//        Firebase.auth.signOut()

    }

    override fun onStart() {
        val currentUser = SettingsFirebase.getFirebaseAuth().currentUser
        Log.i("Rapadura", "rapaduraUser:${currentUser} ")
        if (currentUser != null) {
            updateUI(user)
        }
        super.onStart()
    }


}