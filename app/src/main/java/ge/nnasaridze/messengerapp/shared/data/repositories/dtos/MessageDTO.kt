package ge.nnasaridze.messengerapp.shared.data.repositories.dtos

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import ge.nnasaridze.messengerapp.shared.data.entities.MessageEntity

@IgnoreExtraProperties
data class MessageDTO(
    val senderID: String,
    val text: String,
    val timestamp: Long,
) {
    @Exclude
    fun toEntity(): MessageEntity {
        return MessageEntity(
            senderID,
            text,
            timestamp,
        )
    }

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "senderID" to senderID,
            "text" to text,
            "timestamp" to timestamp,
        )
    }
}
