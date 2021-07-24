package ge.nnasaridze.messengerapp.scenes.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ge.nnasaridze.messengerapp.R
import ge.nnasaridze.messengerapp.scenes.signup.LoginView

class MainActivity : LoginView, AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun displayError(text: String) {
        TODO("Not yet implemented")
    }

    override fun getNickname(): String {
        TODO("Not yet implemented")
    }

    override fun getPassword(): String {
        TODO("Not yet implemented")
    }

    override fun gotoSignup() {
        TODO("Not yet implemented")
    }

    override fun gotoMenu() {
        TODO("Not yet implemented")
    }

    override fun showLoading() {
        TODO("Not yet implemented")
    }

    override fun hideLoading() {
        TODO("Not yet implemented")
    }
}