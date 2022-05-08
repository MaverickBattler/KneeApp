package ru.leti.kneeapp.activity

import android.app.TaskStackBuilder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import ru.leti.kneeapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.leti.kneeapp.dto.TrainingRequestDto
import ru.leti.kneeapp.network.NetworkModule
import ru.leti.kneeapp.util.SharedPreferencesProvider

class ActivityOksReport : AppCompatActivity() {

    private val resultApiService = NetworkModule.resultApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oks_report)

        val buttonBack: Button = findViewById(R.id.buttonOksReportBack)
        buttonBack.setOnClickListener {
            finish()
        }
        val extras = intent.extras
        val oksResultSum: Int = extras?.getInt("oks_result_sum") ?: 0
        val lastOksTime: Long = extras?.getLong("last_oks_time") ?: 0
        val progressBar: ProgressBar = findViewById(R.id.progressBarOksReport)
        val sharedPreferencesProvider = SharedPreferencesProvider(applicationContext)
        val sharedPreferences = sharedPreferencesProvider.getEncryptedSharedPreferences()
        val email = sharedPreferences.getString("email", null)
        val authToken = sharedPreferences.getString("auth_token", null)

        val table: TableLayout = findViewById(R.id.table_layout_report)
        if (authToken != null && email != null) {
            val trainingRequestDto = TrainingRequestDto(
                email,
                lastOksTime
            )
            val authHeader = "Bearer_$authToken"
            resultApiService.getTrainings(
                authHeader,
                trainingRequestDto
            ).enqueue(object : Callback<List<List<Boolean>>> {
                override fun onResponse(
                    call: Call<List<List<Boolean>>>,
                    response: Response<List<List<Boolean>>>
                ) {
                    if (response.code() == 403 || response.code() == 500) {
                        openLoginActivity() //Ошибка аутентификации
                    } else {//Получен ответ
                        val trainings: List<List<Boolean>>? = response.body()
                        if (trainings != null && trainings.isNotEmpty()) {
                            //Показать результат тренировок, если они были
                            for (exercise in 0..12) {
                                val row = TableRow(applicationContext) //i-е упражнение во все дни
                                val viewFirstColumn = TextView(applicationContext)
                                viewFirstColumn.setTextColor(
                                    ContextCompat.getColor(
                                        applicationContext,
                                        R.color.black
                                    )
                                )
                                viewFirstColumn.text = getString(R.string.exercise_n, exercise + 1)
                                row.addView(viewFirstColumn)
                                for (training in 0..6) {
                                    val view = ImageView(applicationContext)
                                    view.textAlignment = View.TEXT_ALIGNMENT_CENTER
                                    if (trainings[training][exercise]) {
                                        view.setImageResource(R.drawable.plus16)
                                    } else {
                                        view.setImageResource(R.drawable.x16)
                                    }
                                    row.addView(view)
                                }
                                table.addView(row)
                            }
                        } else { //Не показывать результат о тренировках, так как их нет
                            val textViewToHide: TextView =
                                findViewById(R.id.textViewTrainingResultsTitle)
                            textViewToHide.visibility = View.GONE
                            table.visibility = View.GONE
                        }
                        Log.i("Success", response.toString())
                        progressBar.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<List<List<Boolean>>>, t: Throwable) {
                    val textViewToHide: TextView = findViewById(R.id.textViewTrainingResultsTitle)
                    textViewToHide.visibility = View.GONE
                    table.visibility = View.GONE
                    Log.i("Failure", t.message ?: "Null message")
                    showPopupErrorMessage(getString(R.string.server_not_responding))
                    progressBar.visibility = View.GONE
                }
            })
            val textViewResult: TextView = findViewById(R.id.textViewOksReportResult)
            textViewResult.text = getString(R.string.result_text, oksResultSum)
            val textViewPrevResult: TextView = findViewById(R.id.textViewOksReportPrevResult)
            val textViewPreviousResultStart: TextView =
                findViewById(R.id.textViewPreviousResultStart)
            val textViewOksReportExplanation: TextView =
                findViewById(R.id.textViewOksReportExplanation)
            val previousResult: Int = sharedPreferences.getInt("last_oks_result", -1)
            if (previousResult != -1) {
                textViewPrevResult.text = getString(R.string.result_text, previousResult)
            } else {
                textViewPreviousResultStart.textSize = 14F
                textViewPreviousResultStart.text =
                    getString(R.string.next_week_compare_your_result_to_previous)
                textViewPrevResult.visibility = View.GONE
            }
            when (oksResultSum) {
                in 0..19 -> {
                    textViewOksReportExplanation.text = getString(R.string.worst_outcome)
                }
                in 20..29 -> {
                    textViewOksReportExplanation.text = getString(R.string.bad_outcome)
                }
                in 30..39 -> {
                    textViewOksReportExplanation.text = getString(R.string.good_outcome)
                }
                in 40..47 -> {
                    textViewOksReportExplanation.text = getString(R.string.very_good_outcome)
                }
                48 -> {
                    textViewOksReportExplanation.text = getString(R.string.best_outcome)
                }
            }

            val editor = sharedPreferences.edit()
            editor.putInt("last_oks_result", oksResultSum)
            editor.apply()

        } else {
            openLoginActivity()
        }
        progressBar.visibility = View.VISIBLE

    }

    private fun openLoginActivity() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        TaskStackBuilder.create(applicationContext).addNextIntentWithParentStack(loginIntent)
            .startActivities()
    }

    private fun showPopupErrorMessage(errorText: String) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, errorText, duration)
        toast.show()
    }
}