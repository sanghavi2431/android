package `in`.woloo.www.application_kotlin.api_classes.publickey_pinning

import android.os.Build
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.util.Arrays
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager

class TLSSocketFactory(publicKey: String?) : SSLSocketFactory() {
    private val internalSSLSocketFactory: SSLSocketFactory

    init {
        val context = SSLContext.getInstance("TLSv1.2")
        val tm = arrayOf<TrustManager>(
            PubKeyManager(
                publicKey!!
            )
        )
        context.init(null, tm, null)
        internalSSLSocketFactory = context.socketFactory
    }

    override fun getDefaultCipherSuites(): Array<String> {
        return internalSSLSocketFactory.defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return internalSSLSocketFactory.supportedCipherSuites
    }

    @Throws(IOException::class)
    override fun createSocket(s: Socket, host: String, port: Int, autoClose: Boolean): Socket {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(s, host, port, autoClose))
    }

    @Throws(IOException::class)
    override fun createSocket(host: String, port: Int): Socket {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port))
    }

    @Throws(IOException::class)
    override fun createSocket(
        host: String,
        port: Int,
        localHost: InetAddress,
        localPort: Int
    ): Socket {
        return enableTLSOnSocket(
            internalSSLSocketFactory.createSocket(
                host,
                port,
                localHost,
                localPort
            )
        )
    }

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress, port: Int): Socket {
        return enableTLSOnSocket(internalSSLSocketFactory.createSocket(host, port))
    }

    @Throws(IOException::class)
    override fun createSocket(
        address: InetAddress,
        port: Int,
        localAddress: InetAddress,
        localPort: Int
    ): Socket {
        return enableTLSOnSocket(
            internalSSLSocketFactory.createSocket(
                address,
                port,
                localAddress,
                localPort
            )
        )
    }

    private fun enableTLSOnSocket(socket: Socket): Socket {
        if (socket != null && socket is SSLSocket) {
            val enabledProtocols = socket.enabledProtocols
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) socket.enabledProtocols =
                enabledProtocols else socket.enabledProtocols =
                checkAndAddTLSv1_1Andv1_2(enabledProtocols)
        }
        return socket
    }

    private fun checkAndAddTLSv1_1Andv1_2(currentProtocols: Array<String>): Array<String> {
        var hasTLSv1_1 = false
        var hasTLSv1_2 = false
        val tlsv11 = "TLSv1.1"
        val tlsv12 = "TLSv1.2"
        val list: MutableList<String> = ArrayList(Arrays.asList(*currentProtocols))
        for (protocol in currentProtocols) {
            if (protocol == tlsv11) {
                hasTLSv1_1 = true
            }
            if (protocol == tlsv12) {
                hasTLSv1_2 = true
            }
        }
        if (!hasTLSv1_1) {
            list.add(tlsv11)
        }
        if (!hasTLSv1_2) {
            list.add(tlsv12)
        }
        return list.toTypedArray<String>()
    }
}