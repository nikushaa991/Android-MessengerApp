package ge.nnasaridze.messengerapp.shared.repositories.dtos

import com.google.firebase.database.IgnoreExtraProperties
import ge.nnasaridze.messengerapp.shared.entities.MessageEntity

@IgnoreExtraProperties
data class MessageDTO(
    val sender: String? = null,
    val message: String? = null,
    val timestamp: Int? = null
) {
    fun toEntity(): MessageEntity {
        return MessageEntity(
            sender,
            message,
            timestamp,
        )
    }
}