package ge.nnasaridze.messengerapp.shared

const val EMAIL_SUFFIX = "@fake.fake"
const val UNREGISTERED = "UNREGISTERED"

const val CREDENTIALS_ERROR = "Credentials must be alphanumeric and non-empty"

fun isValidCredential(credential: String): Any {
    return credential.isNotEmpty() &&
            credential.filter { it in 'A'..'Z' || it in 'a'..'z' || it in '0'..'9' }
                .length == credential.length
}