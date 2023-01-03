package ru.leti.kneeapp.activity

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.view.isVisible
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.leti.kneeapp.AlarmReceiverNotificationOks
import ru.leti.kneeapp.AlarmReceiverNotificationTraining
import ru.leti.kneeapp.network.NetworkModule
import ru.leti.kneeapp.R
import ru.leti.kneeapp.TimeConstants
import ru.leti.kneeapp.util.SharedPreferencesProvider
import ru.leti.kneeapp.dto.OksResultDto
import java.util.*

class TestPageActivity : AppCompatActivity() {

    private val oksResultApiService = NetworkModule.resultsApiService

    private var questionNumber: Int = 1

    private var resultArr = ShortArray(12)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_page)

        val buttonNext: Button = findViewById(R.id.next_question_button)
        val radioGroup: RadioGroup = findViewById(R.id.variants_radiogroup)
        val errorMessage: TextView = findViewById(R.id.error_message_test_page_activity)
        radioGroup.setOnCheckedChangeListener { _, _ ->
            if (errorMessage.isVisible)
                errorMessage.visibility = View.INVISIBLE
        }
        questionNumber = 1
        setQuestionTextForQuestionNumber()

        buttonNext.setOnClickListener {
            onButtonPush()
        }

    }

    private fun onButtonPush() {
        val radioGroup: RadioGroup = findViewById(R.id.variants_radiogroup)
        val buttonNext: Button = findViewById(R.id.next_question_button)
        val progressBar: ProgressBar = findViewById(R.id.next_question_progress_bar_test_page_activity)

        if (radioGroup.checkedRadioButtonId == -1)
            showErrorMessage(getString(R.string.choose_an_option_error))
        else {
            resultArr[questionNumber - 1] = (radioGroup
                .indexOfChild(findViewById(radioGroup.checkedRadioButtonId))).toShort()
            if (questionNumber != 12) {//Кнопка нажата для включения следующего вопроса
                radioGroup.clearCheck()
                radioGroup.jumpDrawablesToCurrentState()
                questionNumber++
                setQuestionTextForQuestionNumber()
            } else { //Кнопка нажата для отправки результатов
                val sharedPreferencesProvider = SharedPreferencesProvider(applicationContext)
                val sharedPreferences = sharedPreferencesProvider.getEncryptedSharedPreferences()
                val email = sharedPreferences.getString("email", null)
                val authToken = sharedPreferences.getString("auth_token", null)
                if (authToken != null && email != null) {
                    val oksResult = OksResultDto(
                        email, resultArr[0], resultArr[1], resultArr[2],
                        resultArr[3], resultArr[4], resultArr[5], resultArr[6], resultArr[7],
                        resultArr[8], resultArr[9], resultArr[10], resultArr[11]
                    )
                    val authHeader = "Bearer_$authToken"
                    oksResultApiService.addOksResult(
                        authHeader,
                        oksResult
                    ).enqueue(object : Callback<Long> {
                        override fun onResponse(call: Call<Long>, response: Response<Long>) {
                            if (response.code() == 403 || response.code() == 500) {
                                openLoginActivity() //Ошибка аутентификации
                            } else {//Успешное завершение, переход к окну с отчетом
                                val timeInMillis: Long? = response.body()
                                val previousOks: Long = sharedPreferences.getLong("last_oks", 0)
                                if (timeInMillis != null) {
                                    setAlarm(timeInMillis + TimeConstants.WEEK_IN_MILLIS)
                                    setAlarmRepeating(timeInMillis)
                                    val editor = sharedPreferences.edit()
                                    editor.putLong("last_oks", timeInMillis)
                                    editor.putString(
                                        "exercise1_1_pass_mark",
                                        getString(R.string.not_completed)
                                    )
                                    editor.putString(
                                        "exercise1_2_pass_mark",
                                        getString(R.string.not_completed)
                                    )
                                    editor.putString(
                                        "exercise1_3_pass_mark",
                                        getString(R.string.not_completed)
                                    )
                                    editor.putString(
                                        "exercise2_1_pass_mark",
                                        getString(R.string.not_completed)
                                    )
                                    editor.putString(
                                        "exercise3_1_pass_mark",
                                        getString(R.string.not_completed)
                                    )
                                    editor.putString(
                                        "exercise3_2_pass_mark",
                                        getString(R.string.not_completed)
                                    )
                                    editor.putString(
                                        "exercise3_3_pass_mark",
                                        getString(R.string.not_completed)
                                    )
                                    editor.putString(
                                        "exercise4_1_pass_mark",
                                        getString(R.string.not_completed)
                                    )
                                    editor.putString(
                                        "exercise4_2_1_pass_mark",
                                        getString(R.string.not_completed)
                                    )
                                    editor.putString(
                                        "exercise4_2_2_pass_mark",
                                        getString(R.string.not_completed)
                                    )
                                    editor.putString(
                                        "exercise4_2_3_pass_mark",
                                        getString(R.string.not_completed)
                                    )
                                    editor.putString(
                                        "exercise4_3_1_pass_mark",
                                        getString(R.string.not_completed)
                                    )
                                    editor.putString(
                                        "exercise4_3_2_pass_mark",
                                        getString(R.string.not_completed)
                                    )

                                    editor.putInt("alarm_count", 1)
                                    editor.apply()

                                    openOksReportActivity(findOksResultSum(), previousOks)
                                    finish()
                                } else {
                                    showErrorMessage(getString(R.string.internal_server_error))
                                    progressBar.visibility = View.INVISIBLE
                                    buttonNext.visibility = View.VISIBLE
                                }
                            }
                            Log.i("Success", response.toString())
                        }

                        override fun onFailure(call: Call<Long>, t: Throwable) {
                            Log.i("Failure", t.message ?: "Null message")
                            showErrorMessage(getString(R.string.server_not_responding))
                            progressBar.visibility = View.INVISIBLE
                            buttonNext.visibility = View.VISIBLE
                        }
                    })
                } else {
                    openLoginActivity()
                }
                buttonNext.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun findOksResultSum(): Int {
        var resultSum = 0
        for (result in resultArr) {
            resultSum += (4 - result)
        }
        return resultSum
    }

    private fun openOksReportActivity(oksResultSum: Int, lastOksTime: Long) {
        val intent = Intent(this, ActivityOksReport::class.java)
        intent.putExtra("oks_result_sum", oksResultSum)
        intent.putExtra("last_oks_time", lastOksTime)
        startActivity(intent)
    }

    private fun setQuestionTextForQuestionNumber() {
        val questionTextView: TextView = findViewById(R.id.question_text_textview)
        val radioButtonVariant1: RadioButton = findViewById(R.id.option_1_radiobutton)
        val radioButtonVariant2: RadioButton = findViewById(R.id.option_2_radiobutton)
        val radioButtonVariant3: RadioButton = findViewById(R.id.option_3_radiobutton)
        val radioButtonVariant4: RadioButton = findViewById(R.id.option_4_radiobutton)
        val radioButtonVariant5: RadioButton = findViewById(R.id.option_5_radiobutton)
        val questionNumberView: TextView = findViewById(R.id.question_number_textview)
        val buttonNext: Button = findViewById(R.id.next_question_button)

        when (questionNumber) {
            1 -> {
                questionTextView.text = getString(R.string.question1)
                radioButtonVariant1.text = getString(R.string.question1option1)
                radioButtonVariant2.text = getString(R.string.question1option2)
                radioButtonVariant3.text = getString(R.string.question1option3)
                radioButtonVariant4.text = getString(R.string.question1option4)
                radioButtonVariant5.text = getString(R.string.question1option5)
            }
            2 -> {
                questionTextView.text = getString(R.string.question2)
                radioButtonVariant1.text = getString(R.string.question2option1)
                radioButtonVariant2.text = getString(R.string.question2option2)
                radioButtonVariant3.text = getString(R.string.question2option3)
                radioButtonVariant4.text = getString(R.string.question2option4)
                radioButtonVariant5.text = getString(R.string.question2option5)
            }
            3 -> {
                questionTextView.text = getString(R.string.question3)
                radioButtonVariant1.text = getString(R.string.question3option1)
                radioButtonVariant2.text = getString(R.string.question3option2)
                radioButtonVariant3.text = getString(R.string.question3option3)
                radioButtonVariant4.text = getString(R.string.question3option4)
                radioButtonVariant5.text = getString(R.string.question3option5)
            }
            4 -> {
                questionTextView.text = getString(R.string.question4)
                radioButtonVariant1.text = getString(R.string.question4option1)
                radioButtonVariant2.text = getString(R.string.question4option2)
                radioButtonVariant3.text = getString(R.string.question4option3)
                radioButtonVariant4.text = getString(R.string.question4option4)
                radioButtonVariant5.text = getString(R.string.question4option5)
            }
            5 -> {
                questionTextView.text = getString(R.string.question5)
                radioButtonVariant1.text = getString(R.string.question5option1)
                radioButtonVariant2.text = getString(R.string.question5option2)
                radioButtonVariant3.text = getString(R.string.question5option3)
                radioButtonVariant4.text = getString(R.string.question5option4)
                radioButtonVariant5.text = getString(R.string.question5option5)
            }
            6 -> {
                questionTextView.text = getString(R.string.question6)
                radioButtonVariant1.text = getString(R.string.question6option1)
                radioButtonVariant2.text = getString(R.string.question6option2)
                radioButtonVariant3.text = getString(R.string.question6option3)
                radioButtonVariant4.text = getString(R.string.question6option4)
                radioButtonVariant5.text = getString(R.string.question6option5)
            }
            7 -> {
                questionTextView.text = getString(R.string.question7)
                radioButtonVariant1.text = getString(R.string.question7option1)
                radioButtonVariant2.text = getString(R.string.question7option2)
                radioButtonVariant3.text = getString(R.string.question7option3)
                radioButtonVariant4.text = getString(R.string.question7option4)
                radioButtonVariant5.text = getString(R.string.question7option5)
            }
            8 -> {
                questionTextView.text = getString(R.string.question8)
                radioButtonVariant1.text = getString(R.string.question8option1)
                radioButtonVariant2.text = getString(R.string.question8option2)
                radioButtonVariant3.text = getString(R.string.question8option3)
                radioButtonVariant4.text = getString(R.string.question8option4)
                radioButtonVariant5.text = getString(R.string.question8option5)
            }
            9 -> {
                questionTextView.text = getString(R.string.question9)
                radioButtonVariant1.text = getString(R.string.question9option1)
                radioButtonVariant2.text = getString(R.string.question9option2)
                radioButtonVariant3.text = getString(R.string.question9option3)
                radioButtonVariant4.text = getString(R.string.question9option4)
                radioButtonVariant5.text = getString(R.string.question9option5)
            }
            10 -> {
                questionTextView.text = getString(R.string.question10)
                radioButtonVariant1.text = getString(R.string.question10option1)
                radioButtonVariant2.text = getString(R.string.question10option2)
                radioButtonVariant3.text = getString(R.string.question10option3)
                radioButtonVariant4.text = getString(R.string.question10option4)
                radioButtonVariant5.text = getString(R.string.question10option5)
            }
            11 -> {
                questionTextView.text = getString(R.string.question11)
                radioButtonVariant1.text = getString(R.string.question11option1)
                radioButtonVariant2.text = getString(R.string.question11option2)
                radioButtonVariant3.text = getString(R.string.question11option3)
                radioButtonVariant4.text = getString(R.string.question11option4)
                radioButtonVariant5.text = getString(R.string.question11option5)
            }
            12 -> {
                questionTextView.text = getString(R.string.question12)
                radioButtonVariant1.text = getString(R.string.question12option1)
                radioButtonVariant2.text = getString(R.string.question12option2)
                radioButtonVariant3.text = getString(R.string.question12option3)
                radioButtonVariant4.text = getString(R.string.question12option4)
                radioButtonVariant5.text = getString(R.string.question12option5)
                buttonNext.text = getString(R.string.send)
            }
        }
        questionNumberView.text = questionNumber.toString()
    }

    private fun showErrorMessage(errorMessageString: String) {
        val animShake: Animation = AnimationUtils.loadAnimation(
            this,
            R.anim.text_shake_animaton
        )
        val errorMessage = findViewById<TextView>(R.id.error_message_test_page_activity)
        errorMessage.text = errorMessageString
        errorMessage.visibility = View.VISIBLE
        errorMessage.startAnimation(animShake)
    }

    private fun openLoginActivity() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        TaskStackBuilder.create(applicationContext).addNextIntentWithParentStack(loginIntent)
            .startActivities()
    }

    private fun setAlarm(at: Long) {
        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, AlarmReceiverNotificationOks::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            applicationContext, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.set(AlarmManager.RTC, at, pendingIntent)
    }

    private fun setAlarmRepeating(at: Long) {
        Log.i("Info", "Setting repeating alarm")
        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(applicationContext, AlarmReceiverNotificationTraining::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(
            applicationContext, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        Log.i("Info", "current time = ${Calendar.getInstance().timeInMillis}," +
                "Time to trigger = $at")
        alarmManager.setRepeating(AlarmManager.RTC, at, TimeConstants.DAY_IN_MILLIS, pendingIntent)
    }

}