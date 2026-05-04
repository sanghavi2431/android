package `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.model.server_request.PurchaseNowRequest
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard.Companion
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.databinding.ActivityPurchasePowderRoomSingleUseTicketBinding
import `in`.woloo.www.databinding.DialogSosDetailsBinding
import `in`.woloo.www.more.subscribe.subscription.viewmodel.SubscriptionViewModel
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent

class PurchasePowderRoomSingleUseTicketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPurchasePowderRoomSingleUseTicketBinding
    var subscriptionViewModel: SubscriptionViewModel? = null
    var powderRoomId : String? = null
    var mSharedPreference: SharedPreference? = null
    var orderId: String? = null
    var wolooId: String? = null
    var powderRoomAmount : String? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (mSharedPreference == null) {
            mSharedPreference = SharedPreference(baseContext)
        }

        binding = ActivityPurchasePowderRoomSingleUseTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subscriptionViewModel = ViewModelProvider(this).get<SubscriptionViewModel>(
            SubscriptionViewModel::class.java
        )

        var bundle = Bundle()
        var payload = HashMap<String, Any>()
        bundle.putString(AppConstants.POWDER_ROOM_OFFER, "Powder Room Offer")
        payload[AppConstants.POWDER_ROOM_OFFER] = "Powder Room Offer"
        logFirebaseEvent(this, bundle, AppConstants.POWDER_ROOM_OFFER)
        logNetcoreEvent( this, payload, AppConstants.POWDER_ROOM_OFFER)

        powderRoomId = mSharedPreference!!.getStoredPreference(
            this,
            SharedPreferencesEnum.POWDER_ROOM_CODE.getPreferenceKey(),
            ""
        )
        powderRoomAmount = mSharedPreference?.getStoredPreference(
            this,
            SharedPreferencesEnum.POWDER_ROOM_AMOUNT.preferenceKey,
            ""
        )
        binding.showAmountText.setText("\u20B9$powderRoomAmount/-")
        binding.buy10Rs.setText("Buy Pass For \u20B9$powderRoomAmount/-")
        binding.displayTextThree.setText("Affordable Access At Just \u20B9$powderRoomAmount/-")


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mSharedPreference?.setStoredPreference(
                   applicationContext,
                    SharedPreferencesEnum.WAH_CERTIFICATE_CODE.preferenceKey,
                    ""
                ).toString()
                mSharedPreference?.setStoredPreference(
                    applicationContext,
                    SharedPreferencesEnum.POWDER_ROOM_CODE.preferenceKey,
                    ""
                ).toString()
            }
        })


        binding.buy10Rs.setOnClickListener{
            val request  =
                PurchaseNowRequest( powderRoomId = powderRoomId!!.toInt(),  // your powder room ID
                    amountOfPowderRoom = powderRoomAmount!!.toInt())
            subscriptionViewModel!!.getPurchaseNow(request)
        }

       /* binding.buyWolooSubscription.setOnClickListener{

            val i = Intent(this , WolooDashboard::class.java)
            i.putExtra("subscribeFragment" , "subscribeFragment")
            startActivity(i)
            // Begin a transaction to replace the current fragment with XYZFragment
        }*/

        binding.contactHelpPowderroom.setOnClickListener {
            showContactConnectDialog(
                "WOLOO",
                "Woloo Support",
                "1706 Lodha Supremus,Tunga Village opp.MTNL, Powai Andheri (E),Mumbai",
                (AppConstants.CALL_MOBILE + AppConstants.MOBILENUMBER).substring(4)
            )

        }

        setLiveData()

    }

    override fun onStop() {
        super.onStop()
        mSharedPreference?.setStoredPreference(
            applicationContext,
            SharedPreferencesEnum.WAH_CERTIFICATE_CODE.preferenceKey,
            ""
        ).toString()
        mSharedPreference?.setStoredPreference(
            applicationContext,
            SharedPreferencesEnum.POWDER_ROOM_CODE.preferenceKey,
            ""
        ).toString()
    }



    override fun onDestroy() {
        super.onDestroy()
        mSharedPreference?.setStoredPreference(
            applicationContext,
            SharedPreferencesEnum.WAH_CERTIFICATE_CODE.preferenceKey,
            ""
        ).toString()
        mSharedPreference?.setStoredPreference(
            applicationContext,
            SharedPreferencesEnum.POWDER_ROOM_CODE.preferenceKey,
            ""
        ).toString()
    }

    @SuppressLint("SetTextI18n")
    private fun showContactConnectDialog(type: String?, name: String?, address: String?, number: String?) {

        try {


            val dialogNew = Dialog(this).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
                setCancelable(true)
                setCanceledOnTouchOutside(true)
                //setContentView(R.layout.dialog_sos_details)
            }

            val binding = DialogSosDetailsBinding.inflate(layoutInflater)
            dialogNew.setContentView(binding.root)

            binding.emergencyName.text = "Woloo Support"
            binding.emergencyPhone.text =
                (AppConstants.CALL_MOBILE + AppConstants.MOBILENUMBER).substring(4)
            binding.explainTextSos.text = "Woloo Powder Room Support Team is Happy to Serve you!!"


            binding.callSosNum.setOnClickListener {
                if (type == "WOLOO") {
                    makePhoneCall(AppConstants.CALL_MOBILE + AppConstants.MOBILENUMBER)
                }
                dialogNew.dismiss()
            }

            dialogNew.show()

        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun makePhoneCall(mobilenumber: String) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
               this,
                arrayOf<String>(Manifest.permission.CALL_PHONE),
                REQUEST_CALL_PERMISSION
            )
        } else {
            // Permission already granted, proceed with making the call
            startCall(mobilenumber)
        }
    }

    private fun startCall(mobileNumber: String) {
        val phoneNumber = mobileNumber // Replace with your phone number
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.setData(Uri.parse(phoneNumber))
        try {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startActivity(callIntent)
            }
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "Your device doesn't support phone calls.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    fun setLiveData()
    {
        subscriptionViewModel!!.observePurchaseNow().observe(this){
            if (it != null){
                try {
                    if (it.data != null) {
                        orderId = it.data!!.orderId.toString()
                        wolooId = powderRoomId
                        if (!TextUtils.isEmpty(orderId)) {
                            CommonUtils.navigateToPerUseFlow(
                                this,
                                orderId,
                                true,
                                SharedPrefSettings.getPreferences.fetchUserDetails()?.mobile.toString(),
                                wolooId
                            )
                           this.finish()


                        } else {
                            Logger.e(
                                "REponse if else",
                                it.data.toString()
                            )
                        }
                    } else {
                        Logger.e("REponse else", it.data.toString())
                    }
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
            } else {
//                    displayToast(WolooApplication.getErrorMessage())
                WolooApplication.errorMessage = ""
            }

        }
    }




    companion object {
        private const val REQUEST_CALL_PERMISSION = 2001
    }


}


