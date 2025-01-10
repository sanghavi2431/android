package `in`.woloo.www.application_kotlin.api_classes.publickey_pinning

import java.math.BigInteger
import java.security.KeyStore
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.security.interfaces.RSAPublicKey
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class PubKeyManager(private val publicKey: String) : X509TrustManager {
    @Throws(CertificateException::class)
    override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        requireNotNull(chain) { "checkServerTrusted: X509Certificate array is null" }
        require(!(chain.size <= 0)) { "checkServerTrusted: X509Certificate is empty" }

        // Perform customary SSL/TLS checks
        val tmf: TrustManagerFactory
        try {
            tmf = TrustManagerFactory.getInstance("X509")
            tmf.init(null as KeyStore?)
            for (trustManager in tmf.trustManagers) {
                (trustManager as X509TrustManager).checkServerTrusted(
                    chain, authType
                )
            }
        } catch (e: Exception) {
            // throw new CertificateException(e.toString());
            //  CommonUtils.printStackTrace(e)
        }

        // Hack ahead: BigInteger and toString(). We know a DER encoded Public
        // Key starts with 0x30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is
        // no leading 0x00 to drop.
        val pubkey = chain[0].publicKey as RSAPublicKey
        val encoded = BigInteger(1 /* positive */, pubkey.encoded)
            .toString(16)

        // Pin it!
        val expected = publicKey.equals(encoded, ignoreCase = true)
        // fail if expected public key is different from our public key
        if (!expected) {
            throw CertificateException(
                "Not trusted"
            )
        }
    }

    override fun checkClientTrusted(xcs: Array<X509Certificate>, string: String) {
        // throw new
        // UnsupportedOperationException("checkClientTrusted: Not supported yet.");
    }

    override fun getAcceptedIssuers(): Array<X509Certificate>? {
        // Optionally, you can throw an exception if not supported
        // throw UnsupportedOperationException("getAcceptedIssuers: Not supported yet.")
        return null
    }

}