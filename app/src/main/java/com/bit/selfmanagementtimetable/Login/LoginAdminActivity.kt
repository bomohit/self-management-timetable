package com.bit.selfmanagementtimetable.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bit.selfmanagementtimetable.MainActivity
import com.bit.selfmanagementtimetable.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class LoginAdminActivity : AppCompatActivity() {
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_admin_activity)

        val logType = intent.getStringExtra("logType").toString()

        val logMatrix: TextView = findViewById(R.id.logMatrixId)
        val logPassword: TextView = findViewById(R.id.logPassword)
        val logButton: Button = findViewById(R.id.buttonSignIn)
        val logRegister: TextView = findViewById(R.id.button_register)
        val typeUser: TextView = findViewById(R.id.typeUser)

        if (logType == "admin") {
            typeUser.text = "ADMIN"
        } else {
            typeUser.text = "STUDENT"
        }

        fun login(type: String) {
            db.collection(type)
                .get()
                .addOnSuccessListener {
                    for (result in it) {
                        val tid = result.getField<String>("id").toString()
                        val tpass = result.getField<String>("password").toString()
                        d("bomoh", "$tid, $tpass")
                        if (logMatrix.text.toString() == tid && logPassword.text.toString() == tpass) {
                            d("bomoh", "login success : $type")

                            // if admin pass to admin role
                            // if student pass student role
                            if (logType == "student") {
                                val intent = Intent(this, MainActivity::class.java)
                                intent.putExtra("user", logMatrix.text.toString())
                                intent.putExtra("userType", logType)
                                startActivity(intent)
                            } else {
                                val intent = Intent(this, InputStudentMatrixActivity::class.java)
                                intent.putExtra("userType", logType)
                                startActivity(intent)
                            }

                        }
                    }
                }
        }

        logButton.setOnClickListener {
            d("bomoh", "${logMatrix.text}, ${logPassword.text}")
            login(logType)
        }

        logRegister.setOnClickListener {
            Snackbar.make(it, "Register Click", Snackbar.LENGTH_SHORT).show()
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("type", logType)
            startActivity(intent)

        }

    }
}