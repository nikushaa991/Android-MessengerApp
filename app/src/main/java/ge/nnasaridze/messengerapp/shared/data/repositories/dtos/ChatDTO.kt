package ge.nnasaridze.messengerapp.shared.data.repositories.dtos

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import ge.nnasaridze.messengerapp.shared.data.entities.ChatEntity

@IgnoreExtraProperties
data class ChatDTO(
    val userIDs: List<String>,
    val lastMessageID: String? = null,
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userIDs" to userIDs,
            "lastMessageID" to lastMessageID,
        )
    }
}

