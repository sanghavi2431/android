package `in`.woloo.www.blogs_module

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.model.CreditCoinsRequest
import `in`.woloo.www.blogs_module.CommentsPopup
import `in`.woloo.www.blogs_module.CommentsPopup.Companion
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.BlogAddCommentPopupBinding
import `in`.woloo.www.databinding.BlogCommentsPopupBinding
import `in`.woloo.www.more.trendingblog.viewmodel.BlogViewModel

class AddCommentToBlogPopup  : BottomSheetDialogFragment() {

    private lateinit var binding: BlogAddCommentPopupBinding
    private lateinit var blogViewModel: BlogViewModel
    private var blogId: String? = null
    var customDialog: Dialog? = null

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = BlogAddCommentPopupBinding.inflate(inflater, container, false)
        blogViewModel = ViewModelProvider(this).get<BlogViewModel>(
            BlogViewModel::class.java
        )

        blogId = arguments?.getString(BLOG_ID)



// Remove border once user starts typing
        binding.tvComment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Remove border once text is entered
                if (!s.isNullOrEmpty()) {
                    removeBorder(binding.tvComment)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.proceedToComment.setOnClickListener{
            if(!binding.tvComment.text.toString().isEmpty()) {
                val request = CommentRequest(
                    blogId = blogId!!,
                    userId = SharedPrefSettings.getPreferences.fetchUserDetails()!!.id.toString(),
                    commentText = binding.tvComment.text.toString()
                )
                blogViewModel.postComment(request)
            }
            else{
                binding.tvComment.hint = "Please enter comment"
                binding.tvComment.background
                setRedBorder(binding.tvComment)
            }
        }

        lifecycleScope.launchWhenStarted {
            blogViewModel.observeCommentAdded().collect { response ->
                response?.let {

                    try {
                        (requireParentFragment() as? BottomSheetDialogFragment)?.dismiss()
                        dismiss()
                        showSuccessDialog()

                    }catch (e : Exception)
                    {
                        e.printStackTrace()
                    }

                }
            }
        }

        return binding.root
    }

    fun setRedBorder(editText: EditText) {
        val shape = GradientDrawable()
        shape.setStroke(4, Color.RED) // 4px red border
        editText.background = shape
    }

    @SuppressLint("ResourceAsColor")
    fun removeBorder(editText: EditText) {
        editText.background = ContextCompat.getDrawable(editText.context, R.color.comment_bg)
        val shape = GradientDrawable()
        shape.setStroke(4, R.color.comment_bg) // 4px red border
        editText.background = shape
    }

    private fun showSuccessDialog() {
        try {
            customDialog = Dialog(requireContext())
            customDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            customDialog!!.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            customDialog?.setCancelable(true)
            customDialog?.setCanceledOnTouchOutside(true)
            customDialog?.setContentView(R.layout.dialog_share_review)
            val tvOkay = customDialog?.findViewById<View>(R.id.tvOK) as TextView
            val tv_dialogreview = customDialog?.findViewById<View>(R.id.tv_dialogreview) as TextView
            val gifImageView = customDialog?.findViewById<View>(R.id.review_success) as ImageView


            gifImageView.visibility = View.GONE

                tv_dialogreview.text = "Thank you for adding Comment"
            tvOkay.setOnClickListener {
                customDialog?.dismiss()
            }
            if (!customDialog?.isShowing!!) {
                customDialog?.show()
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    companion object {

        private const val BLOG_ID = "blog_id"

        @JvmStatic
        fun newInstance(blogId: String): AddCommentToBlogPopup {
            val fragment = AddCommentToBlogPopup()

            AddCommentToBlogPopup().apply {
                arguments = Bundle().apply {
                    val args = Bundle().apply {
                        putString(BLOG_ID, blogId)
                    }
                    fragment.arguments = args
                    return fragment
                }
            }
        }
    }
}