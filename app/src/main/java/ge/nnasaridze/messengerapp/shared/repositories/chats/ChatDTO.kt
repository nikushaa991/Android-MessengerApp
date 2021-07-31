package ge.nnasaridze.messengerapp.shared.repositories.chats

import com.google.firebase.database.IgnoreExtraProperties
import ge.nnasaridze.messengerapp.shared.repositories.users.UserDTO

@IgnoreExtraProperties
data class ChatDTO(
    val user: UserDTO? = null,
    val lastMessage: MessageDTO? = null,
)