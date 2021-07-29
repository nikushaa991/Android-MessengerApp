package ge.nnasaridze.messengerapp.scenes.signup

import ge.nnasaridze.messengerapp.shared.CREDENTIALS_ERROR
import ge.nnasaridze.messengerapp.shared.repository.auth.Authenticator
import ge.nnasaridze.messengerapp.shared.isValidCredential


class SignupPresenterImpl(private val view: SignupView) : SignupPresenter {


    private val auth = Authenticator.getInstance()

    override fun signupPressed() {
        val name = view.getNickname()
        val pass = view.getPassword()
        val prof = view.getProfession()

        if (isValidCredential(name) == false ||
            isValidCredential(pass) == false ||
            isValidCredential(prof) == false
        ) {
            view.displayError(CREDENTIALS_ERROR)
            return
        }

        view.showLoading()
        auth.register(name, pass, prof) { isSuccessful ->
            view.hideLoading()
            if (isSuccessful)
                view.gotoLogin()
            else
                view.displayError("Registration failed")
        }
    }

    //TODO: PROFESSION
}