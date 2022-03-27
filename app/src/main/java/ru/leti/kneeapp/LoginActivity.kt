package ru.leti.kneeapp

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
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val patientIdentityApiService = NetworkModule.patientIdentityApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val editTextMedicalCardId = findViewById<EditText>(R.id.editTextMedicalCardId)
        val errorMessage = findViewById<TextView>(R.id.loginErrorMessage)
        val animShake: Animation = AnimationUtils.loadAnimation(this, R.anim.text_shake_animaton)
        buttonLogin.setOnClickListener {
            val requestDto = AuthenticationRequestDto(editTextMedicalCardId.text.toString())
            patientIdentityApiService.authenticate(requestDto).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val phoneNumber: String? = response.body()
                    if (phoneNumber != null) {
                        Log.i("Success", phoneNumber)
                    }
                    finish()
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.i("Failure", t.message ?: "Null message")
                    progressBar.visibility = View.INVISIBLE
                    buttonLogin.visibility = View.VISIBLE
                    errorMessage.visibility = View.VISIBLE
                    errorMessage.startAnimation(animShake)
                }
            })
            buttonLogin.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        }
        editTextMedicalCardId.doAfterTextChanged {
            if (errorMessage.isVisible)
                errorMessage.visibility = View.INVISIBLE
        }
    }
}