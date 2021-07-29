package ge.nnasaridze.messengerapp.shared.repository.realtimedb.entities

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ChatEntity(
    val user: UserEntity? = null,
    val lastMessage: MessageEntity? = null,
)