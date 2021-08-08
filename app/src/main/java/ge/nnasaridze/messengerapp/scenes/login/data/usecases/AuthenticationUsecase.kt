package ge.nnasaridze.messengerapp.scenes.login.data.usecases

import ge.nnasaridze.messengerapp.shared.data.repositories.authentication.AuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.utils.CREDENTIALS_ERROR

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
        if (!repo.isValidCredential(name) || !repo.isValidCredential(pass)) {
            errorHandler(CREDENTIALS_ERROR)
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