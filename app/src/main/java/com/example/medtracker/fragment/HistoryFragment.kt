package com.example.medtracker.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medtracker.*
import com.example.medtracker.data.adapter.HeartRateAdapter
import com.example.medtracker.data.entity.HeartRate
import com.example.medtracker.data.viewmodel.HeartRateViewModel
import com.example.medtracker.data.viewmodel.HeartRateViewModelFactory

class HistoryFragment:Fragment(R.layout.fragment_history) {
    private val heartRateViewModel: HeartRateViewModel by viewModels {
        HeartRateViewModelFactory((requireActivity().application as MedTrackerApplication).heartRateRepository)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvHistory = view.findViewById<RecyclerView>(R.id.rvHistory)
        val adapter = HeartRateAdapter()
        rvHistory.adapter = adapter
        rvHistory.layoutManager = LinearLayoutManager(context)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        heartRateViewModel.allHeartRates.observe(viewLifecycleOwner, Observer { heartRates: List<HeartRate> ->
            // Update the cached copy of the heartRates in the adapter.
            heartRates?.let { adapter.submitList(it) }
        })
    }
}