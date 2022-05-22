package academy.bangkit.kerani.ui

import academy.bangkit.kerani.R
import academy.bangkit.kerani.customview.PasswordEditText
import academy.bangkit.kerani.databinding.ActivityLoginBinding
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

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var inputText: PasswordEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        showLoading(false)
        inputText = binding.inputPassword
        binding.btnLogin.setOnClickListener {
            val email = binding.inputEmail.text.toString().trim()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                val emailRequired = resources.getString(R.string.email_required)
                binding.inputEmail.error = emailRequired
                binding.inputEmail.requestFocus()
                return@setOnClickListener
            }
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

        binding.btnSignup.setOnClickListener {
            showLoading(true)
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
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