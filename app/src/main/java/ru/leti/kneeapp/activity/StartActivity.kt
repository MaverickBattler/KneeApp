package ru.leti.kneeapp.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
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
import ru.leti.kneeapp.util.SharedPreferencesProvider
import ru.leti.kneeapp.dto.UserDataDto
import ru.leti.kneeapp.network.NetworkModule

//Activity, соответствующее стартовому окну приложения
//Здесь делается вся подготовка к запуску приложения,
//и после нее автоматически открывается LoginActivity или MainActivity
class StartActivity : AppCompatActivity() {

    // подключение UserApiService
    private val userService = NetworkModule.userApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        //Создаем канал уведомлений
        createNotificationChannel()
        //Получение экземпляра EncryptedSharedPreferences
        val sharedPreferencesProvider = SharedPreferencesProvider(applicationContext)
        val sharedPreferences = sharedPreferencesProvider.getEncryptedSharedPreferences()
        //Получение электронной почты пользователя из EncryptedSharedPreferences
        val userEmail = sharedPreferences.getString("email", null)
        if (userEmail != null) {
            //Получение токена аутентификации из EncryptedSharedPreferences
            val authToken = sharedPreferences.getString("auth_token", null)
            val authHeader = "Bearer_$authToken"
            //Составление RequestBody из userEmail
            val requestBody : RequestBody =
                userEmail.toRequestBody("text/plain".toMediaTypeOrNull())
            //Выполнение запроса
            userService.getUserData(authHeader, requestBody).enqueue(object :
                Callback<UserDataDto> {
                override fun onResponse(call: Call<UserDataDto>, response: Response<UserDataDto>) {
                    if (response.code() != 403 && response.code() != 500) {
                        //Получение UserDataDto из ответа
                        val userDataDto = response.body()
                        if (userDataDto != null) { //Если получили данные пользователя
                            openMainActivity(userDataDto.firstName, userDataDto.lastName)
                        } else {
                            openMainActivity(getString(R.string.anonymous), "")
                        }
                    } //Ошибка аутентификации
                    else openLoginActivity()
                }

                override fun onFailure(call: Call<UserDataDto>, t: Throwable) {
                    Log.i("Failure", t.message ?: "Null message")
                    openLoginActivity()
                }
            })
        } else { //Аутентификация будет невозможна
            openLoginActivity()
        }

    }
    //Открытие LoginActivity
    private fun openLoginActivity() {
        //Создание Intent для перехода в LoginActivity
        val loginIntent = Intent(this, LoginActivity::class.java)
        //Открытие окна и закрытие всех остальных
        TaskStackBuilder.create(applicationContext)
            .addNextIntentWithParentStack(loginIntent).startActivities()
    }
    // Открытие MainActivity
    private fun openMainActivity(firstName: String, lastName: String) {
        //Создание Intent для перехода в MainActivity
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("nameAndSurname", "$firstName $lastName")
        //Открытие окна и закрытие всех остальных
        TaskStackBuilder.create(applicationContext)
            .addNextIntentWithParentStack(intent).startActivities()
    }

    // Создаем NotificationChannel, но только для API 26+, потому что
    // класс NotificationChannel является новым и не находится в support library
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Id канала уведомлений
            val channelId = getString(R.string.channel_id)
            //Название канала уведомлений
            val name = getString(R.string.channel_name)
            //Описание канала уведомлений
            val descriptionText = getString(R.string.channel_description)
            //Важность уведомлений канала
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            //Создание канала уведомлений
            val channel =
                NotificationChannel(channelId, name, importance).apply {
                    description = descriptionText
                }
            // Регистрация канала в системе
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}