import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.medtracker.R
import kotlin.random.Random

class HeartRateIndicator:Fragment(R.layout.fragment_hr) {

    var hr = 90 // heart rate
    private val random = Random(2)

    lateinit var mainHandler: Handler

    private lateinit var progressText: TextView
    private lateinit var progressValue: ProgressBar

    private val updateHRTask = object: Runnable {
        override fun run() {
            changeHeartRate()
            mainHandler.postDelayed(this, 300)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressText = view.findViewById<TextView>(R.id.text_view_progress)
        progressValue = view.findViewById<ProgressBar>(R.id.progress_bar)

        mainHandler = Handler(Looper.getMainLooper())
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateHRTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateHRTask)
    }

    fun changeHeartRate() {
        if (random.nextInt(0,2) == 1)
            hr -= random.nextInt(1,10)
        else
            hr += random.nextInt(1,10)

        progressText.text = hr.toString()
        progressValue.progress = hr
    }
}
