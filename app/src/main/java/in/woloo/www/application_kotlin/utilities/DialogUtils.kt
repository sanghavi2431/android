package `in`.woloo.www.application_kotlin.utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.EnrouteDirectionActivity
import `in`.woloo.www.common.CommonUtils

object DialogUtils {

    fun showMoreKmDialog(range: Int , context : Context)
    {
        try {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(
                ContextCompat.getColor(
                context,R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_change_radius)
            val displayMetrics = Resources.getSystem().displayMetrics
            val screenWidth = displayMetrics.widthPixels

            val dialogWidth = (screenWidth).toInt()
            dialog.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setGravity(Gravity.CENTER)
            val btnCloseDialog = dialog.findViewById<View>(R.id.tv_go_back_to_home) as TextView


            btnCloseDialog.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }


            dialog.show()
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }


    @SuppressLint("SetTextI18n")
    fun showdialogGoToMaps(
        context: Context,
        dest: LatLng,
        wolooId: Int,
        onContinue: () -> Unit
    ) {
        try {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)

            dialog.window?.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        context,
                        R.color.transparent_background
                    )
                )
            )

            dialog.setContentView(R.layout.dialog_go_to_googlemaps)
            dialog.window?.attributes?.windowAnimations =
                R.style.DialogAnimation

            val message = dialog.findViewById<TextView>(R.id.tv_text)
            val action = dialog.findViewById<TextView>(R.id.tv_positive)
            val cancel = dialog.findViewById<TextView>(R.id.tv_negative)

            message.text =
                "Get Ready for Your Reward! Hey there! 👋 Google Maps is about to pop up to guide you. Once you get to your spot, just hop back into the Woloo app and snag those reward points! ✨ Easy peasy! 😉"

            action.text = "Continue"
            action.visibility = View.VISIBLE

            action.setOnClickListener {
                dialog.dismiss()

                // ONLY STATE STORAGE IS OK
                SharedPrefSettings.getPreferences.storeIsDirectionWoloo(true)
                SharedPrefSettings.getPreferences.storeDirectionWoloo(
                    EnrouteDirectionActivity.DirectionWoloo(
                        wolooId,
                        dest.latitude,
                        dest.longitude
                    )
                )

                onContinue.invoke()
            }

            cancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()

        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }



        fun showSuccessDialog(
            context: Context,
            pointsText: String,
            onClose: () -> Unit,
            onShopNow: () -> Unit
        ) {
            try {
                if (context !is Activity || context.isFinishing) {
                    return
                }

                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(true)
                dialog.setCanceledOnTouchOutside(true)

                dialog.window?.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            context,
                            R.color.transparent_background
                        )
                    )
                )

                dialog.setContentView(R.layout.dialog_coins_success)

                val screenWidth =
                    context.resources.displayMetrics.widthPixels

                dialog.window?.setLayout(
                    screenWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                dialog.window?.attributes?.windowAnimations =
                    R.style.DialogAnimation
                dialog.window?.setGravity(Gravity.BOTTOM)

                val btnClose =
                    dialog.findViewById<TextView>(R.id.tv_go_back_to_home)
                val btnShop =
                    dialog.findViewById<TextView>(R.id.tv_shop_now)
                val gifImage =
                    dialog.findViewById<ImageView>(R.id.gifImageView)
                val successText =
                    dialog.findViewById<TextView>(R.id.tv_logout)

                successText.text = pointsText

                Glide.with(context)
                    .load(R.drawable.coins_animate)
                    .into(gifImage)

                btnClose.setOnClickListener {
                    dialog.dismiss()
                    onClose()
                }

                btnShop.setOnClickListener {
                    dialog.dismiss()
                    onShopNow()
                }

                dialog.show()

            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        }




        fun showReachedLocationDialog(
            context: Context,
            messageText: String?,
            pointsText: String?,
            isPoints: Boolean,
            onHomeClick: () -> Unit,
            onAutoNavigateToReview: () -> Unit
        ) {
            try {
                if (context !is Activity || context.isFinishing) return

                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(false)

                dialog.window?.setBackgroundDrawable(
                    ColorDrawable(
                        ContextCompat.getColor(
                            context,
                            R.color.transparent_background
                        )
                    )
                )

                dialog.setContentView(R.layout.dialog_destinationarrived)
                dialog.window?.attributes?.windowAnimations =
                    R.style.DialogAnimation

                val message =
                    dialog.findViewById<TextView>(R.id.tv_text)
                val action =
                    dialog.findViewById<TextView>(R.id.tv_subscribe)
                val tvText2 =
                    dialog.findViewById<TextView>(R.id.tv_text2)

                message.text =
                    messageText?.replace("\\\\n".toRegex(), "\n")

                if (isPoints) {
                    tvText2.text = pointsText
                    tvText2.visibility = View.VISIBLE
                    action.visibility = View.GONE
                } else {
                    tvText2.visibility = View.GONE
                    action.visibility = View.VISIBLE
                    action.text = "HOME"
                }

                action.setOnClickListener {
                    dialog.dismiss()
                    onHomeClick()
                }

                dialog.show()

                if (isPoints) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        if (dialog.isShowing) dialog.dismiss()
                        onAutoNavigateToReview()
                    }, 3000)
                }

            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        }

    fun createProgressDialog(context: Context): Dialog {
        return Dialog(context, R.style.CustomDialogTime).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        context,
                        android.R.color.transparent
                    )
                )
            )
            setCancelable(false)
            setContentView(R.layout.dialog_progress_overlay)
        }
    }



}