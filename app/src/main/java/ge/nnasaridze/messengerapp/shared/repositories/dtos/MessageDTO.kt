package ge.nnasaridze.messengerapp.shared.repositories.dtos

import com.google.firebase.database.IgnoreExtraProperties
import ge.nnasaridze.messengerapp.shared.entities.MessageEntity

@IgnoreExtraProperties
data class MessageDTO(
    val senderID: String,
    val text: String,
    val timestamp: Int,
) {
    fun toEntity(messageID: String): MessageEntity {
        return MessageEntity(
            senderID,
            text,
            timestamp,
        )
    }
}