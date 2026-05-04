package `in`.woloo.www.application_kotlin.deeplink

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.application_kotlin.receiver.DeeplinkReceiver
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings

class DeeplinkDispatcherActivity : ComponentActivity() {

    private var targetIntent = Intent()

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            targetIntent = Intent(this, WolooDashboard::class.java)
            startActivity(targetIntent)
            finishAffinity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val deepLinkvalue = intent.getStringExtra(DeeplinkReceiver.DEEPLINK_KEY)
        Logger.e("DeeplinkDispatcherActivity", deepLinkvalue.toString())

        if (deepLinkvalue?.isNotEmpty() == true) {
            val link = Uri.parse(deepLinkvalue)

            try {
                Logger.e("URL woloo", deepLinkvalue)
                Logger.e("HOST woloo", link.host!!)
                Logger.e("scheme woloo", link.scheme!!)
                Logger.e("getQuery woloo", link.query!!)
                Logger.e("getLastPathSegment woloo", link.lastPathSegment!!)
                Logger.e("getSchemeSpecificPart woloo", link.schemeSpecificPart)
            }catch (e : Exception){
                finish()
            }
//             when(link.scheme){}
            when (link.host) {
                "subscription" -> {
                    if(SharedPrefSettings.getPreferences.fetchIsLoggedIn()){
                        targetIntent = Intent(this, WolooDashboard::class.java)
                        targetIntent.putExtra("deeplink",link.host)
                        startForResult.launch(targetIntent)
                    }
                }
                "shop" -> {
                    if(SharedPrefSettings.getPreferences.fetchIsLoggedIn()){
                        targetIntent = Intent(this, WolooDashboard::class.java)
                        targetIntent.putExtra("deeplink",link.host)
                        startActivity(targetIntent)
                    }
                }
                "refer" -> {
                    if(SharedPrefSettings.getPreferences.fetchIsLoggedIn()){
                        targetIntent = Intent(this, WolooDashboard::class.java)
                        targetIntent.putExtra("deeplink",link.host)
                        startActivity(targetIntent)
                    }
                }
                "woloo.page.link" ->{
                    targetIntent = Intent(Intent.ACTION_VIEW, Uri.parse(deepLinkvalue))
//                        targetIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(targetIntent)
                }
                else -> {
                }

            }

        }
        finish()
    }


}