package `in`.woloo.www.application_kotlin.utilities;

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

class EmailSender {

    companion object {
        private const val SMTP_SERVER = "smtpout.secureserver.net"
        private const val SMTP_PORT = "465"  // Or "587" if you are using TLS
        private const val USERNAME = "woloo@woloo.in"
        private const val PASSWORD = BuildConfig.EMAIL_PASSWORD

        private val executor = Executors.newSingleThreadExecutor()

        fun sendEmail(toEmail: String, subject: String, message: String) {
            executor.execute {
                try {
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
                            return PasswordAuthentication(USERNAME, PASSWORD)
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


