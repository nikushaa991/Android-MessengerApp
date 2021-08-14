package ge.nnasaridze.messengerapp.scenes.signup.data.usecases

import ge.nnasaridze.messengerapp.shared.data.api.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.api.repositories.realtime.DefaultUsersRepository
import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.utils.CREDENTIALS_ERROR
import ge.nnasaridze.messengerapp.shared.utils.PASSWORD_ERROR

interface RegisterUsecase {
    fun execute(
        name: String,
        pass: String,
        prof: String,
        onSuccessHandler: () -> Unit,
        errorHandler: (error: String) -> Unit,
    )
}

class DefaultRegisterUsecase : RegisterUsecase {

    private val authRepo = DefaultAuthenticationRepository()
    private val usersRepo = DefaultUsersRepository()

    override fun execute(
        name: String,
        pass: String,
        prof: String,
        onSuccessHandler: () -> Unit,
        errorHandler: (error: String) -> Unit,
    ) {
        if (!authRepo.isValidCredential(name)) {
            errorHandler(CREDENTIALS_ERROR)
            return
        }
        if (!authRepo.isValidPassword(pass)) {
            errorHandler(PASSWORD_ERROR)
            return
        }

        authRepo.register(name, pass) { authIsSuccessful ->
            if (!authIsSuccessful) {
                errorHandler("Authentication registration error")
                return@register
            }

            val entity = UserEntity(authRepo.getID(), name, prof)
            usersRepo.createUser(entity) { userIsSuccessful ->
                if (!userIsSuccessful) {
                    errorHandler("User registration error")
                    return@createUser
                }

                onSuccessHandler()
            }
        }
    }
}
