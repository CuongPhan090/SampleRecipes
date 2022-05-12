package com.offline.continentalrecipesusingnavgraph.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.FragmentResultOwner
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.offline.continentalrecipesusingnavgraph.MealApplication
import com.offline.continentalrecipesusingnavgraph.R
import com.offline.continentalrecipesusingnavgraph.databinding.ActivityEntryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.*

@AndroidEntryPoint
class EntryActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityEntryBinding
    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var timer: Timer
    private var userSessionBegin = false
    private var now: Long = 0
    private var flag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val host =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = host.navController

        appBarConfig = AppBarConfiguration(
            setOf(R.id.category_fragment),
            drawerLayout = binding.drawerLayout
        )

        setSupportActionBar(binding.toolbar)
        setupActionBar(navController, appBarConfig)
        setupNavigationDrawer(navController)

        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun startUserSession() {
        timer = Timer()
        val sessionTimeoutDialog = layoutInflater.inflate(R.layout.session_time_dialog, null)
        timer.schedule(object: TimerTask() {
            override fun run() {
                sessionTimeoutDialog.findViewById<TextView>(R.id.session_timeout_message).text = getString(R.string.session_timeout_message)
                sessionTimeoutDialog.findViewById<TextView>(R.id.session_timer).text = getString(R.string.time_out_count_down)
                now = System.currentTimeMillis()
                runBlocking {
                    withContext(Dispatchers.Main) {
                        AlertDialog.Builder(binding.root.context)
                            .setTitle(getString(R.string.session_timeout_title))
                            .setView(sessionTimeoutDialog)
                            .setPositiveButton("Stay signed in") { dialog, _ ->
                                resetUserSession()
                                dialog.dismiss()
                            }
                            .setNegativeButton("Sign out") { _, _ ->
                                signOut()
                            }
                            .setCancelable(false)
                            .create()
                            .show()
                        repeat(61) {
                            delay(1000)
                            sessionTimeoutDialog.findViewById<TextView>(R.id.session_timer).text = "Time Out: ${(61 - (System.currentTimeMillis() - now) / 1000).toInt()}s"
                        }
                        signOut()
                    }
                }
            }
        }, 5000)
    }

    private fun signOut() {
        getSharedPreferences("credential", MODE_PRIVATE).edit().clear().apply()
        finish()
    }

    private fun resetUserSession() {
        timer.cancel()
        startUserSession()
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        supportFragmentManager.setFragmentResultListener("emailAddress", this) { _, bundle ->
            findViewById<TextView>(R.id.navdrawer_textview).text = "${bundle.getString("email")} Recipes"
        }

        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.category_fragment && flag ) {
                startUserSession()
                flag = false
                userSessionBegin = true
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        if (userSessionBegin) {
            resetUserSession()
        }
    }


    // display the up button if not top destination and hamburger icon if it is top destination
    // set the action bar title based on destination's label
    private fun setupActionBar(navController: NavController, appBarConfig: AppBarConfiguration) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    // if up button is clicked then go back the previous destination
    // if the hamburger icon is clicked then display the nav view
    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfig)
    }

    private fun setupNavigationDrawer(navController: NavController) {
        binding.navView.setupWithNavController(navController)

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.rate_menu_option -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.rate_uri))))
            R.id.share_menu_option -> startActivity(Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Share this app or I will delete your FORNITE account"
                )
                type = "text/plain"
            }, null))
            R.id.about_menu_option -> startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                this.data = Uri.parse("package:" + baseContext.packageName)
            })
            R.id.favorite_menu_option -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_category_fragment_to_favorite_fragment)
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
            R.id.log_out_menu_option -> {
                AlertDialog.Builder(binding.root.context)
                    .setTitle("Are you sure you want to log out?")
                    .setPositiveButton("Log out") { _, _ ->
                        getSharedPreferences("credential", MODE_PRIVATE).edit().clear().apply()
                        binding.drawerLayout.closeDrawer(GravityCompat.START)
                        findNavController(R.id.nav_host_fragment).navigate(R.id.action_category_fragment_to_login_fragment)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.scanner_option_menu -> findNavController(R.id.nav_host_fragment).navigate(R.id.action_category_fragment_to_scannerFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}
