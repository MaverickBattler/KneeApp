package ru.leti.kneeapp.activity

import android.content.Context
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
import ru.leti.kneeapp.dto.AuthenticationRequestDto
import ru.leti.kneeapp.network.NetworkModule


class LoginActivity : AppCompatActivity() {

    private val userService = NetworkModule.userService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editTextEmailAddress: EditText = findViewById(R.id.editTextEmailAddress)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)
        val buttonToLogin: Button = findViewById(R.id.buttonToLogin)
        val progressBar: ProgressBar = findViewById(R.id.progressBarLogin)
        val errorMessage: TextView = findViewById(R.id.errorMessageLoginActivity)
        val toRegister: TextView = findViewById(R.id.toRegister)
        buttonToLogin.setOnClickListener {
            val email = editTextEmailAddress.text.toString()
            val password = editTextPassword.text.toString()
            val requestDto = AuthenticationRequestDto(email, password)
            userService.login(requestDto).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    //save authToken
                    if (response.code() == 403) {
                        showErrorMessage(getString(R.string.wrongUsernameOrPassword))
                    } else {
                        val sharedPref = getSharedPreferences("KneeApp", Context.MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.putString("auth_token", response.body())
                        println(response.headers())
                        editor.apply()
                        //get authToken
                        val sharedPref2 = getSharedPreferences("KneeApp", Context.MODE_PRIVATE)
                        val token = sharedPref2.getString("auth_token", null)
                        openMainActivity()
                    }
                    progressBar.visibility = View.INVISIBLE
                    buttonToLogin.visibility = View.VISIBLE
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.i("Failure", t.message ?: "Null message")
                    progressBar.visibility = View.INVISIBLE
                    buttonToLogin.visibility = View.VISIBLE
                    showErrorMessage(getString(R.string.serverNotResponding))
                }
            })
            buttonToLogin.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        }
        editTextEmailAddress.doAfterTextChanged {
            if (errorMessage.isVisible)
                errorMessage.visibility = View.INVISIBLE
        }
        editTextPassword.doAfterTextChanged {
            if (errorMessage.isVisible)
                errorMessage.visibility = View.INVISIBLE
        }
        toRegister.setOnClickListener {
            openEnterMedicalCardIdActivity()
        }
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun openEnterMedicalCardIdActivity() {
        val intent = Intent(this, EnterMedicalCardIdActivity::class.java)
        startActivity(intent)
    }

    private fun showErrorMessage(errorMessageString: String) {
        val animShake: Animation = AnimationUtils.loadAnimation(
            this,
            R.anim.text_shake_animaton
        )
        val errorMessage = findViewById<TextView>(R.id.errorMessageLoginActivity)
        errorMessage.setTextColor(
            ContextCompat.getColor(applicationContext, R.color.error_red)
        )
        errorMessage.text = errorMessageString
        errorMessage.visibility = View.VISIBLE
        errorMessage.startAnimation(animShake)
    }
}