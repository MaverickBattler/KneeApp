package ru.leti.kneeapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.leti.kneeapp.network.NetworkModule
import ru.leti.kneeapp.OKSResult
import ru.leti.kneeapp.R

class TestPageActivity : AppCompatActivity() {

    private val oksResultApiService = NetworkModule.oksResultApiService

    private var questionNumber: Int = 1

    private var resultArr = ShortArray(12)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_page)

        val buttonNext: Button = findViewById(R.id.nextButton)
        questionNumber = 1
        buttonNext.setOnClickListener {
            buttonNextActivity()
        }
        val questionTextView: TextView = findViewById(R.id.questionText)
        val radioButtonVariant1: RadioButton = findViewById(R.id.option1)
        val radioButtonVariant2: RadioButton = findViewById(R.id.option2)
        val radioButtonVariant3: RadioButton = findViewById(R.id.option3)
        val radioButtonVariant4: RadioButton = findViewById(R.id.option4)
        val radioButtonVariant5: RadioButton = findViewById(R.id.option5)
        val questionNumberView: TextView = findViewById(R.id.questionNumber)
        questionTextView.text = getString(R.string.question1)
        questionNumberView.text = questionNumber.toString()
        radioButtonVariant1.text = getString(R.string.question1option1)
        radioButtonVariant2.text = getString(R.string.question1option2)
        radioButtonVariant3.text = getString(R.string.question1option3)
        radioButtonVariant4.text = getString(R.string.question1option4)
        radioButtonVariant5.text = getString(R.string.question1option5)
    }

    private fun buttonNextActivity() {
        val buttonNext: Button = findViewById(R.id.nextButton)
        val questionNumberView: TextView = findViewById(R.id.questionNumber)
        val questionTextView: TextView = findViewById(R.id.questionText)
        val radioGroup: RadioGroup = findViewById(R.id.radioGroupVariants)
        val radioButtonVariant1: RadioButton = findViewById(R.id.option1)
        val radioButtonVariant2: RadioButton = findViewById(R.id.option2)
        val radioButtonVariant3: RadioButton = findViewById(R.id.option3)
        val radioButtonVariant4: RadioButton = findViewById(R.id.option4)
        val radioButtonVariant5: RadioButton = findViewById(R.id.option5)
        resultArr[questionNumber - 1] = (radioGroup
            .indexOfChild(findViewById(radioGroup.checkedRadioButtonId)) + 1).toShort()
        radioGroup.clearCheck()
        radioGroup.jumpDrawablesToCurrentState()

        when (questionNumber) {
            1 -> {
                questionTextView.text = getString(R.string.question2)
                radioButtonVariant1.text = getString(R.string.question2option1)
                radioButtonVariant2.text = getString(R.string.question2option2)
                radioButtonVariant3.text = getString(R.string.question2option3)
                radioButtonVariant4.text = getString(R.string.question2option4)
                radioButtonVariant5.text = getString(R.string.question2option5)
            }
            2 -> {
                questionTextView.text = getString(R.string.question3)
                radioButtonVariant1.text = getString(R.string.question3option1)
                radioButtonVariant2.text = getString(R.string.question3option2)
                radioButtonVariant3.text = getString(R.string.question3option3)
                radioButtonVariant4.text = getString(R.string.question3option4)
                radioButtonVariant5.text = getString(R.string.question3option5)
            }
            3 -> {
                questionTextView.text = getString(R.string.question4)
                radioButtonVariant1.text = getString(R.string.question4option1)
                radioButtonVariant2.text = getString(R.string.question4option2)
                radioButtonVariant3.text = getString(R.string.question4option3)
                radioButtonVariant4.text = getString(R.string.question4option4)
                radioButtonVariant5.text = getString(R.string.question4option5)
            }
            4 -> {
                questionTextView.text = getString(R.string.question5)
                radioButtonVariant1.text = getString(R.string.question5option1)
                radioButtonVariant2.text = getString(R.string.question5option2)
                radioButtonVariant3.text = getString(R.string.question5option3)
                radioButtonVariant4.text = getString(R.string.question5option4)
                radioButtonVariant5.text = getString(R.string.question5option5)
            }
            5 -> {
                questionTextView.text = getString(R.string.question6)
                radioButtonVariant1.text = getString(R.string.question6option1)
                radioButtonVariant2.text = getString(R.string.question6option2)
                radioButtonVariant3.text = getString(R.string.question6option3)
                radioButtonVariant4.text = getString(R.string.question6option4)
                radioButtonVariant5.text = getString(R.string.question6option5)
            }
            6 -> {
                questionTextView.text = getString(R.string.question7)
                radioButtonVariant1.text = getString(R.string.question7option1)
                radioButtonVariant2.text = getString(R.string.question7option2)
                radioButtonVariant3.text = getString(R.string.question7option3)
                radioButtonVariant4.text = getString(R.string.question7option4)
                radioButtonVariant5.text = getString(R.string.question7option5)
            }
            7 -> {
                questionTextView.text = getString(R.string.question8)
                radioButtonVariant1.text = getString(R.string.question8option1)
                radioButtonVariant2.text = getString(R.string.question8option2)
                radioButtonVariant3.text = getString(R.string.question8option3)
                radioButtonVariant4.text = getString(R.string.question8option4)
                radioButtonVariant5.text = getString(R.string.question8option5)
            }
            8 -> {
                questionTextView.text = getString(R.string.question9)
                radioButtonVariant1.text = getString(R.string.question9option1)
                radioButtonVariant2.text = getString(R.string.question9option2)
                radioButtonVariant3.text = getString(R.string.question9option3)
                radioButtonVariant4.text = getString(R.string.question9option4)
                radioButtonVariant5.text = getString(R.string.question9option5)
            }
            9 -> {
                questionTextView.text = getString(R.string.question10)
                radioButtonVariant1.text = getString(R.string.question10option1)
                radioButtonVariant2.text = getString(R.string.question10option2)
                radioButtonVariant3.text = getString(R.string.question10option3)
                radioButtonVariant4.text = getString(R.string.question10option4)
                radioButtonVariant5.text = getString(R.string.question10option5)
            }
            10 -> {
                questionTextView.text = getString(R.string.question11)
                radioButtonVariant1.text = getString(R.string.question11option1)
                radioButtonVariant2.text = getString(R.string.question11option2)
                radioButtonVariant3.text = getString(R.string.question11option3)
                radioButtonVariant4.text = getString(R.string.question11option4)
                radioButtonVariant5.text = getString(R.string.question11option5)
            }
            11 -> {
                questionTextView.text = getString(R.string.question12)
                radioButtonVariant1.text = getString(R.string.question12option1)
                radioButtonVariant2.text = getString(R.string.question12option2)
                radioButtonVariant3.text = getString(R.string.question12option3)
                radioButtonVariant4.text = getString(R.string.question12option4)
                radioButtonVariant5.text = getString(R.string.question12option5)
            }
        }
        when (questionNumber) {
            in 1..10 -> {
                questionNumber++
                questionNumberView.text = questionNumber.toString()
            }
            11 -> {
                questionNumber++
                questionNumberView.text = questionNumber.toString()
                buttonNext.text = getString(R.string.send)
            }
            12 -> {
                val oksResult = OKSResult(resultArr[0], resultArr[1], resultArr[2],
                    resultArr[3], resultArr[4], resultArr[5], resultArr[6], resultArr[7],
                    resultArr[8], resultArr[9], resultArr[10], resultArr[11]
                )
                Log.i("Success", oksResult.toString())
                oksResultApiService.addOKSResult(oksResult).enqueue(object : Callback<Any> {
                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        Log.i("Success", response.toString())
                    }

                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        Log.i("Failure", t.message ?: "Null message")
                    }

                })
                finish()
            }
        }
    }

}