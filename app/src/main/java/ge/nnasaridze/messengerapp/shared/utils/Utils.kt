package ge.nnasaridze.messengerapp.shared.utils

import java.text.SimpleDateFormat
import java.util.*


const val DATABASE_URL =
    "https://messenger-app-c7d14-default-rtdb.europe-west1.firebasedatabase.app"


const val CREDENTIALS_ERROR = "Credentials must be alphanumeric and non-empty"
const val PASSWORD_ERROR = "Password must be alphanumeric and longer than 6 characters"

const val LAZY_LOADING_AMOUNT = 8

const val chatIDExtra = "chatIDExtra"
const val recipientIDExtra = "recipientIDExtra"

object FormatTime {
    private const val MINUTE = 60000L
    private const val HOUR = 3600000L
    private const val DAY = 86400000L

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
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(time)
    }
}

