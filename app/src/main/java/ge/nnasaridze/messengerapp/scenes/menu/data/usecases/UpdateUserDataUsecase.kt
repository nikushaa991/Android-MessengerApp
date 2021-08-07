package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.data.repositories.users.DefaultUsersRepository

interface UpdateUserDataUsecase {
    fun execute(
        user: UserEntity,
        onCompleteHandler: () -> Unit, errorHandler: (text: String) -> Unit,
    )
}

class DefaultUpdateUserDataUsecase : UpdateUserDataUsecase {


    private val usersRepo = DefaultUsersRepository()

    override fun execute(
        user: UserEntity,
        onCompleteHandler: () -> Unit,
        errorHandler: (text: String) -> Unit,
    ) {
        usersRepo.updateUser(user) { isSuccessful ->
            if (!isSuccessful)
                errorHandler("User data update failed")
            onCompleteHandler()
        }
    }

}
