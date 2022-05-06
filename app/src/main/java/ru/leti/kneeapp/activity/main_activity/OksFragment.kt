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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val startOksButton: Button = binding.buttonRunOKS

        configureTestAvailability()

        startOksButton.setOnClickListener {
            openOksTestActivity()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        if (!::timer.isInitialized) //когда вернулись в OksFragment из теста
            configureTestAvailability()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        if (::timer.isInitialized)
            timer.cancel()
    }

    private fun configureTestAvailability() {
        val textOnTimerTextView: TextView = binding.textOnTimerToNextOks
        val timerTextView: TextView = binding.timerToNextOks
        val startOksButton: Button = binding.buttonRunOKS

        val sharedPreferencesProvider = SharedPreferencesProvider(timerTextView.context)
        val sharedPreferences = sharedPreferencesProvider.getEncryptedSharedPreferences()

        val lastTestTime: Long = sharedPreferences.getLong("last_oks", 0L)
        val timeRemaining: Long = //между тестами должна пройти неделя
            604800000 - (Calendar.getInstance().timeInMillis - lastTestTime)
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
                    val days = millisUntilFinished / 86400000
                    val hours = (millisUntilFinished / 3600000) % 24
                    val mins = (millisUntilFinished / 60000) % 60
                    val secs = (millisUntilFinished / 1000) % 60
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
                    textOnTimerTextView.visibility = View.INVISIBLE
                    timerTextView.visibility = View.INVISIBLE
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
            textOnTimerTextView.visibility = View.INVISIBLE
            timerTextView.visibility = View.INVISIBLE
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