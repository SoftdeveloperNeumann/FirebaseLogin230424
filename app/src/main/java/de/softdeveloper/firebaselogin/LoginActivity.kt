package de.softdeveloper.firebaselogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.softdeveloper.firebaselogin.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        auth = Firebase.auth
        binding.btnLogin.setOnClickListener {
            val uncheckedEmail = binding.etEmail.text.toString().trim()
            val uncheckedPwd = binding.etPassword.text.toString()
            var email: String? = null
            var pwd: String? = null

            if (uncheckedEmail.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(
                    uncheckedEmail
                ).matches()
            ) {
                email = uncheckedEmail
            } else {
                binding.etEmail.error = "Bitte eine gültige email eingeben"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }

            if (uncheckedPwd.isNotEmpty() && uncheckedPwd.length >= 8) {
                pwd = uncheckedPwd
            } else {
                binding.etPassword.error = "Password muss wenigstens 8 Zeichen haben"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            user = auth.currentUser
            if (user == null){
                auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        if (auth.currentUser!!.isEmailVerified){
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }else{
                            Toast.makeText(this, "Bitte ihre email bestätigen", Toast.LENGTH_SHORT).show()
                            auth.signOut()
                        }
                    }else{
                        Toast.makeText(this, "Anmeldung fehlgeschlagen, bitte erneut versuchen", Toast.LENGTH_SHORT).show()

                    }
                }
            }else{
                if (user!!.isEmailVerified){
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this, "Bitte email bestätigen", Toast.LENGTH_SHORT).show()
                    auth.signOut()
                }
            }

        }
    }
}