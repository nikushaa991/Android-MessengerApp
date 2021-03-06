package ge.nnasaridze.messengerapp.scenes.signup.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ge.nnasaridze.messengerapp.databinding.ActivitySignupBinding
import ge.nnasaridze.messengerapp.scenes.menu.presentation.MenuActivity

class SignupActivity : SignupView, AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var presenter: SignupPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        presenter = SignupPresenterImpl(this)

        with(binding) {
            signupSigninButton.setOnClickListener { presenter.signupPressed() }
        }
        supportActionBar?.hide()
        setContentView(binding.root)
    }

    override fun displayError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun getNickname(): String {
        return binding.signupNameText.text.toString()
    }

    override fun getPassword(): String {
        return binding.signupPassText.text.toString()
    }

    override fun getProfession(): String {
        return binding.signupProfText.text.toString()
    }

    override fun gotoMenu() {
        startActivity(Intent(this, MenuActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }

    override fun showLoading() {
        binding.signupPb.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.signupPb.visibility = View.GONE
    }
}