package ge.nnasaridze.messengerapp.scenes.login.presentation

import ge.nnasaridze.messengerapp.shared.utils.CREDENTIALS_ERROR
import ge.nnasaridze.messengerapp.shared.utils.isValidCredential
import ge.nnasaridze.messengerapp.shared.repositories.Repository

class LoginPresenterImpl(private val view: LoginView) : LoginPresenter {
    private val repo = Repository.getInstance()

    override fun signinPressed() {
        val name = view.getNickname()
        val pass = view.getPassword()

        if (isValidCredential(name) == false || isValidCredential(pass) == false) {
            view.displayError(CREDENTIALS_ERROR)
            return
        }

        view.showLoading()
        repo.authenticate(name, pass) { isSuccessful ->
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

    override fun viewInitialized() {
        if (repo.isAuthenticated())
            view.gotoMenu()
    }
}