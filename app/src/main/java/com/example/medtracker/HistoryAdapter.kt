package com.example.medtracker

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(private val dataSet: List<HistoryData>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {

        val dateTextView = itemView.findViewById<TextView>(R.id.date)
        val minTextView = itemView.findViewById<TextView>(R.id.minHr)
        val maxTextView = itemView.findViewById<TextView>(R.id.maxHr)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val historyView = inflater.inflate(R.layout.item_history, parent, false)
        return ViewHolder(historyView)
    }


    override fun onBindViewHolder(viewHolder: HistoryAdapter.ViewHolder, position: Int) {
        val dataItem: HistoryData = dataSet.get(position)

        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        val dateTextView = viewHolder.dateTextView
        dateTextView.text = formatter.format(dataItem.date)

        val minTv = viewHolder.minTextView
        minTv.text = dataItem.minHr.toString()

        val maxTv = viewHolder.maxTextView
        maxTv.text = dataItem.maxHr.toString()

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}