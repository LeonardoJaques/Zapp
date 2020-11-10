package com.jaques.projetos.zapp.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.*
import com.jaques.projetos.zapp.R
import com.jaques.projetos.zapp.helper.Base64Custom
import com.jaques.projetos.zapp.model.User
import com.jaques.projetos.zapp.settings.SettingsFirebase


class RegisterActivity : AppCompatActivity() {

    private lateinit var fieldName: EditText
    private lateinit var fieldEmail: EditText
    private lateinit var fieldPassword: EditText
    private lateinit var buttonRegister: Button

    private lateinit var auth: FirebaseAuth


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        fieldName = findViewById(R.id.editName)
        fieldEmail = findViewById(R.id.editEmail)
        fieldPassword = findViewById(R.id.editPassword)
        buttonRegister = findViewById(R.id.buttonRegisterView)


        buttonRegister.setOnClickListener {
            val textName = fieldName.text.toString()
            val textEmail = fieldEmail.text.toString()
            val textPassword = fieldPassword.text.toString()

            //validate
            when {
                textName.isEmpty() -> Toast.makeText(this, "Preencha o nome", Toast.LENGTH_SHORT)
                    .show()
                textEmail.isEmpty() -> Toast.makeText(this, "Preencha o email", Toast.LENGTH_SHORT)
                    .show()
                textPassword.isEmpty() -> Toast.makeText(
                    this,
                    "Preencha a senha",
                    Toast.LENGTH_SHORT
                ).show()
                else -> run {
                    val userId = Base64Custom.codeBase64(textEmail)
                    val user = User(
                        id = userId,
                        textName,
                        textEmail,
                        textPassword
                    )
                    registerUser(user)
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerUser(user: User) {
        auth = SettingsFirebase.getFirebaseAuth()
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    user.save(user)
                    Toast.makeText(this, "Sucesso ao cadastrar usuario", Toast.LENGTH_SHORT).show()
                    validateUser()
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

    private fun validateUser() {
        val user = auth.currentUser
        if (user != null) {
            updateUI(user)
        } else {
            updateUI(null)
            finish()
        }

    }

    private fun updateUI(user: FirebaseUser?) {
        Toast.makeText(this, "Usuario logado com sucesso", Toast.LENGTH_LONG).show()
        openMain()
//        Firebase.auth.signOut()

    }

    private fun openMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


}

