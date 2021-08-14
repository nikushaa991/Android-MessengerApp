package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.shared.data.api.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.api.repositories.realtime.DefaultUsersRepository
import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity

interface UpdateUserDataUsecase {
    fun execute(
        user: UserEntity,
        onCompleteHandler: () -> Unit,
        errorHandler: (text: String) -> Unit,
    )
}

class DefaultUpdateUserDataUsecase : UpdateUserDataUsecase {


    private val usersRepo = DefaultUsersRepository()
    private val authRepo = DefaultAuthenticationRepository()

    override fun execute(
        user: UserEntity,
        onCompleteHandler: () -> Unit,
        errorHandler: (text: String) -> Unit,
    ) {
        authRepo.updateName(user.nickname) { isSuccessful ->
            if (!isSuccessful) {
                errorHandler("Auth data update failed")
                return@updateName
            }
            usersRepo.updateUser(user) { userIsSuccessful ->
                if (!userIsSuccessful)
                    errorHandler("User data update failed")
                onCompleteHandler()
            }
        }
    }

}
