package ge.nnasaridze.messengerapp.scenes.search.data.usecases

import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.data.repositories.users.DefaultUsersRepository
import ge.nnasaridze.messengerapp.shared.utils.LAZY_LOADING_AMOUNT

interface GetUsersUsecase {
    fun execute(
        amount: Int = LAZY_LOADING_AMOUNT,
        nameQuery: String = "",
        newUserHandler: (UserEntity) -> Unit,
        errorHandler: (text: String) -> Unit,
    )
}

class DefaultGetUsersUsecase : GetUsersUsecase {


    private val repo = DefaultUsersRepository()

    override fun execute(
        amount: Int,
        nameQuery: String,
        newUserHandler: (UserEntity) -> Unit,
        errorHandler: (text: String) -> Unit,
    ) {
        repo.getUsers(
            from = amount - LAZY_LOADING_AMOUNT,
            to = amount,
            nameQuery = nameQuery) { isSuccessful, user ->
            if (!isSuccessful) {
                errorHandler("Fetching user failed")
                return@getUsers
            }
            newUserHandler(user)
        }
    }
}
