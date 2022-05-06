package ru.leti.kneeapp.activity

import android.app.TaskStackBuilder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.leti.kneeapp.R
import ru.leti.kneeapp.data.Training
import ru.leti.kneeapp.dto.TrainingDto
import ru.leti.kneeapp.network.NetworkModule
import ru.leti.kneeapp.util.SharedPreferencesProvider
import java.lang.IllegalArgumentException

class ExerciseActivity : AppCompatActivity() {

    private val resultApiService = NetworkModule.resultApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val exercise = intent?.getParcelableExtra<Training>("exercise")
            ?: throw IllegalArgumentException("Missing argument")
        val exerciseNumber = intent?.getIntExtra("exercise_number", 1)
            ?: throw IllegalArgumentException("Missing argument")

        when (exerciseNumber) {
            in 1..2 -> { //в упражнении 2 картинки
                setContentView(R.layout.activity_exercise_2_images)

                val exerciseImage1: ImageView = findViewById(R.id.exercise_image_1)
                val exerciseImage2: ImageView = findViewById(R.id.exercise_image_2)
                exerciseImage1.setImageResource(exercise.exerciseImage1)
                exerciseImage2.setImageResource(exercise.exerciseImage2!!)
            }
            else -> { //в упражнении 1 картинка
                setContentView(R.layout.activity_exercise_1_image)

                val exerciseImage: ImageView = findViewById(R.id.exercise_image)
                exerciseImage.setImageResource(exercise.exerciseImage1)
            }
        }
        val exerciseName: TextView = findViewById(R.id.textViewExerciseName)
        val exerciseExplanation: TextView = findViewById(R.id.textViewExerciseExplanation)
        val buttonComplete: Button = findViewById(R.id.buttonMarkCompleted)
        val progressBar: ProgressBar = findViewById(R.id.progressBarCompleteTraining)
        exerciseName.text = getString(exercise.nameStringResourceId)
        exerciseExplanation.text = getString(exercise.explanationStringResourceId)

        val sharedPreferencesProvider = SharedPreferencesProvider(applicationContext)
        val sharedPreferences = sharedPreferencesProvider.getEncryptedSharedPreferences()
        val passMark = when (exerciseNumber) {
            0 -> sharedPreferences.getString("exercise1_1_pass_mark", null)!!
            1 -> sharedPreferences.getString("exercise1_2_pass_mark", null)!!
            2 -> sharedPreferences.getString("exercise1_3_pass_mark", null)!!
            3 -> sharedPreferences.getString("exercise2_1_pass_mark", null)!!
            4 -> sharedPreferences.getString("exercise3_1_pass_mark", null)!!
            5 -> sharedPreferences.getString("exercise3_2_pass_mark", null)!!
            6 -> sharedPreferences.getString("exercise3_3_pass_mark", null)!!
            7 -> sharedPreferences.getString("exercise4_1_pass_mark", null)!!
            8 -> sharedPreferences.getString("exercise4_2_1_pass_mark", null)!!
            9 -> sharedPreferences.getString("exercise4_2_2_pass_mark", null)!!
            10 -> sharedPreferences.getString("exercise4_2_3_pass_mark", null)!!
            11 -> sharedPreferences.getString("exercise4_3_1_pass_mark", null)!!
            else -> sharedPreferences.getString("exercise4_3_2_pass_mark", null)!!
        }
        when (passMark) {
            getString(R.string.completed) -> {
                //Упражнение выполнено сегодня
                buttonComplete.text = getString(R.string.completed_today)
                buttonComplete.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.light_green
                    )
                )
                buttonComplete.isEnabled = false
            }
            getString(R.string.not_allowed_to) -> {
                //Если сначала нужно заполнить анкету OKS
                buttonComplete.text = getString(R.string.fill_oks_anketa)
                buttonComplete.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.light_gray
                    )
                )
                buttonComplete.isEnabled = false
            }
            else -> {
                //Упражнение нужно выполнить
                buttonComplete.text = getString(R.string.mark_as_completed)
                buttonComplete.setBackgroundColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.brown
                    )
                )
                buttonComplete.isEnabled = true
            }
        }

        buttonComplete.setOnClickListener {
            val email = sharedPreferences.getString("email", null)
            val authToken = sharedPreferences.getString("auth_token", null)
            if (authToken != null && email != null) {
                val trainingDto = TrainingDto(
                    email, exerciseNumber.toShort(),
                    sharedPreferences.getLong("last_oks", 0)
                )
                val authHeader = "Bearer_$authToken"
                resultApiService.addTraining(
                    authHeader,
                    trainingDto
                ).enqueue(object : Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        if (response.code() == 403 || response.code() == 500) {
                            openLoginActivity() //Ошибка аутентификации
                        } else {//Получен ответ
                            val editor = sharedPreferences.edit()
                            when (exerciseNumber) {
                                0 -> editor.putString(
                                    "exercise1_1_pass_mark",
                                    getString(R.string.completed)
                                )
                                1 -> editor.putString(
                                    "exercise1_2_pass_mark",
                                    getString(R.string.completed)
                                )
                                2 -> editor.putString(
                                    "exercise1_3_pass_mark",
                                    getString(R.string.completed)
                                )
                                3 -> editor.putString(
                                    "exercise2_1_pass_mark",
                                    getString(R.string.completed)
                                )
                                4 -> editor.putString(
                                    "exercise3_1_pass_mark",
                                    getString(R.string.completed)
                                )
                                5 -> editor.putString(
                                    "exercise3_2_pass_mark",
                                    getString(R.string.completed)
                                )
                                6 -> editor.putString(
                                    "exercise3_3_pass_mark",
                                    getString(R.string.completed)
                                )
                                7 -> editor.putString(
                                    "exercise4_1_pass_mark",
                                    getString(R.string.completed)
                                )
                                8 -> editor.putString(
                                    "exercise4_2_1_pass_mark",
                                    getString(R.string.completed)
                                )
                                9 -> editor.putString(
                                    "exercise4_2_2_pass_mark",
                                    getString(R.string.completed)
                                )
                                10 -> editor.putString(
                                    "exercise4_2_3_pass_mark",
                                    getString(R.string.completed)
                                )
                                11 -> editor.putString(
                                    "exercise4_3_1_pass_mark",
                                    getString(R.string.completed)
                                )
                                12 -> editor.putString(
                                    "exercise4_3_2_pass_mark",
                                    getString(R.string.completed)
                                )
                            }
                            editor.apply()
                            //Упражнение выполнено сегодня
                            buttonComplete.text = getString(R.string.completed_today)
                            buttonComplete.setBackgroundColor(
                                ContextCompat.getColor(
                                    applicationContext,
                                    R.color.light_green
                                )
                            )
                            buttonComplete.isEnabled = false
                            progressBar.visibility = View.INVISIBLE
                            buttonComplete.visibility = View.VISIBLE
                        }
                        Log.i("Success", response.toString())
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Log.i("Failure", t.message ?: "Null message")
                        showPopupErrorMessage(getString(R.string.server_not_responding))
                        progressBar.visibility = View.INVISIBLE
                        buttonComplete.visibility = View.VISIBLE
                    }
                })
            } else {
                openLoginActivity()
            }
            buttonComplete.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        }
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