package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.data.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.repositories.users.DefaultUsersRepository

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
    private var isDestroyed = false
    private var subToken = -1

    override fun execute(
        onCompleteHandler: (user: UserEntity) -> Unit,
        errorHandler: (text: String) -> Unit,
    ) {
        usersRepo.getAndListenUser(currentID) { isSuccessful, user, subToken ->
            if (isDestroyed)
                return@getAndListenUser
            this.subToken = subToken
            if (!isSuccessful) {
                errorHandler("Fetching current user failed")
                return@getAndListenUser
            }
            onCompleteHandler(user)
        }
    }

    override fun destroy() {
        isDestroyed = true
        usersRepo.cancelSubscription(-1)
    }
}