package ge.nnasaridze.messengerapp.scenes.signup.presentation

import ge.nnasaridze.messengerapp.scenes.signup.data.usecases.DefaultRegisterUsecase
import ge.nnasaridze.messengerapp.scenes.signup.data.usecases.RegisterUsecase


class SignupPresenterImpl(
    private val view: SignupView,
    private val registerUsecase: RegisterUsecase = DefaultRegisterUsecase(),
) : SignupPresenter {


    override fun signupPressed() {
        view.showLoading()
        registerUsecase.execute(
            name = view.getNickname(),
            pass = view.getPassword(),
            prof = view.getProfession(),
            onSuccessHandler = ::onSuccess,
            errorHandler = ::errorHandler)
    }

    private fun onSuccess() {
        view.hideLoading()
        view.gotoMenu()
    }

    private fun errorHandler(text: String) {
        view.hideLoading()
        view.displayError(text)
    }
}