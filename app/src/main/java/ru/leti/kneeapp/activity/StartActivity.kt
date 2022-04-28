package ru.leti.kneeapp.activity

import android.app.TaskStackBuilder
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.leti.kneeapp.R
import ru.leti.kneeapp.SharedPreferencesProvider
import ru.leti.kneeapp.dto.UserDataDto
import ru.leti.kneeapp.network.NetworkModule

class StartActivity : AppCompatActivity() {

    private val userService = NetworkModule.userService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val sharedPreferencesProvider = SharedPreferencesProvider(applicationContext)
        val sharedPreferences = sharedPreferencesProvider.getEncryptedSharedPreferences()
        val userEmail = sharedPreferences.getString("email", null)
        val authToken = sharedPreferences.getString("auth_token", null)
        val authHeader = "Bearer_$authToken"
        if (userEmail != null) {
            val requestBody : RequestBody =
                userEmail.toRequestBody("text/plain".toMediaTypeOrNull())
            userService.getUserData(authHeader, requestBody).enqueue(object :
                Callback<UserDataDto> {
                override fun onResponse(call: Call<UserDataDto>, response: Response<UserDataDto>) {
                    if (response.code() != 403 && response.code() != 500) {
                        val userDataDto = response.body()
                        if (userDataDto != null) {
                            openMainActivity(userDataDto.firstName, userDataDto.lastName)
                        } else {
                            openMainActivity(getString(R.string.Anonymous), "")
                        }
                    } else openLoginActivity()
                }

                override fun onFailure(call: Call<UserDataDto>, t: Throwable) {
                    Log.i("Failure", t.message ?: "Null message")
                    openLoginActivity()
                }
            })
        } else {
            openLoginActivity()
        }

    }

    private fun openLoginActivity() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        TaskStackBuilder.create(applicationContext)
            .addNextIntentWithParentStack(loginIntent).startActivities()
    }

    private fun openMainActivity(firstName: String, lastName: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("nameAndSurname", "$firstName $lastName")
        TaskStackBuilder.create(applicationContext)
            .addNextIntentWithParentStack(intent).startActivities()
    }
}