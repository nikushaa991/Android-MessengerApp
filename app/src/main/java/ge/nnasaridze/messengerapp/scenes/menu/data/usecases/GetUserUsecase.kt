package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.shared.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.repositories.users.DefaultUsersRepository

interface GetUserUsecase {
    fun execute(id: String, handler: (user: UserEntity) -> Unit)
}

class DefaultGetUserUsecase : GetUserUsecase {
    private val repo = DefaultUsersRepository()
    override fun execute(id: String, handler: (user: UserEntity) -> Unit) {
        repo.subscribeUser(id, handler)
    }
}