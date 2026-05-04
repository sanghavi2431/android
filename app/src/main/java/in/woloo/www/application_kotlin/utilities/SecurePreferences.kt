package `in`.woloo.www.application_kotlin.utilities

import android.os.Build
import androidx.annotation.RequiresApi
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import java.util.Base64
import javax.crypto.spec.IvParameterSpec


object SecurePreferences {

    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"


    @RequiresApi(Build.VERSION_CODES.O)
    @JvmStatic
    fun encrypt(text: String, key: SecretKey): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val iv = ByteArray(16) // Generate a secure random IV in production
        cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
        val encryptedBytes = cipher.doFinal(text.toByteArray())
        return Base64.getEncoder().encodeToString(iv + encryptedBytes) // Prepend IV to encrypted data
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @JvmStatic
    fun decrypt(encryptedText: String, key: SecretKey): String {
        val decodedBytes = Base64.getDecoder().decode(encryptedText)
        val iv = decodedBytes.copyOfRange(0, 16) // Extract IV
        val encryptedBytes = decodedBytes.copyOfRange(16, decodedBytes.size)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }

    @JvmStatic
    fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(ALGORITHM)
        keyGenerator.init(128) // Key size
        return keyGenerator.generateKey()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @JvmStatic
    fun getKeyFromString(keyString: String): SecretKey {
        val keyBytes = Base64.getDecoder().decode(keyString)
        return SecretKeySpec(keyBytes, ALGORITHM)
    }

}