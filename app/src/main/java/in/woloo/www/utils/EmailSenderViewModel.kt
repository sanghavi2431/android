package `in`.woloo.www.utils;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmailSenderViewModel : ViewModel()  {

    private val _emailStatus = MutableLiveData<String>()
    val emailStatus: LiveData<String> get() = _emailStatus

    fun sendEmailResonse(toAddresses:List<String> ,  subject: String, body: String) {
        viewModelScope.launch {
            try {
                // Call a suspend function to perform the network operation
                val result = withContext(Dispatchers.IO) {
                    // Simulate network operation
                    performNetworkOperation(toAddresses ,  subject, body)
                }
                _emailStatus.postValue("Email sent successfully")
            } catch (e: Exception) {
                _emailStatus.postValue("Failed to send email: ${e.message}")
            }
        }
    }

    private fun performNetworkOperation( toAddresses:List<String> ,  subject: String, body: String): String {
      //  EmailSender.sendEmail(toAddresses ,  subject, body);
        Thread.sleep(2000) // Simulating delay
        return "Success"
    }

}