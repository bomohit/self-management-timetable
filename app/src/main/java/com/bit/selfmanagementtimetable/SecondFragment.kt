package com.bit.selfmanagementtimetable

import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    val db = Firebase.firestore
    val set = "added"
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_second, container, false)
        val id = arguments?.getString("id")

        val recyclerView = root.findViewById<RecyclerView>(R.id.SubjectListRecyclerView)

        val subject = mutableListOf<Subjects>()
        fun rv() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@SecondFragment.context)
                adapter = SubjectAdapter(subject)
            }
        }

        val user = (activity as MainActivity).user

        if (user == "admin") {
            db.collection("$user").document("$id").collection("subject")
                .get()
                .addOnSuccessListener {
                    for (result in it) {
                        d("bomoh", "id = ${result.id} data = ${result.data}")
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
        } else {
            fun getData() {
                db.collection("$user").document("$id").collection("added")
                    .get()
                    .addOnSuccessListener {
                        for (result in it) {
                            d("bomoh", "id = ${result.id} data = ${result.data}")
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
            }

            val docRef = db.collection("$user").document("$id").collection("added")
            docRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    subject.clear()
                    getData()
                }
            }


        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getString("id")
        val user = (activity as MainActivity).user
        val userType = (activity as MainActivity).userType

        view.findViewById<Button>(R.id.ButtonAddSubject).setOnClickListener {

            // Send to Student User Fragment
            if (userType == "student") {
                val bundle = bundleOf("id" to id, "user" to user)
                findNavController().navigate(R.id.action_SecondFragment_to_forthFragment, bundle)
            } else {
                val bundle = bundleOf("id" to id, "user" to user)
                findNavController().navigate(R.id.action_SecondFragment_to_thirdFragment, bundle)
            }

        }
    }
}


