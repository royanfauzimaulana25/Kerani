package academy.bangkit.kerani.ui.activities

import academy.bangkit.kerani.data.reduceFileImage
import academy.bangkit.kerani.data.rotateBitmap
import academy.bangkit.kerani.databinding.ActivityDiagnoseBinding
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class DiagnoseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiagnoseBinding
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiagnoseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(false)
        showDiagnose(false)
        showCamera(true)
        binding.btnPhoto.setOnClickListener {
            showLoading(true)
            val intent = Intent(this@DiagnoseActivity, CameraActivity::class.java)
            launchCameraX.launch(intent)
        }

        binding.btnMore.setOnClickListener {
            val intent = Intent(this@DiagnoseActivity, ResultActivity::class.java)
            startActivity(intent)
        }
    }

    private val launchCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            showDiagnose(true)
            showCamera(false)
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            getFile = myFile
            val reduce = reduceFileImage(getFile as File)
            val result = rotateBitmap(BitmapFactory.decodeFile(reduce.path), isBackCamera)
            binding.previewImageView.setImageBitmap(result)
        }
    }

    override fun onResume() {
        super.onResume()
        showLoading(false)
    }

    private fun showDiagnose(show: Boolean) {
        binding.btnMore.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showCamera(show: Boolean) {
        binding.btnPhoto.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    companion object {
        const val CAMERA_X_RESULT = 200
    }
}