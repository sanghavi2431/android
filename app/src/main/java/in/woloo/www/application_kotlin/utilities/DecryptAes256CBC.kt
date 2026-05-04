package `in`.woloo.www.application_kotlin.utilities

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class DecryptAes256CBC {


    companion object {
        fun decryptAes256CBC(encryptedData: String, secretKey: String): String {
            return try {
                val parts = encryptedData.split(":")
                val ivHex = parts[0]
                val encryptedHex = parts[1]

                val ivBytes = hexStringToByteArray(ivHex)
                val encryptedBytes = hexStringToByteArray(encryptedHex)

                val keyBytes = secretKey.toByteArray(Charsets.UTF_8)
                val secretKeySpec = SecretKeySpec(keyBytes, "AES")
                val ivSpec = IvParameterSpec(ivBytes)

                val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec)

                val original = cipher.doFinal(encryptedBytes)
                String(original, Charsets.UTF_8)
            } catch (e: Exception) {
                e.printStackTrace()
                "Decryption failed: ${e.message}"
            }
        }

        private fun hexStringToByteArray(s: String): ByteArray {
            val len = s.length
            val result = ByteArray(len / 2)
            for (i in 0 until len step 2) {
                result[i / 2] = ((Character.digit(s[i], 16) shl 4)
                        + Character.digit(s[i + 1], 16)).toByte()
            }
            return result
        }
    }
}