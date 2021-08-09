package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import android.net.Uri
import ge.nnasaridze.messengerapp.shared.data.api.repositories.pictures.DefaultPicturesRepository

interface GetImageUsecase {
    fun execute(
        id: String,
        onSuccessHandler: (uri: Uri) -> Unit,
        errorHandler: (text: String) -> Unit,
    )
}


class DefaultGetImageUsecase : GetImageUsecase {


    private val repo = DefaultPicturesRepository()

    override fun execute(
        id: String,
        onSuccessHandler: (uri: Uri) -> Unit,
        errorHandler: (text: String) -> Unit,
    ) {
        repo.getPictureURL(id) { _, uri ->
            onSuccessHandler(uri)
        }
    }
}
