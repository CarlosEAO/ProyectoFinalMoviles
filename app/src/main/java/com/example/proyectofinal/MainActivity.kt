package com.example.proyectofinal

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        /*val  signupButton: Button = findViewById(R.id.signupButton)

        signupButton.setOnClickListener{
            var email: String = findViewById<EditText>(R.id.emailSignupEditText).text.toString()
            var password: String = findViewById<EditText>(R.id.passwordSignupEditText).text.toString()
            Toast.makeText(this, email + " " + password, Toast.LENGTH_SHORT).show()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        //val user = auth.currentUser
                        Toast.makeText(baseContext, "user created.",
                            Toast.LENGTH_SHORT).show()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }

                    // [START_EXCLUDE]
                    // [END_EXCLUDE]
                }
        }*/

    }

}

