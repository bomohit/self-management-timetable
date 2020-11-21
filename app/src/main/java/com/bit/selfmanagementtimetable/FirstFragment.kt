package com.bit.selfmanagementtimetable

import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    val db = Firebase.firestore

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_first, container, false)
        val user = (activity as MainActivity).user

        val recyclerView = root.findViewById<RecyclerView>(R.id.semesterRecyclerView)
        val semester = mutableListOf<Semester>()
        fun rv () {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@FirstFragment.context)
                adapter = SemesterAdapter(semester)
            }
        }

        db.collection(user)
            .get()
            .addOnSuccessListener {
                for (result in it) {
                    val doc = result.id
//                    d("bomoh", "doc $doc")
                    semester.add(Semester(doc))
                }
                rv()
            }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }
}

data class Semester (
        val date: String
)
