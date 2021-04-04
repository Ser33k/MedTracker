package com.example.medtracker.data.adapter

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.medtracker.R
import com.example.medtracker.data.entity.HeartRate
import java.util.*

class HeartRateAdapter : ListAdapter<HeartRate, HeartRateAdapter.HeartRateViewHolder>(HeartRatesComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeartRateViewHolder {
        return HeartRateViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: HeartRateViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.date, current.value)
    }

    class HeartRateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.date)
        private val valueTextView: TextView = itemView.findViewById(R.id.hrValue)

        private val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        fun bind(date: Date?, value: Int?) {
            dateTextView.text = formatter.format(date)
            valueTextView.text = value.toString()
        }

        companion object {
            fun create(parent: ViewGroup): HeartRateViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_history, parent, false)
                return HeartRateViewHolder(view)
            }
        }
    }

    class HeartRatesComparator : DiffUtil.ItemCallback<HeartRate>() {
        override fun areItemsTheSame(oldItem: HeartRate, newItem: HeartRate): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: HeartRate, newItem: HeartRate): Boolean {
            return oldItem.date == newItem.date
        }
    }
}