package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.data.api.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.api.repositories.users.DefaultUsersRepository

interface SubscribeCurrentUserUsecase {
    fun execute(
        onCompleteHandler: (user: UserEntity) -> Unit,
        errorHandler: (text: String) -> Unit,
    )

    fun destroy()
}

class DefaultSubscribeCurrentUserUsecase : SubscribeCurrentUserUsecase {

    private val usersRepo = DefaultUsersRepository()
    private val currentID = DefaultAuthenticationRepository().getID()

    override fun execute(
        onCompleteHandler: (user: UserEntity) -> Unit,
        errorHandler: (text: String) -> Unit,
    ) {
        usersRepo.getAndListenUser(currentID) { isSuccessful, user ->
            if (!isSuccessful) {
                errorHandler("Fetching current user failed")
                return@getAndListenUser
            }
            onCompleteHandler(user)
        }
    }

    override fun destroy() {
        usersRepo.cancelSubscriptions()
    }
}