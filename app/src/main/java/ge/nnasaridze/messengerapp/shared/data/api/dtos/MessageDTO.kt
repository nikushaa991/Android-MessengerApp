package ge.nnasaridze.messengerapp.shared.data.api.dtos

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import ge.nnasaridze.messengerapp.shared.data.entities.MessageEntity

@IgnoreExtraProperties
data class MessageDTO(
    val senderID: String? = null,
    val text: String? = null,
    val timestamp: Long? = null,
) {
    @Exclude
    fun toEntity(): MessageEntity {
        return MessageEntity(
            senderID ?: "",
            text ?: "",
            timestamp ?: 0,
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
