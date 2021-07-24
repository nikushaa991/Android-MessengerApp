package ge.nnasaridze.messengerapp.scenes.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ge.nnasaridze.messengerapp.R

class SignupActivity : SignupView, AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
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

    override fun getProfession(): String {
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