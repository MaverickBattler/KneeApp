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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.leti.kneeapp.network.NetworkModule
import ru.leti.kneeapp.R

class EnterMedicalCardIdActivity : AppCompatActivity() {

    private val patientIdentityApiService = NetworkModule.userApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_medical_card_id)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarRegistrationStep1)
        val buttonLogin = findViewById<Button>(R.id.buttonContinueToStep2)
        val editTextMedicalCardId = findViewById<EditText>(R.id.editTextMedicalCardID)
        val messageTextView = findViewById<TextView>(R.id.errorMessageRegistrationStep1)

        buttonLogin.setOnClickListener {
            val medicalCardId = editTextMedicalCardId.text.toString()
            val requestBody : RequestBody =
                medicalCardId.toRequestBody("text/plain".toMediaTypeOrNull())
            patientIdentityApiService.verifyMedicalCardId(requestBody)
                .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    when (response.body()) {
                        "to_be_registered" ->
                            openRegistrationActivity(editTextMedicalCardId.text.toString())
                        "registered" ->
                            showInformation(getString(R.string.user_already_registered_short))
                        else ->
                            showErrorMessage(getString(R.string.patient_not_found))
                    }
                    progressBar.visibility = View.INVISIBLE
                    buttonLogin.visibility = View.VISIBLE
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.i("Failure", t.message ?: "Null message")
                    if (t.message == "End of input")
                        showErrorMessage(getString(R.string.patient_not_found))
                    else
                        showErrorMessage(getString(R.string.server_not_responding))
                    progressBar.visibility = View.INVISIBLE
                    buttonLogin.visibility = View.VISIBLE
                }
            })
            buttonLogin.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        }
        editTextMedicalCardId.doAfterTextChanged {
            if (messageTextView.isVisible)
                messageTextView.visibility = View.INVISIBLE
        }
    }

    private fun openRegistrationActivity(medicalCardId : String) {
        val intent = Intent(this, RegistrationActivity::class.java)
        intent.putExtra("medicalCardId", medicalCardId)
        startActivity(intent)
    }

    private fun showErrorMessage(errorMessageString: String) {
        val animShake: Animation = AnimationUtils.loadAnimation(
            this,
            R.anim.text_shake_animaton
        )
        val errorMessage = findViewById<TextView>(R.id.errorMessageRegistrationStep1)
        errorMessage.setTextColor(
            ContextCompat.getColor(applicationContext, R.color.error_red)
        )
        errorMessage.text = errorMessageString
        errorMessage.visibility = View.VISIBLE
        errorMessage.startAnimation(animShake)
    }

    private fun showInformation(messageString: String) {
        val animShake: Animation = AnimationUtils.loadAnimation(
            this,
            R.anim.text_shake_animaton
        )
        val message = findViewById<TextView>(R.id.errorMessageRegistrationStep1)
        message.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.information_color
            )
        )
        message.text = messageString
        message.visibility = View.VISIBLE
        message.startAnimation(animShake)
    }
}