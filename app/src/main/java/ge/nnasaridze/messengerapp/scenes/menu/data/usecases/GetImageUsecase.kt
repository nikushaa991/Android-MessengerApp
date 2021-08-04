package ge.nnasaridze.messengerapp.scenes.menu.data.usecases

import android.net.Uri
import ge.nnasaridze.messengerapp.shared.repositories.pictures.DefaultPicturesRepository

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