package at.co.boris.secuton.driverutil

import java.security.MessageDigest

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digested = md.digest(toByteArray())
    return digested.joinToString("") {
        String.format("%02x", it)
    }
}


fun String.toBoolean(): Boolean {
    return when (toLowerCase()) {
        "true", "ja", "yes" -> true
        "false", "nein", "no" -> false
        else -> return false
    }
}

