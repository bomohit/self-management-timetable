package com.bit.selfmanagementtimetable

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

class ForthFragment : Fragment() {
    val db = Firebase.firestore
    val set = "selection"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_forth, container, false)
        val id = arguments?.getString("id")
        val user = arguments?.getString("user")

        val recyclerView = root.findViewById<RecyclerView>(R.id.SubjectSelectionRecyclerView)

        val subject = mutableListOf<Subjects>()
        fun rv() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@ForthFragment.context)
                adapter = SubjectAdapter(subject)
            }
        }

        db.collection("$user").document("$id").collection("subject")
            .get()
            .addOnSuccessListener {
                for (result in it) {
                    Log.d("bomoh", "id = ${result.id} data = ${result.data}")
                    val name = result.getField<String>("name").toString()
                    val lecturer = result.getField<String>("lecturer").toString()
                    val venue = result.getField<String>("venue").toString()
                    val start = result.getField<String>("start").toString()
                    val end = result.getField<String>("end").toString()
                    val id = result.id
                    val sem = arguments?.getString("id").toString()
                    subject.add(Subjects(name, lecturer, start, end, venue, id, set, sem))
                }
                rv()
            }

        return root
    }

}