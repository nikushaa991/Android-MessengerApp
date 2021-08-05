package ge.nnasaridze.messengerapp.scenes.signup.presentation

import ge.nnasaridze.messengerapp.scenes.signup.data.usecases.DefaultRegisterUsecase
import ge.nnasaridze.messengerapp.scenes.signup.data.usecases.RegisterUsecase


class SignupPresenterImpl(
    private val view: SignupView,
    private val registerUsecase: RegisterUsecase = DefaultRegisterUsecase(),
) : SignupPresenter {


    override fun signupPressed() {
        val name = view.getNickname()
        val pass = view.getPassword()
        val prof = view.getProfession()

        view.showLoading()
        registerUsecase.execute(name, pass, prof, ::onRegister)

    }

    private fun onRegister(isSuccessful: Boolean, error: String) {
        view.hideLoading()
        if (isSuccessful)
            view.gotoLogin()
        else {
            view.displayError(error)
        }
    }
}