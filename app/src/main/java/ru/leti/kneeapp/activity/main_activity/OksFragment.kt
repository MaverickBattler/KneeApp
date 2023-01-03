package ru.leti.kneeapp.activity.main_activity

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.leti.kneeapp.R
import ru.leti.kneeapp.TimeConstants
import ru.leti.kneeapp.util.SharedPreferencesProvider
import ru.leti.kneeapp.activity.TestPageActivity
import ru.leti.kneeapp.databinding.FragmentOksBinding
import java.util.*

class OksFragment : Fragment() {

    private var _binding: FragmentOksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var timer: CountDownTimer

    private var isRunning: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val startOksButton: Button = binding.runOksButton

        configureTestAvailability()

        startOksButton.setOnClickListener {
            openOksTestActivity()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        if (!isRunning) //когда вернулись в OksFragment из теста
            configureTestAvailability()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (::timer.isInitialized) {
            timer.cancel()
            isRunning = false
        }
    }

    private fun configureTestAvailability() {
        val textOnTimerTextView: TextView = binding.textOnTimerToNextOksTextview
        val timerTextView: TextView = binding.timerToNextOksTextview
        val startOksButton: Button = binding.runOksButton

        val sharedPreferencesProvider = SharedPreferencesProvider(timerTextView.context)
        val sharedPreferences = sharedPreferencesProvider.getEncryptedSharedPreferences()

        val lastTestTime: Long = sharedPreferences.getLong("last_oks", 0L)
        println(lastTestTime)
        val timeRemaining: Long = //между тестами должна пройти неделя
            TimeConstants.WEEK_IN_MILLIS - (Calendar.getInstance().timeInMillis - lastTestTime)
        if (lastTestTime != 0L && timeRemaining > 0) {
            startOksButton.setBackgroundColor(
                ContextCompat.getColor(
                    timerTextView.context,
                    R.color.light_gray
                )
            )
            startOksButton.isEnabled = false
            textOnTimerTextView.visibility = View.VISIBLE
            timerTextView.visibility = View.VISIBLE
            timer = object : CountDownTimer(timeRemaining, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    isRunning = true
                    val days = millisUntilFinished / TimeConstants.DAY_IN_MILLIS
                    val hours = (millisUntilFinished / TimeConstants.HOUR_IN_MILLIS) % 24
                    val mins = (millisUntilFinished / TimeConstants.MINUTE_IN_MILLIS) % 60
                    val secs = (millisUntilFinished / TimeConstants.SECOND_IN_MILLIS) % 60
                    when {
                        days > 0 ->
                            timerTextView.text =
                                getString(R.string.timer_days, days, hours, mins, secs)
                        hours > 0 ->
                            timerTextView.text = getString(R.string.timer_hours, hours, mins, secs)
                        mins > 0 ->
                            timerTextView.text = getString(R.string.timer_mins, mins, secs)
                        else ->
                            timerTextView.text = getString(R.string.timer_seconds, secs)
                    }
                }

                override fun onFinish() { //сделать кнопку доступной
                    isRunning = false
                    textOnTimerTextView.visibility = View.GONE
                    timerTextView.visibility = View.GONE
                    startOksButton.isEnabled = true
                    startOksButton.setBackgroundColor(
                        ContextCompat.getColor(
                            startOksButton.context,
                            R.color.brown
                        )
                    )
                    val editor = sharedPreferences.edit()
                    editor.putString(
                        "exercise1_1_pass_mark",
                        getString(R.string.not_allowed_to)
                    )
                    editor.putString(
                        "exercise1_2_pass_mark",
                        getString(R.string.not_allowed_to)
                    )
                    editor.putString(
                        "exercise1_3_pass_mark",
                        getString(R.string.not_allowed_to)
                    )
                    editor.putString(
                        "exercise2_1_pass_mark",
                        getString(R.string.not_allowed_to)
                    )
                    editor.putString(
                        "exercise3_1_pass_mark",
                        getString(R.string.not_allowed_to)
                    )
                    editor.putString(
                        "exercise3_2_pass_mark",
                        getString(R.string.not_allowed_to)
                    )
                    editor.putString(
                        "exercise3_3_pass_mark",
                        getString(R.string.not_allowed_to)
                    )
                    editor.putString(
                        "exercise4_1_pass_mark",
                        getString(R.string.not_allowed_to)
                    )
                    editor.putString(
                        "exercise4_2_1_pass_mark",
                        getString(R.string.not_allowed_to)
                    )
                    editor.putString(
                        "exercise4_2_2_pass_mark",
                        getString(R.string.not_allowed_to)
                    )
                    editor.putString(
                        "exercise4_2_3_pass_mark",
                        getString(R.string.not_allowed_to)
                    )
                    editor.putString(
                        "exercise4_3_1_pass_mark",
                        getString(R.string.not_allowed_to)
                    )
                    editor.putString(
                        "exercise4_3_2_pass_mark",
                        getString(R.string.not_allowed_to)
                    )
                    editor.apply()
                }
            }
            timer.start()
        } else { //сделать кнопку доступной
            textOnTimerTextView.visibility = View.GONE
            timerTextView.visibility = View.GONE
            startOksButton.isEnabled = true
            startOksButton.setBackgroundColor(
                ContextCompat.getColor(
                    timerTextView.context,
                    R.color.brown
                )
            )
        }
    }

    private fun openOksTestActivity() {
        val intent = Intent(activity, TestPageActivity::class.java)
        startActivity(intent)
    }
}