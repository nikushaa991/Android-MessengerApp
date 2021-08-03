package ge.nnasaridze.messengerapp.scenes.signup

import ge.nnasaridze.messengerapp.shared.utils.CREDENTIALS_ERROR
import ge.nnasaridze.messengerapp.shared.repositories.authentication.DefaultAuthenticationRepository


class SignupPresenterImpl(private val view: SignupView) : SignupPresenter {


    private val repo = DefaultAuthenticationRepository()

    override fun signupPressed() {
        val name = view.getNickname()
        val pass = view.getPassword()
        val prof = view.getProfession()

        if (!repo.isValidCredential(name) || !repo.isValidCredential(pass)) {
            view.displayError(CREDENTIALS_ERROR)
            return
        }

        view.showLoading()
        repo.register(name, pass, prof) { isSuccessful ->
            view.hideLoading()
            if (isSuccessful)
                view.gotoLogin()
            else
                view.displayError("Registration failed")
        }
    }

    //TODO: PROFESSION
}