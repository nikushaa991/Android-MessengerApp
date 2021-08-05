package ge.nnasaridze.messengerapp.scenes.signup.data.usecases

import ge.nnasaridze.messengerapp.shared.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.repositories.authentication.AuthenticationRepository
import ge.nnasaridze.messengerapp.shared.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.repositories.users.DefaultUsersRepository
import ge.nnasaridze.messengerapp.shared.repositories.users.UsersRepository
import ge.nnasaridze.messengerapp.shared.utils.CREDENTIALS_ERROR

interface RegisterUsecase {
    fun execute(
        name: String,
        pass: String,
        prof: String,
        handler: (isSuccessful: Boolean, error: String) -> Unit,
    )
}

class DefaultRegisterUsecase : RegisterUsecase {

    private val authRepo: AuthenticationRepository = DefaultAuthenticationRepository()
    private val usersRepo: UsersRepository = DefaultUsersRepository()

    override fun execute(
        name: String,
        pass: String,
        prof: String,
        handler: (isSuccessful: Boolean, error: String) -> Unit,
    ) {
        if (!authRepo.isValidCredential(name) || !authRepo.isValidCredential(pass)) {
            handler(false, CREDENTIALS_ERROR)
            return
        }
        authRepo.register(name, pass) { isSuccessful ->
            if (!isSuccessful) {
                handler(false, "Registration error")
            } else {
                val entity = UserEntity(authRepo.getID(), name, prof)
                usersRepo.createUser(entity) {
                    handler(it, "")
                }
            }
        }
    }
}
