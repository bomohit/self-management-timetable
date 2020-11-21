package com.bit.selfmanagementtimetable

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import java.time.LocalTime

class SubjectAdapter(private val subject: MutableList<Subjects>) : RecyclerView.Adapter<SubjectAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val subjectName: TextView = itemView.findViewById(R.id.subjectName)
        val subjectLecturer: TextView = itemView.findViewById(R.id.subjectLecturer)
        val subjectStart: TextView = itemView.findViewById(R.id.subjectStart)
        val subjectEnd: TextView = itemView.findViewById(R.id.subjectEnd)
        val subjectVenue: TextView = itemView.findViewById(R.id.subjectVenue)
        val buttonAdd: Button = itemView.findViewById(R.id.buttonAdd)
        val buttonRemove: Button = itemView.findViewById(R.id.buttonRemove)

        val progressBar : ProgressBar = itemView.findViewById(R.id.progressBarAddSubject)
        val db = Firebase.firestore

        val userType = MainActivity().userType
        val user = MainActivity().user
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.subject_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subjects = subject[position]
        holder.subjectName.text = subjects.name
        holder.subjectLecturer.text = subjects.lecturer
        holder.subjectStart.text = subjects.start
        holder.subjectEnd.text = subjects.end
        holder.subjectVenue.text = subjects.venue

        // display button add only on selection subject
        holder.buttonAdd.isVisible = subjects.set != "added"
        holder.buttonRemove.isVisible = subjects.set != "selection"

        holder.buttonRemove.setOnClickListener {
            holder.progressBar.isVisible = true
            Snackbar.make(holder.itemView, "SUBJECT DELETED", Snackbar.LENGTH_SHORT).show()
            holder.db.collection(holder.user).document(subjects.sem).collection("added").document(subjects.id)
                .delete()
                .addOnSuccessListener {
                    holder.progressBar.isVisible = false
                }

        }

        holder.buttonAdd.setOnClickListener {
            holder.progressBar.isVisible = true
            val data = hashMapOf(
                "end" to subjects.end,
                "start" to subjects.start,
                "name" to subjects.name,
                "venue" to subjects.venue,
                "lecturer" to subjects.lecturer
            )

            fun addSubject(){
                holder.db.collection(holder.user).document(subjects.sem).collection("added").document(subjects.id)
                    .set(data)
                    .addOnSuccessListener {
                        holder.progressBar.isVisible = false
                        val mySnackbar = Snackbar.make(holder.itemView, "SUBJECT ADDED", Snackbar.LENGTH_SHORT)
                        mySnackbar.show()
                        holder.itemView.findNavController().popBackStack()
                    }
            }

            //check clashing
            d("bomoh", "c: ${holder.user} , d: ${subjects.sem}")
            holder.db.collection(holder.user).document(subjects.sem).collection("added")
                .get()
                .addOnSuccessListener {
                    var valid = true
                    for (result in it) {
                        val existStart = LocalTime.parse(result.getField<String>("start").toString())
                        val existEnd = LocalTime.parse(result.getField<String>("end").toString())
                        val selectStart = LocalTime.parse(subjects.start)
                        val selectEnd = LocalTime.parse(subjects.end)

                        if (selectStart.isAfter(existStart)) {
                            if (selectStart.isBefore(existEnd)) {
                                d("bomoh", "clashing")
                                valid = false
                            } else {
                                d("bomoh", "not clashing")
                            }
                        }

                        if (selectStart == existStart) {
                            d("bomoh", "clashing")
                            valid = false
                        }

                    }
                    if (valid) {
                        d("bomoh", "can add")
                        addSubject()
                    } else {
                        d("bomoh", "can't add")
                        holder.progressBar.isVisible = false
                        val mySnackbar = Snackbar.make(holder.itemView, "SUBJECT CLASHED", Snackbar.LENGTH_SHORT)
                        mySnackbar.show()
                        val bundle = bundleOf("id" to subjects.sem, "user" to holder.user)
//                        holder.itemView.findNavController().navigate(R.id.action_ForthFragment_to_SecondFragment, bundle)
                        holder.itemView.findNavController().popBackStack()
                    }
                }
        }

    }

    override fun getItemCount() = subject.size

}
