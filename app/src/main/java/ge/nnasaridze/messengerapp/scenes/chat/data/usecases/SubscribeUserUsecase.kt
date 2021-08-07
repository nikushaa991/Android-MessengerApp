package ge.nnasaridze.messengerapp.scenes.chat.data.usecases

import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.data.repositories.users.DefaultUsersRepository

interface SubscribeUserUsecase {
    fun execute(
        userID: String,
        onSuccessHandler: (user: UserEntity) -> Unit,
        errorHandler: (text: String) -> Unit,
    )

    fun destroy()
}

class DefaultSubscribeUserUsecase : SubscribeUserUsecase {


    private val repo = DefaultUsersRepository()
    private var isDestroyed = false
    private var subToken = -1

    override fun execute(
        userID: String,
        onSuccessHandler: (user: UserEntity) -> Unit,
        errorHandler: (text: String) -> Unit,
    ) {
        repo.getAndListenUser(userID) { isSuccessful, user, subToken ->
            if (isDestroyed) {
                repo.cancelSubscription(subToken)
                return@getAndListenUser
            }
            this.subToken = subToken
            if (!isSuccessful) {
                errorHandler("Fetching user failed")
                return@getAndListenUser
            }
            onSuccessHandler(user)
        }
    }

    override fun destroy() {
        isDestroyed = true
        repo.cancelSubscription(subToken)
    }
}