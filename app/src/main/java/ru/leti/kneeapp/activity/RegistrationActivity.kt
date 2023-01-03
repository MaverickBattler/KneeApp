package ru.leti.kneeapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.leti.kneeapp.R
import ru.leti.kneeapp.dto.RegistrationRequestDto
import ru.leti.kneeapp.network.NetworkModule

class RegistrationActivity : AppCompatActivity() {

    private val userService = NetworkModule.userApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val passwordEditText: EditText = findViewById(R.id.create_password_edittext)
        val passwordRepeatEditText: EditText = findViewById(R.id.repeat_password_edittext)
        val buttonFinishRegistration: Button = findViewById(R.id.finish_registration_button)
        val messageTextView: TextView = findViewById(R.id.error_message_registration_step_2)
        val progressBar: ProgressBar = findViewById(R.id.registration_step_2_progressbar)
        buttonFinishRegistration.setOnClickListener {
            val password: String = passwordEditText.text.toString()
            val passwordRepeated: String = passwordRepeatEditText.text.toString()
            if (password == passwordRepeated && password.length in 8..32) {
                val extras = intent.extras
                val medicalCardId: String = extras?.getString("medicalCardId") ?: "null"
                val requestDto = RegistrationRequestDto(medicalCardId, password)
                userService.register(requestDto).enqueue(object :
                    Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        val email: String? = response.body()
                        if (email != null) {
                            openRegistrationSuccessActivity(email)
                        } else {
                            showErrorMessage(getString(R.string.could_not_get_email))
                        }
                        progressBar.visibility = View.INVISIBLE
                        buttonFinishRegistration.visibility = View.VISIBLE
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.i("Failure", t.message ?: "Null message")
                        progressBar.visibility = View.INVISIBLE
                        buttonFinishRegistration.visibility = View.VISIBLE
                        showErrorMessage(getString(R.string.patient_not_found))
                    }
                })
                buttonFinishRegistration.visibility = View.INVISIBLE
                progressBar.visibility = View.VISIBLE


            } else if (password.length in 8..32)
                showErrorMessage(getString(R.string.passwords_dont_match))
            else
                showErrorMessage(getString(R.string.password_must_be_in_range))

        }
        passwordEditText.doAfterTextChanged {
            if (messageTextView.isVisible)
                messageTextView.visibility = View.INVISIBLE
        }
        passwordRepeatEditText.doAfterTextChanged {
            if (messageTextView.isVisible)
                messageTextView.visibility = View.INVISIBLE
        }

    }

    private fun openRegistrationSuccessActivity(email: String) {
        val intent = Intent(this, RegistrationSuccessActivity::class.java)
        intent.putExtra("email", email)
        finishAffinity()
        startActivity(intent)
    }

    private fun showErrorMessage(errorMessageString: String) {
        val animShake: Animation = AnimationUtils.loadAnimation(
            this,
            R.anim.text_shake_animaton
        )
        val errorMessage = findViewById<TextView>(R.id.error_message_registration_step_2)
        errorMessage.setTextColor(
            ContextCompat.getColor(applicationContext, R.color.error_red)
        )
        errorMessage.text = errorMessageString
        errorMessage.visibility = View.VISIBLE
        errorMessage.startAnimation(animShake)
    }
}