package academy.bangkit.kerani.ui.activities

import academy.bangkit.kerani.databinding.ActivityEditProfileBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarHome.toolbar)
    }
}