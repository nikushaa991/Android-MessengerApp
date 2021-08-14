package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import android.net.Uri
import ge.nnasaridze.messengerapp.shared.data.api.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.data.api.repositories.storage.DefaultPicturesRepository

interface UploadImageUsecase {
    fun execute(
        uri: Uri,
        onCompleteHandler: () -> Unit,
        errorHandler: (text: String) -> Unit,
    )
}

class DefaultUploadImageUsecase : UploadImageUsecase {


    private val authRepo = DefaultAuthenticationRepository()
    private val imagesRepo = DefaultPicturesRepository()

    override fun execute(
        uri: Uri,
        onCompleteHandler: () -> Unit,
        errorHandler: (text: String) -> Unit,
    ) {
        val id = authRepo.getID()
        imagesRepo.uploadPicture(id, uri) { isSuccessful ->
            if (!isSuccessful)
                errorHandler("Image upload failed")
            onCompleteHandler()
        }
    }

}
