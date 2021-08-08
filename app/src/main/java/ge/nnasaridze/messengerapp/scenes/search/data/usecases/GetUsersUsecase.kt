package ge.nnasaridze.messengerapp.scenes.search.data.usecases

import android.net.Uri
import ge.nnasaridze.messengerapp.scenes.search.presentation.recycler.RecyclerUserEntity
import ge.nnasaridze.messengerapp.shared.data.repositories.pictures.DefaultPicturesRepository
import ge.nnasaridze.messengerapp.shared.data.repositories.users.DefaultUsersRepository
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
            val pictureUri = picturesRepo.getPictureURL(user.userID)
            if (pictureUri == null) {
                errorHandler("Fetching image failed")
                return@getUsers
            }

            val recyclerEntity =
                RecyclerUserEntity(user.userID, user.nickname, user.profession, pictureUri)
            newUserHandler(recyclerEntity)
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
            val pictureUri = picturesRepo.getPictureURL(user.userID) ?: Uri.EMPTY
            if (pictureUri == Uri.EMPTY) {
                errorHandler("Fetching image failed")
            }

            val recyclerEntity =
                RecyclerUserEntity(user.userID, user.nickname, user.profession, pictureUri)
            newUserHandler(recyclerEntity, nameQuery)
        }
    }
}
