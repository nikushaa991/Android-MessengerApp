package ge.nnasaridze.messengerapp.scenes.signup.data.usecases

import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.data.repositories.authentication.AuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.repositories.users.DefaultUsersRepository
import ge.nnasaridze.messengerapp.shared.data.repositories.users.UsersRepository
import ge.nnasaridze.messengerapp.shared.utils.CREDENTIALS_ERROR

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

    private val authRepo: AuthenticationRepository = DefaultAuthenticationRepository()
    private val usersRepo: UsersRepository = DefaultUsersRepository()

    override fun execute(
        name: String,
        pass: String,
        prof: String,
        onSuccessHandler: () -> Unit,
        errorHandler: (error: String) -> Unit,
    ) {
        if (!authRepo.isValidCredential(name) || !authRepo.isValidCredential(pass)) {
            errorHandler(CREDENTIALS_ERROR)
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
