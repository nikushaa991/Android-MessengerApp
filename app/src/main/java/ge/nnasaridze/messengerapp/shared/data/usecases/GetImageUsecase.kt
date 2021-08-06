package ge.nnasaridze.messengerapp.shared.data.usecases

import android.net.Uri
import ge.nnasaridze.messengerapp.shared.data.repositories.pictures.DefaultPicturesRepository

interface GetImageUsecase {
    fun execute(id: String, handler: (uri: Uri) -> Unit)
}


class DefaultGetImageUsecase : GetImageUsecase {


    private val repo = DefaultPicturesRepository()

    override fun execute(id: String, handler: (uri: Uri) -> Unit) {
        val uri = repo.getPictureURL(id)
        if (uri != null)
            handler(uri)
    }

}