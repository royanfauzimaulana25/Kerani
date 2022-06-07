package academy.bangkit.kerani.ui.activities

import academy.bangkit.kerani.R
import academy.bangkit.kerani.data.Preferences
import academy.bangkit.kerani.databinding.ActivityHomeBinding
import academy.bangkit.kerani.ui.fragments.HistoryFragment
import academy.bangkit.kerani.ui.fragments.HomeFragment
import academy.bangkit.kerani.ui.fragments.ProfileFragment
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sharedPref: Preferences

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                val permissionDenied = resources.getString(R.string.permission_denied)
                Toast.makeText(
                    this,
                    permissionDenied,
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarHome.toolbar)
        supportActionBar?.title = "Kerabat Tani"

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        auth = FirebaseAuth.getInstance()
        sharedPref = Preferences(this)

        val navTheme = binding.navView.menu.findItem(R.id.nav_theme)
        val switch = navTheme.actionView as SwitchCompat
        switch.setOnCheckedChangeListener { _, isChecked ->
            when(isChecked){
                true -> {
                    sharedPref.put("dark_mode", true)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                false -> {
                    sharedPref.put("dark_mode", false)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }

        switch.isChecked = sharedPref.getBoolean("dark_mode")

        drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            when (menuItem.itemId){
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_profile -> replaceFragment(ProfileFragment())
                R.id.nav_history -> replaceFragment(HistoryFragment())
                R.id.nav_theme -> {
                    switch.isChecked = !switch.isChecked
                }
                R.id.nav_logout -> {
                    auth.signOut()
                    val logoutSuccess = resources.getString(R.string.logout_success)
                    Toast.makeText(applicationContext, logoutSuccess, Toast.LENGTH_SHORT).show()
                    Intent(this, LoginActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_placeholder, fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}