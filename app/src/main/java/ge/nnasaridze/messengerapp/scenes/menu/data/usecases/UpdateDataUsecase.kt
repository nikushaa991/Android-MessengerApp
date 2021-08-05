package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.shared.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.repositories.users.DefaultUsersRepository

interface UpdateDataUsecase {
    fun execute(
        user: UserEntity,
        handler: (isSuccessful: Boolean) -> Unit,
    )
}

class DefaultUpdateDataUsecase : UpdateDataUsecase {


    private val usersRepo = DefaultUsersRepository()

    override fun execute(
        user: UserEntity,
        handler: (isSuccessful: Boolean) -> Unit,
    ) {
        usersRepo.updateUser(user, handler)
    }

}
