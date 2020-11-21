package com.bit.selfmanagementtimetable.Login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bit.selfmanagementtimetable.MainActivity
import com.bit.selfmanagementtimetable.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class InputStudentMatrixActivity : AppCompatActivity() {

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.input_student_activity)

        val inputId = findViewById<TextView>(R.id.inputStudentId)
        val button = findViewById<Button>(R.id.buttonSubmitStudent)
        val userType = intent.getStringExtra("userType").toString()

        button.setOnClickListener {
            val vi = it
            if (inputId.text.toString().isNotEmpty()) {
                db.collection("student")
                    .get()
                    .addOnSuccessListener {
                        var valid = false
                        for (result in it) {
                            val tid = result.getField<String>("id").toString()

                            if(tid == inputId.text.toString()) {
                                valid = true
                            }
                        }
                        if (valid) {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("user", inputId.text.toString())
                            intent.putExtra("userType", userType)
                            startActivity(intent)
                        } else {
                            Snackbar.make(vi, "STUDENT NOT REGISTERED", Snackbar.LENGTH_SHORT).show()
                        }

                    }
            }
        }
    }
}