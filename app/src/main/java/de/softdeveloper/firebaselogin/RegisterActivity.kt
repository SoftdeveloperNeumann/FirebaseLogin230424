package de.softdeveloper.firebaselogin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.softdeveloper.firebaselogin.databinding.ActivityLoginBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.text= "Registrieren"
        binding.tvRegister.visibility = View.INVISIBLE

        auth = Firebase.auth
        binding.btnLogin.setOnClickListener {
            val uncheckedEmail = binding.etEmail.text.toString().trim()
            val uncheckedPwd = binding.etPassword.text.toString()
            var email:String? = null
            var pwd:String? = null

            if(uncheckedEmail.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(
                    uncheckedEmail
            ).matches()){
                email = uncheckedEmail
            }else{
                binding.etEmail.error = "Bitte eine gültige email eingeben"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }

            if(uncheckedPwd.isNotEmpty() && uncheckedPwd.length >=8){
                pwd = uncheckedPwd
            }else{
                binding.etPassword.error = "Password muss wenigstens 8 Zeichen haben"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    user = auth.currentUser
                    Toast.makeText(this, "Sie haben sich registriert, bitte per Mail bestätigen", Toast.LENGTH_SHORT).show()
                    sendMail()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this, "Registrierung fehlgeschlagen, bitte erneut versuchen", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun sendMail() {
        if(user != null){
            user!!.sendEmailVerification().addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "Bestätigungsmail versendet", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Mailversand fehlgeschlagen", Toast.LENGTH_SHORT).show()
                }
                auth.signOut()
            }
        }
    }
}