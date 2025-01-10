package `in`.woloo.www.application_kotlin.utilities

import android.os.Build
import androidx.annotation.RequiresApi
import `in`.woloo.www.BuildConfig
import java.util.Properties
import java.util.concurrent.Executors
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailSenderClass {

    companion object {
        private const val SMTP_SERVER = "smtpout.secureserver.net"
        private const val SMTP_PORT = "465"  // Or "587" if you are using TLS
        private const val USERNAME = "woloo@woloo.in"
        private val ENCRYPTION_KEY = BuildConfig.EMAIL_ENCRYPTION_KEY
        private val ENCRYPTED_PASSWORD = BuildConfig.EMAIL_PASSWORD



        // Example of how to get the key from an environment variable

        @RequiresApi(Build.VERSION_CODES.O)
        private val SECRET_KEY = SecurePreferences.generateKey()


        @RequiresApi(Build.VERSION_CODES.O)
        val encryptedText = SecurePreferences.encrypt(ENCRYPTED_PASSWORD, SECRET_KEY)
        private val executor = Executors.newSingleThreadExecutor()

        @RequiresApi(Build.VERSION_CODES.O)
        fun sendEmail(toEmail: String, subject: String, message: String) {
            executor.execute {
                try {

                    val encryptedPassword = (encryptedText)
                    val password = SecurePreferences.decrypt(encryptedPassword, SECRET_KEY)
                    println("Email Sending$encryptedPassword $password $SECRET_KEY")
                    val props = Properties().apply {
                        put("mail.smtp.host", SMTP_SERVER)
                        put("mail.smtp.port", SMTP_PORT)
                        put("mail.smtp.auth", "true")
                        put("mail.smtp.socketFactory.port", SMTP_PORT)
                        put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory") // Use "TLS" if using port 587
                        put("mail.smtp.socketFactory.fallback", "false")
                    }

                    val session = Session.getInstance(props, object : Authenticator() {
                        override fun getPasswordAuthentication(): PasswordAuthentication {
                            return PasswordAuthentication(USERNAME, password)
                        }
                    })

                    val mimeMessage = MimeMessage(session).apply {
                        setFrom(InternetAddress(USERNAME))
                        setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
                        setSubject(subject)
                        setText(message)
                    }

                    Transport.send(mimeMessage)
                    println("Email Sent Successfully")

                } catch (e: MessagingException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
