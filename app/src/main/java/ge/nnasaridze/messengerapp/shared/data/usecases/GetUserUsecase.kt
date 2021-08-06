package ge.nnasaridze.messengerapp.shared.data.usecases

import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.data.repositories.users.DefaultUsersRepository

interface GetUserUsecase {
    fun execute(id: String, handler: (user: UserEntity) -> Unit)
}

class DefaultGetUserUsecase : GetUserUsecase {
    private val repo = DefaultUsersRepository()
    override fun execute(id: String, handler: (user: UserEntity) -> Unit) {
        repo.subscribeUser(id, handler)
    }
}