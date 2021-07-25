package ge.nnasaridze.messengerapp.scenes.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ge.nnasaridze.messengerapp.databinding.ActivityMainBinding
import ge.nnasaridze.messengerapp.scenes.menu.MenuActivity
import ge.nnasaridze.messengerapp.scenes.signup.SignupActivity

class MainActivity : LoginView, AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: LoginPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        presenter = LoginPresenterImpl(this)

        with(binding) {
            loginSigninButton.setOnClickListener { presenter.signinPressed() }
            loginSignupButton.setOnClickListener { presenter.signupPressed() }
        }
        supportActionBar?.hide()
        setContentView(binding.root)
    }

    override fun displayError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun getNickname(): String {
        return binding.loginName.text.toString()
    }

    override fun getPassword(): String {
        return binding.loginPassword.text.toString()

    }

    override fun gotoSignup() {
        startActivity(Intent(this, SignupActivity::class.java))
    }

    override fun gotoMenu() {
        startActivity(Intent(this, MenuActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_TASK_ON_HOME
        })
        this.finish();
    }

    override fun showLoading() {
        binding.loginPb.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.loginPb.visibility = View.GONE
    }
}