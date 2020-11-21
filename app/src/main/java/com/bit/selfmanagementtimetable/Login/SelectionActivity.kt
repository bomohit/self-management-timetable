package com.bit.selfmanagementtimetable.Login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.bit.selfmanagementtimetable.R

class SelectionActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selection_activty)

        val student = findViewById<Button>(R.id.buttonSelectStudent)
        val admin = findViewById<Button>(R.id.buttonSelectAdmin)

        student.setOnClickListener(this)
        admin.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.buttonSelectStudent -> openSelection("student")
            R.id.buttonSelectAdmin -> openSelection("admin")
        }
    }

    private fun openSelection(s: String) {
        val intent = Intent(this, LoginAdminActivity::class.java)
        intent.putExtra("logType", s)
        startActivity(intent)
    }
}