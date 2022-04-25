package ru.leti.kneeapp.activity

import android.app.TaskStackBuilder
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
import ru.leti.kneeapp.network.NetworkModule
import ru.leti.kneeapp.R
import ru.leti.kneeapp.SharedPreferencesProvider
import ru.leti.kneeapp.dto.OksResultDto

class TestPageActivity : AppCompatActivity() {

    private val oksResultApiService = NetworkModule.oksResultApiService

    private var questionNumber: Int = 1

    private var resultArr = ShortArray(12)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_page)

        val buttonNext: Button = findViewById(R.id.nextButton)
        val radioGroup: RadioGroup = findViewById(R.id.radioGroupVariants)
        val errorMessage: TextView = findViewById(R.id.errorMessageTestPageActivity)
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
        val radioGroup: RadioGroup = findViewById(R.id.radioGroupVariants)
        val buttonNext: Button = findViewById(R.id.nextButton)
        val progressBar: ProgressBar = findViewById(R.id.progressBarTestPage)

        if (radioGroup.checkedRadioButtonId == -1)
            showErrorMessage(getString(R.string.chooseAnOptionError))
        else {
            resultArr[questionNumber - 1] = (radioGroup
                .indexOfChild(findViewById(radioGroup.checkedRadioButtonId)) + 1).toShort()
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
                    ).enqueue(object : Callback<Unit> {
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            if (response.code() == 403 || response.code() == 500) {
                                openLoginActivity() //Ошибка аутентификации
                            } else {
                                finish() //Успешное завершение, переход к главному окну
                            }
                            Log.i("Success", response.toString())
                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            Log.i("Failure", t.message ?: "Null message")
                            showErrorMessage(getString(R.string.serverNotResponding))
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

    private fun setQuestionTextForQuestionNumber() {
        val questionTextView: TextView = findViewById(R.id.questionText)
        val radioButtonVariant1: RadioButton = findViewById(R.id.option1)
        val radioButtonVariant2: RadioButton = findViewById(R.id.option2)
        val radioButtonVariant3: RadioButton = findViewById(R.id.option3)
        val radioButtonVariant4: RadioButton = findViewById(R.id.option4)
        val radioButtonVariant5: RadioButton = findViewById(R.id.option5)
        val questionNumberView: TextView = findViewById(R.id.questionNumber)
        val buttonNext: Button = findViewById(R.id.nextButton)

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
        val errorMessage = findViewById<TextView>(R.id.errorMessageTestPageActivity)
        errorMessage.text = errorMessageString
        errorMessage.visibility = View.VISIBLE
        errorMessage.startAnimation(animShake)
    }

    private fun openLoginActivity() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        TaskStackBuilder.create(applicationContext).addNextIntentWithParentStack(loginIntent)
            .startActivities()
    }

}