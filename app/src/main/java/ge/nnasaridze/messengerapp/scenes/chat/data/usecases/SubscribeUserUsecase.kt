package ge.nnasaridze.messengerapp.scenes.chat.data.usecases

import ge.nnasaridze.messengerapp.shared.data.api.repositories.realtime.DefaultUsersRepository
import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity

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

    override fun execute(
        userID: String,
        onSuccessHandler: (user: UserEntity) -> Unit,
        errorHandler: (text: String) -> Unit,
    ) {
        repo.getAndListenUser(userID) { isSuccessful, user ->
            if (!isSuccessful) {
                errorHandler("Fetching user failed")
                return@getAndListenUser
            }
            onSuccessHandler(user)
        }
    }

    override fun destroy() {
        repo.cancelSubscriptions()
    }
}