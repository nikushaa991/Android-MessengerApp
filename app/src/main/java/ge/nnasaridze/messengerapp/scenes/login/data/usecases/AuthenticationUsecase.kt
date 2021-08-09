package ge.nnasaridze.messengerapp.scenes.login.data.usecases

import ge.nnasaridze.messengerapp.shared.data.api.repositories.authentication.AuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.api.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.utils.CREDENTIALS_ERROR
import ge.nnasaridze.messengerapp.shared.utils.PASSWORD_ERROR

interface AuthenticationUsecase {
    fun execute(
        name: String,
        pass: String,
        onSuccessHandler: () -> Unit,
        errorHandler: (text: String) -> Unit,
    )
}

class DefaultAuthenticationUsecase : AuthenticationUsecase {


    private val repo: AuthenticationRepository = DefaultAuthenticationRepository()

    override fun execute(
        name: String,
        pass: String,
        onSuccessHandler: () -> Unit,
        errorHandler: (text: String) -> Unit,
    ) {
        if (!repo.isValidCredential(name)) {
            errorHandler(CREDENTIALS_ERROR)
            return
        }
        if (!repo.isValidPassword(pass)) {
            errorHandler(PASSWORD_ERROR)
            return
        }
        repo.authenticate(name, pass) { isSuccessful ->
            if (!isSuccessful) {
                errorHandler("Authentication failed")
                return@authenticate
            }
            onSuccessHandler()
        }
    }
}