package ge.nnasaridze.messengerapp.scenes.search.data.usecases

import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity
import ge.nnasaridze.messengerapp.shared.data.repositories.users.DefaultUsersRepository
import ge.nnasaridze.messengerapp.shared.utils.LAZY_LOADING_AMOUNT

interface GetUsersUsecase {
    fun execute(
        amount: Int,
        newUserHandler: (user: UserEntity) -> Unit,
        errorHandler: (text: String) -> Unit,
    )

    fun execute(
        nameQuery: String,
        newUserHandler: (user: UserEntity, query: String) -> Unit,
        errorHandler: (text: String) -> Unit,
    )
}

class DefaultGetUsersUsecase : GetUsersUsecase {


    private val repo = DefaultUsersRepository()

    override fun execute(
        amount: Int,
        newUserHandler: (user: UserEntity) -> Unit,
        errorHandler: (text: String) -> Unit,
    ) {
        repo.getUsers(
            from = amount - LAZY_LOADING_AMOUNT,
            to = amount) { isSuccessful, user ->
            if (!isSuccessful) {
                errorHandler("Fetching user failed")
                return@getUsers
            }
            newUserHandler(user)
        }
    }


    override fun execute(
        nameQuery: String,
        newUserHandler: (user: UserEntity, query: String) -> Unit,
        errorHandler: (text: String) -> Unit,
    ) {
        repo.getUsers(
            nameQuery = nameQuery) { isSuccessful, user ->
            if (!isSuccessful) {
                errorHandler("Fetching searched user failed")
                return@getUsers
            }
            newUserHandler(user, nameQuery)
        }
    }
}
