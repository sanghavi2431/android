package `in`.woloo.www.application_kotlin.interfaces

import `in`.woloo.www.application_kotlin.model.server_response.SayItAddMessageResponse
import `in`.woloo.www.application_kotlin.model.server_response.SayItFileUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import java.io.File

interface sayitwithwoloo {

    @GET("WOLOO")
    suspend fun sendMessage(
        @Query("name") name: String,
        @Query("number") number: String
    ): Response<Unit>

    @FormUrlEncoded
    @POST("Message/add_message")
    suspend fun addMessage(
        @Field("QrId") qrid:String,
        @Field("Name") name: String,
        @Field("Number") number: String ,
        @Field("RecName") recName: String,
        @Field("RecNumber") recNumber: String,
        @Field("Msg") message: String,
        @Field("Occasion") occasion: String ,
        @Field("OtherOccasion") otherOccasion: String,
        @Field("AttachmentURL") attachmentUrl: String
    ): Response<SayItAddMessageResponse>

  /*  @Multipart
    @POST("Message/FileUpload/")
    suspend fun fileUpload(
        @Part("file") file: MultipartBody.Part,
        @Part("QrId")  qrid: RequestBody
    ): Response<Unit>*/

    @Multipart
    @POST("Message/FileUpload/")
    suspend fun fileUpload(
        @Part file: MultipartBody.Part, // No part name in the annotation
        @Part("QrId") qrid: RequestBody
    ): Response<SayItFileUploadResponse>


    @FormUrlEncoded
    @POST("Message/QrSend")
    suspend fun qrSend(
        @Field("QrId") qrid:String
    ): Response<Unit>

}