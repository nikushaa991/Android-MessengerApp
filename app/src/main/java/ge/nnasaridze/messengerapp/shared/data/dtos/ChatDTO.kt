package ge.nnasaridze.messengerapp.shared.data.dtos

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class ChatDTO(
    val userIDs: Map<String, String>? = null,
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

