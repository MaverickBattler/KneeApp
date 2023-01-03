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

//Activity, соответствующее окну приложения с отчетом после заполнения анкеты
class ActivityOksReport : AppCompatActivity() {
    // подключение ResultsApiService
    private val resultsApiService = NetworkModule.resultsApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oks_report)
        //Закрытие окна по нажатию кнопки (и возвращение к OksFragment)
        val buttonBack: Button = findViewById(R.id.oks_report_back_button)
        buttonBack.setOnClickListener {
            finish()
        }
        //Получение intent.extras
        val extras = intent.extras
        //Получение текущего результата OKS из intent.extras
        val oksResultSum: Int = extras?.getInt("oks_result_sum") ?: 0
        //Получение времени последнего заполнения OKS из intent.extras
        val lastOksTime: Long = extras?.getLong("last_oks_time") ?: 0
        val progressBar: ProgressBar = findViewById(R.id.oks_report_progressbar)
        //Получение экземпляра EncryptedSharedPreferences
        val sharedPreferencesProvider = SharedPreferencesProvider(applicationContext)
        val sharedPreferences = sharedPreferencesProvider.getEncryptedSharedPreferences()
        //Получение электронной почты и токена аутентификации
        val email = sharedPreferences.getString("email", null)
        val authToken = sharedPreferences.getString("auth_token", null)
        val table: TableLayout = findViewById(R.id.table_layout_activity_oks_report)

        if (authToken != null && email != null) {
            // Создание TrainingRequestDto
            val trainingRequestDto = TrainingRequestDto(
                email,
                lastOksTime
            )
            // Составление заголовка аутентификации
            val authHeader = "Bearer_$authToken"
            resultsApiService.getTrainings(
                authHeader,
                trainingRequestDto
            ).enqueue(object : Callback<List<List<Boolean>>> { // Выполнение запроса к серверу
                override fun onResponse(
                    call: Call<List<List<Boolean>>>,
                    response: Response<List<List<Boolean>>>
                ) {
                    if (response.code() == 403 || response.code() == 500) {
                        openLoginActivity() //Ошибка аутентификации
                    } else { //Получен ответ
                        val trainings: List<List<Boolean>>? = response.body()
                        if (trainings != null && trainings.isNotEmpty()) {
                            //Показать результат тренировок, если они были
                            for (exercise in 0..12) {
                                // Отметка о выполнении i-го упражнения во каждый из 7 дней
                                val row = TableRow(applicationContext)
                                // Создание нового TextView для вставки в таблицу
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
                                    //Создание нового ImageView для вставки в таблицу
                                    val view = ImageView(applicationContext)
                                    view.textAlignment = View.TEXT_ALIGNMENT_CENTER
                                    if (trainings[training][exercise]) { //Установка + или x
                                        view.setImageResource(R.drawable.plus16)
                                    } else {
                                        view.setImageResource(R.drawable.x16)
                                    }
                                    row.addView(view)
                                }
                                table.addView(row)
                            }
                        } else { //Не показывать результат о тренировках, так как их нет
                            //Текст, который нужно скрыть
                            val textViewToHide: TextView =
                                findViewById(R.id.training_results_title_textview)
                            textViewToHide.visibility = View.GONE
                            table.visibility = View.GONE
                        }
                        Log.i("Success", response.toString())
                        progressBar.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<List<List<Boolean>>>, t: Throwable) {
                    //Текст, который нужно скрыть
                    val textViewToHide: TextView = findViewById(R.id.training_results_title_textview)
                    textViewToHide.visibility = View.GONE
                    table.visibility = View.GONE
                    Log.i("Failure", t.message ?: "Null message")
                    //Показ сообщения об ошибке
                    showPopupErrorMessage(getString(R.string.server_not_responding))
                    progressBar.visibility = View.GONE
                }
            })
            // TextView с результатом OKS в баллах
            val textViewResult: TextView = findViewById(R.id.oks_report_result_textview)
            textViewResult.text = getString(R.string.result_text, oksResultSum)
            // TextView с результатом прошлого OKS в баллах
            val textViewPrevResult: TextView = findViewById(R.id.oks_report_prev_result_textview)
            // TextView с текстом "Предыдущий результат"
            val textViewPreviousResultStart: TextView =
                findViewById(R.id.previous_result_start_textview)
            // TextView с текстом, зависящим от результата OKS
            val textViewOksReportExplanation: TextView =
                findViewById(R.id.oks_report_explanation_textview)
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
                in 0..26 -> {
                    textViewOksReportExplanation.text = getString(R.string.bad_outcome)
                }
                in 27..35 -> {
                    textViewOksReportExplanation.text = getString(R.string.good_outcome)
                }
                in 36..48 -> {
                    textViewOksReportExplanation.text = getString(R.string.excellent_outcome)
                }
            }
            // Сохраняем текущий результат OKS как предыдущий
            val editor = sharedPreferences.edit()
            editor.putInt("last_oks_result", oksResultSum)
            editor.apply()

        } else { // невозможно будет аутентифицировать запрос
            openLoginActivity()
        }
        progressBar.visibility = View.VISIBLE

    }
    //Открыть LoginActivity
    private fun openLoginActivity() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        TaskStackBuilder.create(applicationContext).addNextIntentWithParentStack(loginIntent)
            .startActivities()
    }
    //Показать всплывающее сообщение об ошибке
    private fun showPopupErrorMessage(
        //Текст ошибки
        errorText: String
    ) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, errorText, duration)
        toast.show()
    }
}