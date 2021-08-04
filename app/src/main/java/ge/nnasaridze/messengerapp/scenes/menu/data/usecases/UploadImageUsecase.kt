package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import android.net.Uri
import ge.nnasaridze.messengerapp.shared.repositories.authentication.DefaultAuthenticationRepository
import ge.nnasaridze.messengerapp.shared.repositories.pictures.DefaultPicturesRepository

interface UploadImageUsecase {
    fun execute(uri: Uri, handler: (isSuccessful: Boolean) -> Unit)
}

class DefaultUploadImageUsecase : UploadImageUsecase {


    private val authRepo = DefaultAuthenticationRepository()
    private val imagesRepo = DefaultPicturesRepository()

    override fun execute(uri: Uri, handler: (isSuccessful: Boolean) -> Unit) {
        val id = authRepo.getID()
        imagesRepo.uploadPicture(id, uri, handler)
    }

}
