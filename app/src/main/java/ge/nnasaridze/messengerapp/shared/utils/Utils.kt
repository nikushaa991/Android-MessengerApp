package ge.nnasaridze.messengerapp.shared.utils

import java.text.SimpleDateFormat
import java.util.*


const val DATABASE_URL =
    "https://messenger-app-c7d14-default-rtdb.europe-west1.firebasedatabase.app"

const val EMAIL_SUFFIX = "@fake.fake"
const val UNREGISTERED = "UNREGISTERED"

const val CREDENTIALS_ERROR = "Credentials must be alphanumeric and non-empty"

const val LAZY_LOADING_AMOUNT = 10

const val MINUTE = 60000L
const val HOUR = 3600000L
const val DAY = 86400000L

fun formatChatTime(time: Long): String {
    val curr = System.currentTimeMillis()
    val diff = curr - time
    return when {
        diff < HOUR -> "${(diff % HOUR) / MINUTE} mins"
        diff < DAY -> "${(diff % DAY) / HOUR} hours"
        else -> SimpleDateFormat("d MMM", Locale.getDefault()).format(time)
    }
}

fun formatMessageTime(time: Long): String {
    val hour = (time % DAY) / HOUR
    val minute = (time % HOUR) / MINUTE
    return "$hour:$minute"
}