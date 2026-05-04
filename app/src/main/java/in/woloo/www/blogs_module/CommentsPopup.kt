package `in`.woloo.www.blogs_module

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import `in`.woloo.www.R
import `in`.woloo.www.databinding.BlogCommentsPopupBinding
import `in`.woloo.www.more.trendingblog.viewmodel.BlogViewModel



class CommentsPopup : BottomSheetDialogFragment() {

    private lateinit var binding: BlogCommentsPopupBinding
    private lateinit var blogViewModel: BlogViewModel
    private var blogId: String? = null

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
        binding = BlogCommentsPopupBinding.inflate(inflater, container, false)
        blogViewModel = ViewModelProvider(this).get<BlogViewModel>(
            BlogViewModel::class.java
        )

        blogId = arguments?.getString(BLOG_ID)
        blogViewModel.getCommentListBlogWise(blogId!!)

        binding.writeReviewImage.setOnClickListener {

            val bottomSheetFragment = AddCommentToBlogPopup.newInstance(blogId!!)
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)

        }

        lifecycleScope.launchWhenStarted {
            blogViewModel.observeCommentListBlogWise().collect { response ->
                response?.let {

                    try {
                        if(response.data!!.data!!.isNotEmpty()) {
                            binding.deliveryDetailsRecycler.layoutManager = LinearLayoutManager(
                                requireActivity(),
                                LinearLayoutManager.VERTICAL,
                                false
                            )
                            binding.deliveryDetailsRecycler.adapter =
                                CommentsAdapter(requireActivity() , response.data!!.data!!)
                        }
                    }catch (e : Exception)
                    {
                        e.printStackTrace()
                    }

                }
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        blogViewModel.getCommentListBlogWise(blogId!!)
    }

    companion object {

        private const val BLOG_ID = "blog_id"

        @JvmStatic
        fun newInstance(blogId: String): CommentsPopup {
            val fragment = CommentsPopup()

            CommentsPopup().apply {
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