package ru.leti.kneeapp.activity

import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import ru.leti.kneeapp.R
import ru.leti.kneeapp.util.SharedPreferencesProvider
import ru.leti.kneeapp.databinding.ActivityNavigationDrawerBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNavigationDrawerBinding

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

        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            val sharedPreferencesProvider = SharedPreferencesProvider(applicationContext)
            val sharedPreferences = sharedPreferencesProvider.getEncryptedSharedPreferences()
            val editor = sharedPreferences.edit()
            editor.remove("email")
            editor.remove("auth_token")
            editor.apply()
            openLoginActivity()
            true
        }

        val headerView : View = navView.getHeaderView(0)
        headerView.setOnClickListener {
            //Открытие фрагмента с профилем пользователя
        }
        val navUsername : TextView = headerView
            .findViewById(R.id.textViewFirstnameLastnameNavDrawer)
        val navUserEmail : TextView = headerView
            .findViewById(R.id.textViewEmailNavDrawer)
        val sharedPreferencesProvider = SharedPreferencesProvider(applicationContext)
        val sharedPreferences = sharedPreferencesProvider.getEncryptedSharedPreferences()
        val extras = intent.extras
        val nameAndSurname: String = extras?.getString("nameAndSurname") ?: "null"
        navUsername.text = nameAndSurname
        navUserEmail.text = sharedPreferences.getString("email", null)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navigation_drawer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun openLoginActivity() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        TaskStackBuilder.create(applicationContext)
            .addNextIntentWithParentStack(loginIntent).startActivities()
    }
}