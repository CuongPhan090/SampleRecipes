package com.offline.continentalrecipesusingnavgraph.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
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
            else -> {
                item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment))
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
        return true
    }
}
