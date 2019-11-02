package com.example.lab_2_3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_detalrow.view.*

data class Answerss(
    val ques: String,
    val answ: String
)

class DetalRatingAdapter(
    private val answers: ArrayList<Answerss>,
    private val context: Context,
    val onItemClick: ItemClick
) : RecyclerView.Adapter<DetalRatingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_detalrow, parent, false), onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rques.text = answers[position].ques
        holder.ans.text = answers[position].answ
    }

    override fun getItemCount(): Int {
        return answers.size
    }

    class ViewHolder(view: View, val onItemClick: ItemClick) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val rques = view.Ques!!
        val ans = view.Ans!!

        init {
            rques.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            onItemClick.onItemClick(adapterPosition)
        }

    }

}