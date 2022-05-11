package com.offline.continentalrecipesusingnavgraph.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.FragmentResultOwner
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.offline.continentalrecipesusingnavgraph.R
import com.offline.continentalrecipesusingnavgraph.databinding.ActivityEntryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntryActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityEntryBinding
    private lateinit var appBarConfig: AppBarConfiguration

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

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        supportFragmentManager.setFragmentResultListener("emailAddress", this) { _, bundle ->
            findViewById<TextView>(R.id.navdrawer_textview).text = "${bundle.getString("email")} Recipes"
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
