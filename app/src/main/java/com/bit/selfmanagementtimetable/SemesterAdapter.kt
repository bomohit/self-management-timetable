package com.bit.selfmanagementtimetable

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

class SemesterAdapter(private val semester: MutableList<Semester>) : RecyclerView.Adapter<SemesterAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) :  RecyclerView.ViewHolder(itemView){
        val semesterDate: TextView = itemView.findViewById(R.id.semesterDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.semester_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.semesterDate.text = semester[position].date
        holder.semesterDate.setOnClickListener {
            val userType = MainActivity().userType
            d("bomoh", "test: $userType")
            val bundle = bundleOf("id" to semester[position].date)
            it.findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
        }
    }

    override fun getItemCount() = semester.size

}
