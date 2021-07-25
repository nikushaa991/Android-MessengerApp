package ge.nnasaridze.messengerapp.scenes.login

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ge.nnasaridze.messengerapp.shared.EMAIL_SUFFIX

class LoginPresenterImpl(private val view: LoginView) : LoginPresenter {
    private val auth = Firebase.auth

    init {
        if (auth.currentUser != null)
            view.gotoMenu()
    }

    override fun signinPressed() {
        val name = view.getNickname()
        val pass = view.getPassword()
        if (isValid(name, pass) == false) {
            view.displayError("Credentials must be alphanumeric and non-empty")
            return
        }
        view.showLoading()
        auth.signInWithEmailAndPassword("$name$EMAIL_SUFFIX", pass)
            .addOnCompleteListener { task ->
                view.hideLoading()
                if (task.isSuccessful)
                    view.gotoMenu()
                else
                    view.displayError("Authentication failed")
            }
    }

    override fun signupPressed() {
        view.gotoSignup()
    }

    private fun isValid(name: String, pass: String): Any {
        return name.isNotEmpty() && pass.isNotEmpty() &&
                (name + pass).filter { it in 'A'..'Z' || it in 'a'..'z' || it in '0'..'9' }
                    .length == (name + pass).length
    }
}