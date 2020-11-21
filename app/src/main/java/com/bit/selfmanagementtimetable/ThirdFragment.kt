package com.bit.selfmanagementtimetable

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class ThirdFragment : Fragment(), View.OnClickListener {
    val db = Firebase.firestore
    val timeFormat: String = "hh:mm aa" // in 12 hour format // aa = am||pm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_third, container, false)
        val buttonSetTime: Button = root.findViewById(R.id.buttonSetTime)
        val buttonSetEndTime: Button = root.findViewById(R.id.buttonSetEndTime)
        val buttonSubmitSubject: Button = root.findViewById(R.id.buttonSubmitSubject)
        val id = arguments?.getString("id")
        val user = arguments?.getString("user")

        buttonSetTime.setOnClickListener(this)
        buttonSetEndTime.setOnClickListener(this)
        buttonSubmitSubject.setOnClickListener(this)

//        d("bomoh", "id: $id, user: $user")

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.buttonSetTime -> setTime(this)
            R.id.buttonSetEndTime -> setEndTime(this)
            R.id.buttonSubmitSubject -> submitPressed(this)
        }
    }

    private fun submitPressed(thirdFragment: ThirdFragment) {
        if(!valid()){
            return
        }
        val id = arguments?.getString("id")
        val user = arguments?.getString("user")
        val addSubjectName = view?.findViewById<TextView>(R.id.addSubjectName)!!.text.toString()
        val addLecturerName = view?.findViewById<TextView>(R.id.addLecturerName)!!.text.toString()
        val addSubjectVenue = view?.findViewById<TextView>(R.id.addSubjectVenue)!!.text.toString()
        val buttonSetTime = view?.findViewById<Button>(R.id.buttonSetTime)!!.text.toString()
        val buttonEndTime = view?.findViewById<Button>(R.id.buttonSetEndTime)!!.text.toString()


        val data = hashMapOf(
            "name" to addSubjectName,
            "lecturer" to addLecturerName,
            "venue" to addSubjectVenue,
            "start" to buttonSetTime,
            "end" to buttonEndTime
        )

        db.collection("$user").document("$id").collection("subject")
            .add(data)
            .addOnSuccessListener {
                val mySnackbar = Snackbar.make(requireView(), "SUBJECT ADDED", Snackbar.LENGTH_SHORT)
                mySnackbar.show()
            }


    }
    // check if the form is filled or not
    private fun valid(): Boolean {
        var valid = true

        val addSubjectName = view?.findViewById<TextView>(R.id.addSubjectName)
        val addLecturerName = view?.findViewById<TextView>(R.id.addLecturerName)
        val addSubjectVenue = view?.findViewById<TextView>(R.id.addSubjectVenue)
        val buttonSetTime = view?.findViewById<Button>(R.id.buttonSetTime)
        val buttonEndTime = view?.findViewById<Button>(R.id.buttonSetEndTime)
        val startHeader = view?.findViewById<TextView>(R.id.setTimeHeader)
        val endHeader = view?.findViewById<TextView>(R.id.setEndTimeHeader)

        if (addSubjectName!!.text.isNullOrEmpty()){
            addSubjectName.error = "Required"
            valid = false
        } else {
            addSubjectName.error = null
        }

        if (addLecturerName!!.text.isNullOrEmpty()) {
            addLecturerName.error = "Required"
            valid = false
        } else {
            addLecturerName.error = null
        }

        if (addSubjectVenue!!.text.isNullOrEmpty()) {
            addSubjectVenue.error = "Required"
            valid = false
        } else {
            addSubjectVenue.error = null
        }

        if (buttonSetTime!!.text == "_____") {
            startHeader!!.error = "Required"
            valid = false
        } else {
            startHeader!!.error = null
        }

        if (buttonEndTime!!.text == "_____") {
            endHeader!!.error = "Required"
            valid = false
        } else {
            endHeader!!.error = null
        }

        return valid
    }

    private fun setEndTime(thirdFragment: ThirdFragment) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            val time = SimpleDateFormat(timeFormat).format(cal.time)
            view?.findViewById<Button>(R.id.buttonSetEndTime)!!.text = time
        }
        TimePickerDialog(thirdFragment.context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
    }

    private fun setTime(thirdFragment: ThirdFragment) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            val time = SimpleDateFormat(timeFormat).format(cal.time)
            view?.findViewById<Button>(R.id.buttonSetTime)!!.text = time
        }
        TimePickerDialog(thirdFragment.context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
    }


}