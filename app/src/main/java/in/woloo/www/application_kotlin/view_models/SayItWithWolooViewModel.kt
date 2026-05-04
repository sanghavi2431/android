package `in`.woloo.www.application_kotlin.view_models


import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.EventLiveData
import `in`.woloo.www.application_kotlin.model.server_response.SayItAddMessageResponse
import `in`.woloo.www.application_kotlin.model.server_response.SayItFileUploadResponse
import `in`.woloo.www.application_kotlin.repositories.SayItWithWolooRepository
import `in`.woloo.www.common.CommonUtils
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File

class SayItWithWolooViewModel(private val repository: SayItWithWolooRepository) : ViewModel() {

    private val _fileUploadResponse = MutableLiveData<Response<SayItFileUploadResponse>>()
    val fileUploadResponse: LiveData<Response<SayItFileUploadResponse>> get() = _fileUploadResponse

    private val _messageResponse = MutableLiveData<SayItAddMessageResponse>()
    val messageResponse: LiveData<SayItAddMessageResponse> get() = _messageResponse

    private val _qrsendResponse = MutableLiveData<Result<String>>()
    val qrsendResponse: LiveData<Result<String>> get() = _qrsendResponse

    fun sendMessage(name: String, number: String) {
        viewModelScope.launch {
            try {
                val response = repository.sendMessage(name, number)
                if (response.isSuccessful) {
                    // Handle success
                    val message = response.body()
                    Log.d("Say it Response" , response.toString())
                    // Do something with the message
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    fun addMessage(context: Context ,qrid:String,name: String, number: String , recName: String, recNumber: String, message: String, occasion: String ,otherOccasion: String, attachmentUrl: String) {
        viewModelScope.launch {
            try {
                val response = repository.addMessage(qrid, name, number, recName, recNumber, message, occasion, otherOccasion, attachmentUrl)
                if (response.isSuccessful) {
                    response.body()?.let { messageResponse ->
                        Log.d("SayIt Response", "Success: ${messageResponse.message}") // Log success message
                        _messageResponse.postValue(messageResponse)  // Post actual response
                    }
                } else {
                    Log.d("SayIt Response", "Error: ${response.errorBody()?.string()}")
                    val errorMessage = response.errorBody()?.string() ?: "Something went wrong"
                    CommonUtils.showCustomDialogBackClick(context, errorMessage)

                }
            } catch (e: Exception) {
                Log.e("SayIt Response", "Exception: ${e.message}", e)
            }
        }
    }


    fun fileUpload(context: Context ,file: MultipartBody.Part, qrid: RequestBody) {
        viewModelScope.launch {
            try {
                val response  = repository.fileUpload(file, qrid)
                if (response.isSuccessful) {
                    response.body()?.let {
                        // Handle success
                        _fileUploadResponse.postValue(response)
                        Log.d("Say it Response", fileUploadResponse.toString())
                    }
                } else {
                    // Handle error
                    val errorMessage = response.errorBody()?.string() ?: "Something went wrong"
                    CommonUtils.showCustomDialogBackClick(context, errorMessage)
                    _fileUploadResponse.postValue(response)
                    Log.d("Say it Response", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                // Handle exception
                Log.e("Say it Response", "Exception: ${e.message}", e)
            }
        }
    }

    fun qrSend(context: Context, qrid:String) {
        viewModelScope.launch {
            try {
                val response = repository.qrSend(qrid )
                if (response.isSuccessful) {
                    // Handle success
                    val message = response.body()
                    Log.d("Say it Response" , response.toString())
                    _qrsendResponse.postValue(Result.success(response.body().toString()))
                    // Do something with the message
                } else {
                    // Handle error
                    val errorMessage = response.errorBody()?.string() ?: "Something went wrong"
                    CommonUtils.showCustomDialogBackClick(context, errorMessage)
                    _qrsendResponse.postValue(Result.failure(Exception("Error: ${response.code()}")))
                }
            } catch (e: Exception) {
                // Handle exception
                _qrsendResponse.postValue(Result.failure(e))
            }
        }
    }


}
