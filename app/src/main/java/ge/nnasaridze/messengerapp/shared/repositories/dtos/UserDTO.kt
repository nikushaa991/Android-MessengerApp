package ge.nnasaridze.messengerapp.shared.repositories.dtos

import com.google.firebase.database.IgnoreExtraProperties
import ge.nnasaridze.messengerapp.shared.entities.UserEntity

@IgnoreExtraProperties
data class UserDTO(
    val nickname: String? = null,
    val profession: String? = null,
    val chatIDs: List<String>? = null,
    val imageID: Int? = null,
)  {
    fun toEntity(): UserEntity {
        return UserEntity(
            nickname,
            profession,
            chatIDs,
            imageID)
    }
}