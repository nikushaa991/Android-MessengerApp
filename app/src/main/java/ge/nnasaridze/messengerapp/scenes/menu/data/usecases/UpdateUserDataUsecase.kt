package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.data.repositories.users.DefaultUsersRepository

interface UpdateUserDataUsecase {
    fun execute(
        user: UserEntity,
        handler: (isSuccessful: Boolean) -> Unit,
    )
}

class DefaultUpdateUserDataUsecase : UpdateUserDataUsecase {


    private val usersRepo = DefaultUsersRepository()

    override fun execute(
        user: UserEntity,
        handler: (isSuccessful: Boolean) -> Unit,
    ) {
        usersRepo.updateUser(user, handler)
    }

}
