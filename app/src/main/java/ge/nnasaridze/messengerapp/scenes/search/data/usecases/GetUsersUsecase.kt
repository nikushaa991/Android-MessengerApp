package ge.nnasaridze.messengerapp.scenes.search.data.usecases

import ge.nnasaridze.messengerapp.scenes.search.presentation.recycler.RecyclerUserEntity
import ge.nnasaridze.messengerapp.shared.data.api.repositories.realtime.DefaultUsersRepository
import ge.nnasaridze.messengerapp.shared.data.api.repositories.storage.DefaultPicturesRepository
import ge.nnasaridze.messengerapp.shared.utils.LAZY_LOADING_AMOUNT

interface GetUsersUsecase {
    fun execute(
        amount: Int,
        newUserHandler: (user: RecyclerUserEntity) -> Unit,
        errorHandler: (text: String) -> Unit,
    )

    fun execute(
        nameQuery: String,
        newUserHandler: (user: RecyclerUserEntity, query: String) -> Unit,
        errorHandler: (text: String) -> Unit,
    )
}

class DefaultGetUsersUsecase : GetUsersUsecase {


    private val usersRepo = DefaultUsersRepository()
    private val picturesRepo = DefaultPicturesRepository()

    override fun execute(
        amount: Int,
        newUserHandler: (user: RecyclerUserEntity) -> Unit,
        errorHandler: (text: String) -> Unit,
    ) {
        usersRepo.getUsers(
            from = amount - LAZY_LOADING_AMOUNT,
            to = amount) { isSuccessful, user ->
            if (!isSuccessful) {
                errorHandler("Fetching user failed")
                return@getUsers
            }
            picturesRepo.getPictureURL(user.userID) { _, uri ->
                val recyclerEntity =
                    RecyclerUserEntity(user.userID, user.nickname, user.profession, uri)
                newUserHandler(recyclerEntity)
            }
        }
    }


    override fun execute(
        nameQuery: String,
        newUserHandler: (user: RecyclerUserEntity, query: String) -> Unit,
        errorHandler: (text: String) -> Unit,
    ) {
        usersRepo.getUsers(
            nameQuery = nameQuery) { isSuccessful, user ->
            if (!isSuccessful) {
                errorHandler("Fetching searched user failed")
                return@getUsers
            }
            picturesRepo.getPictureURL(user.userID) { _, uri ->
                val recyclerEntity =
                    RecyclerUserEntity(user.userID, user.nickname, user.profession, uri)
                newUserHandler(recyclerEntity, nameQuery)
            }
        }
    }

}
