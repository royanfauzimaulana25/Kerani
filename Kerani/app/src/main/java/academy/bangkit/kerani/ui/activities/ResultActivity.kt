package academy.bangkit.kerani.ui.activities

import academy.bangkit.kerani.databinding.ActivityResultBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object {
        const val CAMERA_X_RESULT = 200
    }
}