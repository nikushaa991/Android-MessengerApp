package ge.nnasaridze.messengerapp.shared.utils

import java.text.SimpleDateFormat
import java.util.*

const val BATMAN_PATH =
    "android.resource://ge.nnasaridze.messengerapp/drawable/avatar_image_placeholder.png"

const val EMAIL_SUFFIX = "@fake.fake"
const val UNREGISTERED = "UNREGISTERED"

const val CREDENTIALS_ERROR = "Credentials must be alphanumeric and non-empty"

const val LAZY_LOADING_AMOUNT = 10

const val HOUR = 3600000L
const val DAY = 86400000L

fun formatChatTime(time: Long): String {
    val curr = System.currentTimeMillis()
    val diff = curr - time
    return when {
        diff < HOUR -> "${diff % HOUR} mins"
        diff < DAY -> "${diff % DAY} hours"
        else -> SimpleDateFormat("d MMM", Locale.getDefault()).format(time)
    }
}

fun formatMessageTime(time: Long): String {
    val hour = time % (1000 * 60 * 60 * 24)
    val minute = time % (1000 * 60 * 60)
    return "$hour:$minute"
}