package ru.leti.kneeapp.activity

import android.app.TaskStackBuilder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import ru.leti.kneeapp.R
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils

import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.leti.kneeapp.dto.UserDataDto
import ru.leti.kneeapp.network.NetworkModule
import ru.leti.kneeapp.util.SharedPreferencesProvider

//Activity, соответствующее окну приложения, в котором пользователь отправляет
//сообщение лечащему врачу об экстренной ситуации
class ContactDoctorActivity : AppCompatActivity() {
    // подключение UserApiService
    private val userService = NetworkModule.userApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_doctor)
        val button: Button = findViewById(R.id.buttonSendEmergencies)
        val cb1: CheckBox = findViewById(R.id.checkBoxEmergency1)
        val cb2: CheckBox = findViewById(R.id.checkBoxEmergency2)
        val cb3: CheckBox = findViewById(R.id.checkBoxEmergency3)
        val cb4: CheckBox = findViewById(R.id.checkBoxEmergency4)
        val progressBar: ProgressBar = findViewById(R.id.progressBarSendEmergencies)
        button.setOnClickListener {
            // переменная, которая показывает, какие варианты выбрал пользователь
            var chooseNumber = 0
            if (cb1.isChecked) chooseNumber += 1
            if (cb2.isChecked) chooseNumber += 2
            if (cb3.isChecked) chooseNumber += 4
            if (cb4.isChecked) chooseNumber += 8
            if (chooseNumber == 0) {
                showErrorMessage(getString(R.string.please_check_a_checkbox),
                    R.color.errorRed_alternative)
            } else {
                val sharedPreferencesProvider = SharedPreferencesProvider(applicationContext)
                val sharedPreferences = sharedPreferencesProvider.getEncryptedSharedPreferences()
                //Получение данных из EncryptedSharedPreferences
                val userEmail = sharedPreferences.getString("email", null)
                if (userEmail != null) {
                    val authToken = sharedPreferences.getString("auth_token", null)
                    val authHeader = "Bearer_$authToken"
                    //Составление RequestBody из userEmail
                    val requestBody: RequestBody =
                        userEmail.toRequestBody("text/plain".toMediaTypeOrNull())
                    //Выполнение запроса к серверу
                    userService.getUserData(authHeader, requestBody).enqueue(object :
                        Callback<UserDataDto> {
                        override fun onResponse(
                            call: Call<UserDataDto>,
                            response: Response<UserDataDto>
                        ) {
                            if (response.code() != 403 && response.code() != 500) {
                                val userDataDto = response.body()
                                if (userDataDto != null) {
                                    val medicalCardId = userDataDto.medicalCardId
                                    val firstName = userDataDto.firstName
                                    val lastName = userDataDto.lastName
                                    val fatherName = userDataDto.fatherName
                                    val doctorEmail = userDataDto.doctorEmail
                                    val phoneNumber = userDataDto.phoneNumber
                                    //Составление текста письма
                                    var emailText: String = getString(
                                        R.string.email_text_start,
                                        medicalCardId, lastName, firstName, fatherName
                                    )
                                    when (chooseNumber) {
                                        1 -> emailText += getString(R.string.email_text_option1)
                                        2 -> emailText += getString(R.string.email_text_option2)
                                        3 -> emailText += getString(R.string.email_text_option3)
                                        4 -> emailText += getString(R.string.email_text_option4)
                                        5 -> emailText += getString(R.string.email_text_option5)
                                        6 -> emailText += getString(R.string.email_text_option6)
                                        7 -> emailText += getString(R.string.email_text_option7)
                                        8 -> emailText += getString(R.string.email_text_option8)
                                        9 -> emailText += getString(R.string.email_text_option9)
                                        10 -> emailText += getString(R.string.email_text_option10)
                                        11 -> emailText += getString(R.string.email_text_option11)
                                        12 -> emailText += getString(R.string.email_text_option12)
                                        13 -> emailText += getString(R.string.email_text_option13)
                                        14 -> emailText += getString(R.string.email_text_option14)
                                        15 -> emailText += getString(R.string.email_text_option15)
                                    }
                                    emailText += getString(
                                        R.string.email_text_end,
                                        phoneNumber,
                                        lastName,
                                        firstName
                                    )
                                    // Создание Intent для отправки письма по электронной почте
                                    val i = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
                                    i.putExtra(Intent.EXTRA_EMAIL, arrayOf(doctorEmail))
                                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                                    i.putExtra(
                                        Intent.EXTRA_TEXT,
                                        emailText
                                    )
                                    startActivity(Intent.createChooser(i, "Отправить сообщение при помощи..."))
                                    finish()
                                } else
                                    showErrorMessage(getString(R.string.internal_server_error),
                                        R.color.error_red)
                            } else //Ошибка аутентификации
                                openLoginActivity()
                            progressBar.visibility = View.INVISIBLE
                            button.visibility = View.VISIBLE
                        }

                        override fun onFailure(call: Call<UserDataDto>, t: Throwable) {
                            Log.i("Failure", t.message ?: "Null message")
                            showErrorMessage(getString(R.string.server_not_responding),
                                R.color.error_red)
                            progressBar.visibility = View.INVISIBLE
                            button.visibility = View.VISIBLE
                        }
                    })
                    button.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                } else {
                    openLoginActivity()
                }
            }
        }
    }

    //Показать всплывающее сообщение об ошибке
    private fun showErrorMessage(errorMessageString: String, color: Int) {
        val animShake: Animation = AnimationUtils.loadAnimation(
            this,
            R.anim.text_shake_animaton
        )
        val errorMessage = findViewById<TextView>(R.id.errorMessageContactDoctorActivity)
        errorMessage.setTextColor(
            ContextCompat.getColor(applicationContext, color)
        )
        errorMessage.text = errorMessageString
        errorMessage.visibility = View.VISIBLE
        errorMessage.startAnimation(animShake)
    }

    //Открыть LoginActivity
    private fun openLoginActivity() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        TaskStackBuilder.create(applicationContext)
            .addNextIntentWithParentStack(loginIntent).startActivities()
    }
}