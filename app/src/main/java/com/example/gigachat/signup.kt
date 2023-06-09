package com.example.gigachat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class signup : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtpasswd: EditText
    private lateinit var edtName: EditText
    private lateinit var btnsignup: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        edtEmail = findViewById(R.id.EmailET)
        edtpasswd = findViewById(R.id.PasswdET)
        edtName = findViewById(R.id.nameET)
        btnsignup = findViewById(R.id.signupBtn)

        btnsignup.setOnClickListener {
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtpasswd.text.toString()

            signUp(name, email, password)
        }

    }

    private fun signUp(name: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                    val intent = Intent(this@signup, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this@signup, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(name, email, uid))
    }
}