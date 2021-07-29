package ge.nnasaridze.messengerapp.shared.repository.realtimedb.entities

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserEntity(
    val nickname: String? = null,
    val profession: String? = null,
    val chatIDs: List<String>? = null,
    val imageID: Int? = null,
)