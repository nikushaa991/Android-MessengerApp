package ge.nnasaridze.messengerapp.scenes.menu.presentation.fragments.conversations.recycler

import android.net.Uri
import ge.nnasaridze.messengerapp.shared.data.entities.MessageEntity
import ge.nnasaridze.messengerapp.shared.data.entities.UserEntity

data class RecyclerChatEntity(
    var chatID: String,
    var user: UserEntity,
    var lastMessage: MessageEntity,
    var image: Uri,
)
