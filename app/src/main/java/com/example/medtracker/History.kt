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
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvHistory = view.findViewById(R.id.rvHistory) as RecyclerView
        historyItems = HistoryData.createRandomDataList(15)
        val adapter = HistoryAdapter(historyItems)
        rvHistory.adapter = adapter
        rvHistory.layoutManager = LinearLayoutManager(context)
    }
}