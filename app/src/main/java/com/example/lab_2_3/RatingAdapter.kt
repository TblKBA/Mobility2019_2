package com.example.lab_2_3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_row.view.*

interface ItemClick {
    fun onItemClick(pos: Int)
}

data class Ratingg(
    val login: String,
    val percent: String
)

class RatingAdapter(
    private val rating: ArrayList<Ratingg>,
    private val context: Context,
    val onItemClick: ItemClick
) : RecyclerView.Adapter<RatingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_row, parent, false), onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rlogin.text = rating[position].login
        holder.percent.text = rating[position].percent
    }

    override fun getItemCount(): Int {
        return rating.size
    }

    class ViewHolder(view: View, val onItemClick: ItemClick) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val rlogin = view.Login!!
        val percent = view.Percent!!

        init {
            rlogin.setOnClickListener(this)
            percent.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            onItemClick.onItemClick(adapterPosition)
        }

    }

}