package academy.bangkit.kerani.ui.activities

import academy.bangkit.kerani.R
import academy.bangkit.kerani.customview.PasswordEditText
import academy.bangkit.kerani.databinding.ActivityRegisterBinding
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var inputText: PasswordEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        showLoading(false)

        auth = FirebaseAuth.getInstance()
        inputText = binding.inputPassword
        binding.btnSignup.setOnClickListener {

            val email = binding.inputEmail.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                val emailRequired = resources.getString(R.string.email_required)
                binding.inputEmail.error = emailRequired
                binding.inputEmail.requestFocus()
                return@setOnClickListener
            }
            registerUser(email, password)
            showLoading(true)
        }

        binding.inputPassword.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    hideKeyboard()
                    true
                }
                else -> false
            }
        }

        binding.inputPassword.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        binding.btnLogin.setOnClickListener {
            showLoading(true)
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ it ->
                if(it.isSuccessful){
                    Intent(this@RegisterActivity, HomeActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                } else {
                    showLoading(false)
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser != null){
            Intent(this@RegisterActivity, HomeActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
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

    private fun showLoading(loading: Boolean) {
        when(loading) {
            true -> binding.progressBar.visibility = View.VISIBLE
            false -> binding.progressBar.visibility = View.GONE
        }
    }
}