package `in`.woloo.www.application_kotlin.database

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.common.CommonUtils
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.SecureRandom
import java.security.SignatureException
import java.security.UnrecoverableEntryException
import java.security.cert.CertificateException
import java.security.interfaces.RSAPublicKey
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.IllegalBlockSizeException
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey

class EnCryptor {
    lateinit var encryption: ByteArray
        private set
    lateinit var iv: ByteArray
        private set
    private var keyStore: KeyStore? = null

    init {
        try {
            initKeyStore()
        } catch (e: KeyStoreException) {
            CommonUtils.printStackTrace(e)
        } catch (e: CertificateException) {
            CommonUtils.printStackTrace(e)
        } catch (e: NoSuchAlgorithmException) {
            CommonUtils.printStackTrace(e)
        } catch (e: IOException) {
            CommonUtils.printStackTrace(e)
        }
    }

    @Throws(
        KeyStoreException::class,
        CertificateException::class,
        NoSuchAlgorithmException::class,
        IOException::class
    )
    private fun initKeyStore() {
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore?.load(null)
    }

    @Throws(
        UnrecoverableEntryException::class,
        NoSuchAlgorithmException::class,
        KeyStoreException::class,
        NoSuchProviderException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IOException::class,
        InvalidAlgorithmParameterException::class,
        SignatureException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class
    )
    fun encryptText(context: Context, alias: String, textToEncrypt: String): ByteArray {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias))
        iv = cipher.iv
        val mEx =
            cipher.doFinal(textToEncrypt.toByteArray(charset("UTF-8"))).also { encryption = it }
        setProperty(context, alias, iv, String(mEx, StandardCharsets.ISO_8859_1))
        return mEx
    }

    private fun setProperty(mContext: Context, key: String, iv: ByteArray, encryptedValue: String) {

//        String ivString = Base64.encodeToString(iv, Base64.DEFAULT);
        var mContext: Context? = mContext
        val ivString = String(iv, StandardCharsets.ISO_8859_1)
        if (mContext == null) {
            mContext = WolooApplication.instance!!
        }
        val sp = getPreferenceModePrivate(mContext!!, key + "_data")
        val editor = sp.edit()
        editor.putString(key, encryptedValue)
        editor.apply()
        val sp1 = getPreferenceModePrivate(mContext!!, key + "_iv")
        val editor1 = sp1.edit()
        editor1.putString(key + "_iv", ivString)
        editor1.apply()
    }

    private fun getPreferenceModePrivate(mContext: Context, key: String): SharedPreferences {
        return mContext.getSharedPreferences(key, Context.MODE_PRIVATE)
    }

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchProviderException::class,
        InvalidAlgorithmParameterException::class
    )
    private fun getSecretKey(alias: String): SecretKey {
        var keyGenerator: KeyGenerator? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenerator = KeyGenerator
                .getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenerator!!.init(
                KeyGenParameterSpec.Builder(
                    alias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            )
        } else {
//            keyGenerator = KeyGenerator.getInstance(ANDROID_KEY_STORE);
            // or something like
            keyGenerator = KeyGenerator.getInstance("RSA/ECB/PKCS1Padding", ANDROID_KEY_STORE)

            // use the supported init method here such as this one: https://developer.android.com/reference/javax/crypto/KeyGenerator.html#init(int, java.security.SecureRandom)
            keyGenerator.init(128, secureRandom)
        }
        return keyGenerator!!.generateKey()
    }

    private val secureRandom: SecureRandom?
        private get() {
            var secureRandom1: SecureRandom? = null
            try {
                // Create a secure random number generator using the SHA1PRNG algorithm
                val secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG")
                // Get 128 random bytes
                val randomBytes = ByteArray(128)
                secureRandomGenerator.nextBytes(randomBytes)

                // Create two secure number generators with the same seed
                val seedByteCount = 5
                val seed = secureRandomGenerator.generateSeed(seedByteCount)
                secureRandom1 = SecureRandom.getInstance("SHA1PRNG")
                secureRandom1.setSeed(seed)
            } catch (e: NoSuchAlgorithmException) {
                CommonUtils.printStackTrace(e)
            }
            return secureRandom1
        }

    fun encryptString(alias: String?, initialText: String) {
        try {
            val privateKeyEntry = keyStore!!.getEntry(alias, null) as KeyStore.PrivateKeyEntry
            val publicKey = privateKeyEntry.certificate.publicKey as RSAPublicKey

            // Encrypt the text
            if (initialText.isEmpty()) {
                return
            }
            val input = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL")
            input.init(Cipher.ENCRYPT_MODE, publicKey)
            val outputStream = ByteArrayOutputStream()
            val cipherOutputStream = CipherOutputStream(
                outputStream, input
            )
            cipherOutputStream.write(initialText.toByteArray(charset("UTF-8")))
            cipherOutputStream.close()
            val vals = outputStream.toByteArray()
            //            encryptedText.setText(Base64.encodeToString(vals, Base64.DEFAULT));
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    companion object {
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
    }
}
