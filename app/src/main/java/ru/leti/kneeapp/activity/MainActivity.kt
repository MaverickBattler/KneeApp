package ru.leti.kneeapp.activity

import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.leti.kneeapp.R
import ru.leti.kneeapp.util.SharedPreferencesProvider
import ru.leti.kneeapp.databinding.ActivityNavigationDrawerBinding
import ru.leti.kneeapp.dto.UserDataDto
import ru.leti.kneeapp.network.NetworkModule

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNavigationDrawerBinding

    private val userService = NetworkModule.userService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarNavigationDrawer.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_oks, R.id.nav_training, R.id.nav_feedback
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val sharedPreferencesProvider = SharedPreferencesProvider(applicationContext)
        val sharedPreferences = sharedPreferencesProvider.getEncryptedSharedPreferences()

        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            val editor = sharedPreferences.edit()
            editor.remove("email")
            editor.remove("auth_token")
            editor.apply()
            openLoginActivity()
            true
        }

        val headerView: View = navView.getHeaderView(0)
        var connecting = false
        headerView.setOnClickListener {
            if (!connecting) {
                connecting = true
                val userEmail = sharedPreferences.getString("email", null)
                if (userEmail != null) {
                    val authToken = sharedPreferences.getString("auth_token", null)
                    val authHeader = "Bearer_$authToken"
                    val requestBody: RequestBody =
                        userEmail.toRequestBody("text/plain".toMediaTypeOrNull())
                    userService.getUserData(authHeader, requestBody).enqueue(object :
                        Callback<UserDataDto> {
                        override fun onResponse(
                            call: Call<UserDataDto>,
                            response: Response<UserDataDto>
                        ) {
                            if (response.code() != 403 && response.code() != 500) {
                                val userDataDto = response.body()
                                if (userDataDto != null) {
                                    //Открытие профиля пользователя
                                    connecting = false
                                    openUserProfileActivity(userDataDto)
                                } else {
                                    connecting = false
                                    showPopupErrorMessage(getString(R.string.internal_server_error))
                                }
                            } else {
                                openLoginActivity()
                            }
                        }

                        override fun onFailure(call: Call<UserDataDto>, t: Throwable) {
                            connecting = false
                            Log.i("Failure", t.message ?: "Null message")
                            showPopupErrorMessage(getString(R.string.server_not_responding))
                        }
                    })
                } else {
                    openLoginActivity()
                }
            }
        }
        val navUsername: TextView = headerView
            .findViewById(R.id.textViewFirstnameLastnameNavDrawer)
        val navUserEmail: TextView = headerView
            .findViewById(R.id.textViewEmailNavDrawer)
        val extras = intent.extras
        val nameAndSurname: String = extras?.getString("nameAndSurname") ?: "null"
        navUsername.text = nameAndSurname
        navUserEmail.text = sharedPreferences.getString("email", null)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun openUserProfileActivity(userDataDto: UserDataDto) {
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.putExtra("medicalCardId", userDataDto.medicalCardId)
        intent.putExtra("email", userDataDto.email)
        intent.putExtra("phoneNumber", userDataDto.phoneNumber)
        intent.putExtra("firstName", userDataDto.firstName)
        intent.putExtra("lastName", userDataDto.lastName)
        intent.putExtra("fatherName", userDataDto.fatherName)
        startActivity(intent)
    }

    private fun openLoginActivity() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        TaskStackBuilder.create(applicationContext)
            .addNextIntentWithParentStack(loginIntent).startActivities()
    }

    private fun showPopupErrorMessage(errorText: String) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, errorText, duration)
        toast.show()
    }
}