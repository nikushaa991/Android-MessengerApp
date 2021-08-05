package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.shared.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.repositories.users.DefaultUsersRepository

interface UpdateDataUsecase {
    fun execute(user: UserEntity)
}

class DefaultUpdateDataUsecase : UpdateDataUsecase {


    private val authRepo = DefaultAuthenticationRepository()
    private val usersRepo = DefaultUsersRepository()

    override fun execute(user: UserEntity) {
        TODO("Not yet implemented")
    }

}
