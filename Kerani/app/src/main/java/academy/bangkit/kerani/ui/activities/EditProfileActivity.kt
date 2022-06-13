package academy.bangkit.kerani.ui.activities

import academy.bangkit.kerani.R
import academy.bangkit.kerani.data.User
import academy.bangkit.kerani.data.createTempFile
import academy.bangkit.kerani.data.rotateBitmap
import academy.bangkit.kerani.data.uriToFile
import academy.bangkit.kerani.databinding.ActivityEditProfileBinding
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.File

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Edit Profil"
        showLoading(false)
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        binding.photoProfile.setOnClickListener {
            showDialog()
        }
        binding.btnSave.setOnClickListener {
            showLoading(true)
            val name = binding.nameValue.text.toString()
            val bio = binding.bioValue.text.toString()
            val phone = binding.phoneValue.text.toString()
            val address = binding.addressValue.text.toString()

            val user = User(name, bio, phone, address)
            if (uid != null){
                databaseReference.child(uid).setValue(user).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showLoading(false)
                        Toast.makeText(this@EditProfileActivity, "Update Berhasil", Toast.LENGTH_SHORT).show()
                        Intent(this@EditProfileActivity, HomeActivity::class.java).also {
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                        }
                    } else {
                        showLoading(false)
                        Toast.makeText(this@EditProfileActivity, "Update Gagal", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.phoneValue.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    hideKeyboard()
                    true
                }
                else -> false
            }
        }

        binding.phoneValue.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    private fun showDialog(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.profile_sheet)
        val gallery = dialog.findViewById<ImageView>(R.id.gallery)
        gallery.setOnClickListener {
            startGallery()
            dialog.dismiss()
        }
        val camera = dialog.findViewById<ImageView>(R.id.camera)
        camera.setOnClickListener {
            startTakePhoto()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
       launcherIntentGallery.launch(chooser)
   }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@EditProfileActivity,
                "academy.bangkit.kerani",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@EditProfileActivity)
            getFile = myFile
            val isBackCamera = result.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val result1 = rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera)
            binding.photoProfile.setImageBitmap(result1)
        }
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val result2 =  BitmapFactory.decodeFile(myFile.path)
            binding.photoProfile.setImageBitmap(result2)
        }
    }
}