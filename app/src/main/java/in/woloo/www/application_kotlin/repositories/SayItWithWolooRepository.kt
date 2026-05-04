package `in`.woloo.www.application_kotlin.repositories

import `in`.woloo.www.application_kotlin.api_classes.sayItwithWolooInstance
import `in`.woloo.www.application_kotlin.interfaces.sayitwithwoloo
import `in`.woloo.www.application_kotlin.model.server_response.SayItAddMessageResponse
import `in`.woloo.www.application_kotlin.model.server_response.SayItFileUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class SayItWithWolooRepository {
    private val api = sayItwithWolooInstance.api

    suspend fun sendMessage(name: String, number: String) = api.sendMessage(name, number)

    suspend fun addMessage(qrid:String,name: String, number: String , recName: String,
                           recNumber: String, message: String,
                           occasion: String ,otherOccasion: String, attachmentUrl: String): Response<SayItAddMessageResponse>{
        return api.addMessage(qrid ,name, number , recName, recNumber,
            message, occasion ,otherOccasion, attachmentUrl)
    }

    suspend fun fileUpload(file: MultipartBody.Part, qrid: RequestBody): Response<SayItFileUploadResponse> {
        return api.fileUpload(file, qrid)
    }


    suspend fun qrSend(qrid:String)
            = api.qrSend(qrid )

}