package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import android.net.Uri
import ge.nnasaridze.messengerapp.shared.data.repositories.pictures.DefaultPicturesRepository

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
        repo.getPictureURL(id) { isSuccessful, uri ->
            if (!isSuccessful)
                errorHandler("Image not found")
            onSuccessHandler(uri)
        }
    }
}
