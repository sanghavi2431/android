package `in`.woloo.www.application_kotlin.utilities

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object AgeCalculatorCoroutine {

    fun calculateAge(context: Context, dateString: String, callback: (Int?) -> Unit) {
        // Use application context to avoid leaks
        val appContext = context.applicationContext

        CoroutineScope(Dispatchers.IO).launch {
            val myFormat = "dd-MMM-yyyy" // The format to use
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            val age: Int? = try {
                val dateOfBirth = sdf.parse(dateString)
                val ageCalculator = AgeCalculatorFromDOB // Create an instance
                ageCalculator.calculateAge(dateOfBirth) // Call the method
            } catch (e: ParseException) {
                Log.e("TAG", "Error parsing date", e)
                null
            }

            // Switch to the Main thread to update UI or perform further actions
            withContext(Dispatchers.Main) {
                callback(age)
            }
        }
    }

}