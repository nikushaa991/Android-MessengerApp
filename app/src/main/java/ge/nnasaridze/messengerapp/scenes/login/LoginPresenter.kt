package ge.nnasaridze.messengerapp.scenes.login

import ge.nnasaridze.messengerapp.shared.CREDENTIALS_ERROR
import ge.nnasaridze.messengerapp.shared.authenticator.Authenticator
import ge.nnasaridze.messengerapp.shared.isValidCredential

class LoginPresenterImpl(private val view: LoginView) : LoginPresenter {
    private val auth = Authenticator.getInstance()

    init {
        if (auth.isAuthenticated())
            view.gotoMenu()
    }

    override fun signinPressed() {
        val name = view.getNickname()
        val pass = view.getPassword()
        if (isValidCredential(name) == false ||isValidCredential(pass) == false) {
            view.displayError(CREDENTIALS_ERROR)
            return
        }
        view.showLoading()
        auth.authenticate(name, pass) { isSuccessful ->
            view.hideLoading()
            if (isSuccessful)
                view.gotoMenu()
            else
                view.displayError("Authentication failed")
        }
    }

    override fun signupPressed() {
        view.gotoSignup()
    }
}