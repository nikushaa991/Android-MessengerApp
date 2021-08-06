package ge.nnasaridze.messengerapp.scenes.login.presentation

import ge.nnasaridze.messengerapp.shared.data.repositories.authentication.AuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.utils.CREDENTIALS_ERROR

class LoginPresenterImpl(
    private val view: LoginView,
    private val repo: AuthenticationRepository = DefaultAuthenticationRepository(),
) : LoginPresenter {


    override fun viewInitialized() {
        if (repo.isAuthenticated())
            view.gotoMenu()
    }

    override fun signinPressed() {
        val name = view.getNickname()
        val pass = view.getPassword()

        if (!repo.isValidCredential(name) || !repo.isValidCredential(pass)) {
            view.displayError(CREDENTIALS_ERROR)
            return
        }

        view.showLoading()
        repo.authenticate(name, pass, ::onAuthentication)
    }

    override fun signupPressed() {
        view.gotoSignup()
    }

    private fun onAuthentication(isSuccessful: Boolean) {
        view.hideLoading()
        if (isSuccessful)
            view.gotoMenu()
        else
            view.displayError("Authentication failed")
    }

}