import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medtracker.*

class History:Fragment(R.layout.fragment_history) {
    lateinit var historyItems: ArrayList<HistoryData>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val rvHistory = view.findViewById(R.id.rvHistory) as RecyclerView
        // Initialize contacts
        historyItems = HistoryData.createRandomDataList(5)
        // Create adapter passing in the sample user data
        val adapter = HistoryAdapter(historyItems)
        // Attach the adapter to the recyclerview to populate items
        rvHistory.adapter = adapter
        // Set layout manager to position the items
        rvHistory.layoutManager = LinearLayoutManager(context)
        // That's all!


    }

//    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(itemView, savedInstanceState)
//                recyclerView = itemView.findViewById(R.id.recycler_view)
//        recyclerView.apply {
//            // set a LinearLayoutManager to handle Android
//            // RecyclerView behavior
//            layoutManager = LinearLayoutManager(activity)
//            // set the custom adapter to the RecyclerView
//            val list = arrayOf("dsad", "dsadsa", "dsads");
//            adapter = HistoryAdapter(list)
//        }
//    }
}