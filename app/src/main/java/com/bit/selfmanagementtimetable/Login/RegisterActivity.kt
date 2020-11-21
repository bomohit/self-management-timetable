package com.bit.selfmanagementtimetable.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bit.selfmanagementtimetable.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_admin_activity)

        val logType = intent.getStringExtra("type")

        val logMatrix: TextView = findViewById(R.id.logMatrixId)
        val logPassword: TextView = findViewById(R.id.logPassword)
        val logButton: Button = findViewById(R.id.buttonSignIn)
        val logLogin: TextView = findViewById(R.id.button_register)
        val typeUser: TextView = findViewById(R.id.typeUser)

        if (logType == "admin") {
            typeUser.text = "ADMIN"
        } else {
            typeUser.text = "STUDENT"
        }

        logLogin.text = "Login"
        logButton.text = "REGISTER"

        fun login(type: String , view: View) {
            val data = hashMapOf(
                "id" to logMatrix.text.toString(),
                "password" to logPassword.text.toString()
            )
            fun register() {
                if (logMatrix.text.isNotEmpty() && logPassword.text.isNotEmpty()){
                    db.collection(type)
                        .add(data)
                        .addOnSuccessListener {
                            Snackbar.make(view, "Login Click", Snackbar.LENGTH_SHORT).show()
                        }
                } else {
                    Snackbar.make(view, "FILL UP THE FORM", Snackbar.LENGTH_SHORT).show()
                }
            }

            db.collection(type)
                .get()
                .addOnSuccessListener {
                    var valid = true
                    for (result in it) {
                        val tid = result.getField<String>("id").toString()
                        if (tid == logMatrix.text.toString()) {
                            valid = false
                            break
                        }
                    }
                    if (valid) {
                        register()
                    } else {
                        Snackbar.make(view, "ID ALREADY REGISTER", Snackbar.LENGTH_SHORT).show()
                    }
                }

            }

        logButton.setOnClickListener {
            Log.d("bomoh", "${logMatrix.text}, ${logPassword.text}")
            login(logType.toString(), it)
        }

        logLogin.setOnClickListener {
            Snackbar.make(it, "Login Click", Snackbar.LENGTH_SHORT).show()
            onBackPressed()
        }

    }
}