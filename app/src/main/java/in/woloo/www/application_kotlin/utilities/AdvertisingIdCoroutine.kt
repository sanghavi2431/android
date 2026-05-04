import android.content.Context
import android.util.Log
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object AdvertisingIdCoroutine {

    fun fetchAdvertisingId(context: Context, onResult: (String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val advertisingId = try {
                val info = AdvertisingIdClient.getAdvertisingIdInfo(context)
                info.id
            } catch (e: Exception) {
                Log.e("TAG", "Failed to retrieve advertising ID", e)
                null
            }

            withContext(Dispatchers.Main) {
                onResult(advertisingId)
            }
        }
    }
}
