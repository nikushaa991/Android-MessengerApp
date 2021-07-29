package ge.nnasaridze.messengerapp.shared.repository.realtimedb.entities

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class MessageEntity(
    val sender: String? = null,
    val message: String? = null,
    val timestamp: Int? = null
)