package `in`.woloo.www.application_kotlin.api_classes

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.jetsynthesys.encryptor.JetEncryptor
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException

open class ImageMultipartRequest : Request<NetworkResponse> {
    private val twoHyphens = "--"
    private val lineEnd = "\r\n"
    private val boundary = "apiclient-" + System.currentTimeMillis()
    private var mListener: Response.Listener<NetworkResponse>
    private var mErrorListener: Response.ErrorListener
    private var mHeaders: MutableMap<String, String>?
    private var mContext: Context? = null
    private var jetEncryptor: JetEncryptor? = null

    @Suppress("unused")
    constructor(
        url: String?,
        headers: MutableMap<String, String>?,
        listener: Response.Listener<NetworkResponse>,
        errorListener: Response.ErrorListener
    ) : super(
        Method.POST, url, errorListener
    ) {
        mListener = listener
        this.mErrorListener = errorListener
        mHeaders = headers
    }

    constructor(
        method: Int,
        url: String?,
        listener: Response.Listener<NetworkResponse>,
        errorListener: Response.ErrorListener,
        mContext: Context?
    ) : super(method, url, errorListener) {
        mListener = listener
        this.mContext = mContext
        mHeaders = HashMap()
        this.mErrorListener = errorListener
    }

    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        jetEncryptor = JetEncryptor.getInstance()
        if (CommonUtils().isLoggedIn) {
            val (id, roleId, name, email, password, rememberToken, mobile, city, pincode, address, avatar, fbId, gpId, refCode, sponsorId, wolooId, subscriptionId, expiryDate, voucherId, giftSubscriptionId, lat, lng, otp, status, settings, createdAt, updatedAt, deletedAt, gender, isFirstSession, dob, isThirstReminder, thirstReminderHours, isBlogContentNotification, isFreeTrial, isVtionUser) = CommonUtils().userInfo!!
            mHeaders!!["Authorization"] =
                "Bearer " + SharedPrefSettings.Companion.getPreferences.fetchToken()
        } else {
            mHeaders!!["Authorization"] = "Bearer " + jetEncryptor?.jwtkey
        }
        return if (mHeaders != null) mHeaders!! else super.getHeaders()
    }

    override fun getBodyContentType(): String {
        return "multipart/form-data;boundary=$boundary"
    }

    @Throws(AuthFailureError::class)
    override fun getBody(): ByteArray? {
        val bos = ByteArrayOutputStream()
        val dos = DataOutputStream(bos)

        return try {
            // Populate text payload
            val params = params
            if (params != null && params.isNotEmpty()) {
                textParse(dos, params, paramsEncoding)
            }

            // Populate data byte payload
            val data = byteData
            if (data != null && data.isNotEmpty()) {
                dataParse(dos, data)
            }

            // Close multipart form data after text and file data
            dos.writeBytes("$twoHyphens$boundary$twoHyphens$lineEnd")
            bos.toByteArray()
        } catch (e: IOException) {
            CommonUtils.printStackTrace(e)
            null
        }
    }


    protected open val byteData: Map<String, DataPart>?
        protected get() = null

    override fun parseNetworkResponse(response: NetworkResponse): Response<NetworkResponse> {
        return try {
            Response.success(
                response,
                HttpHeaderParser.parseCacheHeaders(response)
            )
        } catch (e: Exception) {
            Response.error(ParseError(e))
        }
    }

    override fun deliverResponse(response: NetworkResponse) {
        mListener.onResponse(response)
    }

    override fun deliverError(error: VolleyError) {
        mErrorListener.onErrorResponse(error)
    }

    @Throws(IOException::class)
    private fun textParse(
        dataOutputStream: DataOutputStream,
        params: Map<String, String>,
        encoding: String
    ) {
        try {
            for ((key, value) in params) {
                buildTextPart(dataOutputStream, key, value)
            }
        } catch (uee: UnsupportedEncodingException) {
            throw RuntimeException("Encoding not supported: $encoding", uee)
        }
    }

    @Throws(IOException::class)
    private fun dataParse(dataOutputStream: DataOutputStream, data: Map<String, DataPart>) {
        for ((key, value) in data) {
            buildDataPart(dataOutputStream, value, key)
        }
    }

    @Throws(IOException::class)
    private fun buildTextPart(
        dataOutputStream: DataOutputStream,
        parameterName: String,
        parameterValue: String
    ) {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd)
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"$parameterName\"$lineEnd")
        //dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
        dataOutputStream.writeBytes(lineEnd)
        dataOutputStream.writeBytes(parameterValue + lineEnd)
    }

    @Throws(IOException::class)
    private fun buildDataPart(
        dataOutputStream: DataOutputStream,
        dataFile: DataPart,
        inputName: String
    ) {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd)
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + dataFile.fileName + "\"" + lineEnd)
        if (dataFile.type != null && !dataFile.type!!.trim { it <= ' ' }.isEmpty()) {
            dataOutputStream.writeBytes("Content-Type: " + dataFile.type + lineEnd)
        }
        dataOutputStream.writeBytes(lineEnd)
        val fileInputStream = ByteArrayInputStream(dataFile.content)
        var bytesAvailable = fileInputStream.available()
        val maxBufferSize = 1024 * 1024
        var bufferSize = Math.min(bytesAvailable, maxBufferSize)
        val buffer = ByteArray(bufferSize)
        var bytesRead = fileInputStream.read(buffer, 0, bufferSize)
        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize)
            bytesAvailable = fileInputStream.available()
            bufferSize = Math.min(bytesAvailable, maxBufferSize)
            bytesRead = fileInputStream.read(buffer, 0, bufferSize)
        }
        dataOutputStream.writeBytes(lineEnd)
    }

    inner class DataPart {
        var fileName: String? = null
        lateinit var content: ByteArray
        var type: String? = null

        constructor()
        constructor(name: String?, data: ByteArray) {
            fileName = name
            content = data
        }

        constructor(name: String?, data: ByteArray, mimeType: String?) {
            fileName = name
            content = data
            type = mimeType
        }
    }
}
