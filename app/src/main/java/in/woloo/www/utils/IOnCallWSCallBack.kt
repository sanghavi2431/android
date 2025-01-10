package `in`.woloo.www.utils

import com.android.volley.VolleyError
import `in`.woloo.www.more.models.FileUploadResponse

interface IOnCallWSCallBack {
    fun onSuccessResponse(fileUploadResponse: FileUploadResponse?)
    fun onFailure(volleyError: VolleyError?)
}
