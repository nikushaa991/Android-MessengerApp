package ge.nnasaridze.messengerapp.shared.data.repositories.pictures

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


interface PicturesRepository {
    fun getPictureURL(id: String): Uri?

    fun uploadPicture(
        id: String,
        uri: Uri,
        handler: (isSuccessful: Boolean) -> Unit,
    )
}

class DefaultPicturesRepository : PicturesRepository {

    private val database = Firebase.storage.reference


    override fun getPictureURL(id: String): Uri? {
        return database.child(id).downloadUrl.result
    }


    override fun uploadPicture(id: String, uri: Uri, handler: (isSuccessful: Boolean) -> Unit) {
        database.child(id).putFile(uri).addOnCompleteListener { task ->
            handler(task.isSuccessful)
        }
    }
}