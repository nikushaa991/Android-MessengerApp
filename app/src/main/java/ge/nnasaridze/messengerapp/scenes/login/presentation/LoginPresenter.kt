package ge.nnasaridze.messengerapp.scenes.login.presentation

import ge.nnasaridze.messengerapp.scenes.login.data.usecases.AuthenticationUsecase
import ge.nnasaridze.messengerapp.scenes.login.data.usecases.DefaultAuthenticationUsecase
import ge.nnasaridze.messengerapp.scenes.login.data.usecases.DefaultIsAuthenticatedUsecase
import ge.nnasaridze.messengerapp.scenes.login.data.usecases.IsAuthenticatedUsecase

class LoginPresenterImpl(
    private val view: LoginView,
    private val authenticationUsecase: AuthenticationUsecase = DefaultAuthenticationUsecase(),
    private val isAuthenticatedUsecase: IsAuthenticatedUsecase = DefaultIsAuthenticatedUsecase(),
) : LoginPresenter {

    override fun viewInitialized() {
        isAuthenticatedUsecase.execute { isAuthenticated ->
            if (isAuthenticated)
                onAuthentication()
        }
    }

    override fun signinPressed() {
        view.showLoading()
        authenticationUsecase.execute(
            name = view.getNickname(),
            pass = view.getPassword(),
            onSuccessHandler = ::onSuccess,
            errorHandler = ::errorHandler)

    }

    override fun signupPressed() {
        view.gotoSignup()
    }

    private fun onAuthentication() {
        view.gotoMenu()
    }

    private fun onSuccess() {
        view.hideLoading()
        onAuthentication()
    }

    private fun errorHandler(text: String) {
        view.hideLoading()
        view.displayError(text)
    }
}