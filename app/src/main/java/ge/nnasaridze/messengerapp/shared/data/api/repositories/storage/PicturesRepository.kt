package ge.nnasaridze.messengerapp.shared.data.api.repositories.storage

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


interface PicturesRepository {
    fun getPictureURL(
        id: String,
        handler: (isSuccessful: Boolean, uri: Uri) -> Unit,
    )

    fun uploadPicture(
        id: String,
        uri: Uri,
        handler: (isSuccessful: Boolean) -> Unit,
    )
}

class DefaultPicturesRepository : PicturesRepository {


    private val database = Firebase.storage.reference

    override fun getPictureURL(
        id: String,
        handler: (isSuccessful: Boolean, uri: Uri) -> Unit,
    ) {
        database.child(id).downloadUrl.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                handler(false, Uri.EMPTY)
                return@addOnCompleteListener
            }
            val result = task.result
            if (result == null)
                handler(false, Uri.EMPTY)
            else
                handler(true, result)
        }
    }


    override fun uploadPicture(
        id: String,
        uri: Uri,
        handler: (isSuccessful: Boolean) -> Unit,
    ) {
        database.child(id).putFile(uri).addOnCompleteListener { task ->
            handler(task.isSuccessful)
        }
    }
}