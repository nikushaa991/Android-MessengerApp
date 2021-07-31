package ge.nnasaridze.messengerapp.shared.repositories.users

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserDTO(
    val nickname: String? = null,
    val profession: String? = null,
    val chatIDs: List<String>? = null,
    val imageID: Int? = null,
)