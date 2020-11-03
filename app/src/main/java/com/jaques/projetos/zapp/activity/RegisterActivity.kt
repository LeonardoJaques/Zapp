package com.jaques.projetos.zapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.jaques.projetos.zapp.R
import com.jaques.projetos.zapp.model.User
import com.jaques.projetos.zapp.settings.SettingsFirebase



class RegisterActivity : AppCompatActivity() {

    private lateinit var fieldName: EditText
    private lateinit var fieldEmail: EditText
    private lateinit var fieldPassword: EditText
    private lateinit var buttonRegister: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var user: User


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
                textName.isEmpty() -> Toast.makeText(this, "Preencha o nome", Toast.LENGTH_SHORT).show()
                textEmail.isEmpty() -> Toast.makeText(this, "Preencha o email", Toast.LENGTH_SHORT).show()
                textPassword.isEmpty() -> Toast.makeText(this, "Preencha a senha", Toast.LENGTH_SHORT).show()
                else -> run {
                    user = User(textName, textEmail, textPassword)
                    registerUser(user)
                }
            }
        }
    }

    fun registerUser(user: User) {
        auth = SettingsFirebase.getFirebaseAuth()
        auth.createUserWithEmailAndPassword(user.email, user.password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Toast.makeText(this, "Sucesso ao cadastrar usuario", Toast.LENGTH_SHORT).show()
                finish()

            } else {
                var exception = ""
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
            }
        }
    }

}

