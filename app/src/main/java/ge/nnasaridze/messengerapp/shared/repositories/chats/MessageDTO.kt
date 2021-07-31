package ge.nnasaridze.messengerapp.shared.repositories.chats

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class MessageDTO(
    val sender: String? = null,
    val message: String? = null,
    val timestamp: Int? = null
)