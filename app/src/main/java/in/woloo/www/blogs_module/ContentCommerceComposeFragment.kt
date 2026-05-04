package `in`.woloo.www.blogs_module

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.appsflyer.internal.by
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.more.trendingblog.model.BlogByCategoryResponse
import `in`.woloo.www.more.trendingblog.model.blog.Blog
import  `in`.woloo.www.more.trendingblog.model.blog.Category
import `in`.woloo.www.more.trendingblog.viewmodel.BlogViewModel
import `in`.woloo.www.utils.AppConstants
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.ui.AspectRatioFrameLayout
import com.bumptech.glide.Glide
import com.google.gson.Gson
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.screens.StoreProductDetailsActivity
import `in`.woloo.www.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONObject


class ContentCommerceComposeFragment : Fragment() {

    private var blogViewModel: BlogViewModel? = null
    private var page by mutableStateOf(0)
    private var categoryString by mutableStateOf("all")
    private val categories: MutableList<Category> = ArrayList()
    private val categoryListState = mutableStateListOf<Category>()
    private val blogs: MutableList<Blog> = ArrayList()
    private val blogsListState = mutableStateListOf<Blog>()
    private var storeViewModel: StoreViewModel? = null
    private var blogIdReaded : String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        storeViewModel = ViewModelProvider(this).get<StoreViewModel>(
            StoreViewModel::class.java
        )
        blogViewModel = ViewModelProvider(this).get<BlogViewModel>(BlogViewModel::class.java)


        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    FullBlogScreen()
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLiveData()

    }

    @SuppressLint("SuspiciousIndentation")
    fun setLiveData()
    {

        blogViewModel!!.observeBlogsForUserByCategory().observe(viewLifecycleOwner,
            Observer<BaseResponse<BlogByCategoryResponse>?> { response ->
                if (response!!.success) {
                    if(categoryString.equals("all" , ignoreCase = true)) {
                        categories.addAll(response.data!!.categories!!)
                        categoryListState.addAll(response.data!!.categories!!)
                    }
                        blogs.addAll(response.data!!.blogs!!)
                        blogsListState.addAll(response.data!!.blogs!!)
                }
            })

        blogViewModel!!.observeCtaFavourite().observe(viewLifecycleOwner,
            Observer<BaseResponse<FavouriteResult>?> { response ->
                if (response != null && response.data != null) {

                }
            })

        storeViewModel!!.observeProductDetailsWithPrice()
            .observe(viewLifecycleOwner, Observer { response ->
                response?.let {
                    try{
                        Logger.i("Aarati Store", "setLiveData ${it.products!!.id!!}")

                         val intent = Intent(context, StoreProductDetailsActivity::class.java)
                                     intent.putExtra("IS_SHOW_BACK_BUTTON", true)
                                     intent.putExtra("PRODUCT_DETAILS", Gson().toJson(it.products))
                                     intent.putExtra("CALLINGACTIVITY", "CONTENTCOMMERCE")
                             requireActivity().startActivity(intent)

                    }catch (e : Exception)
                    {
                        CommonUtils.printStackTrace(e)
                    }


                }

            })

        blogViewModel?.observeBlogReadPoint()?.observe(requireActivity(), Observer { response ->
            response?.let {
                try {
                    Logger.i("Aarati Store", "setLiveData ${it.message}")

                    blogViewModel?.ctaBlogRead(blogIdReaded.toString())

                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
            }
        })

        blogViewModel!!.observeCtaBlogRead().observe(requireActivity(),
            Observer<BaseResponse<JSONObject>?> { response ->
                if (response != null && response.data != null) {
                    showSuccessDialog(requireContext() , page)
                }
            })

    }

    @Composable
    @Preview
    fun FullBlogScreen() {
        val listState = rememberLazyListState()
        var selectedIndex by remember { mutableStateOf(-1) }
        var isLoading by remember { mutableStateOf(false) }
        var mostVisibleIndex by remember { mutableStateOf(-1) }
        val awardedBlogIds = remember { mutableStateMapOf<String, Boolean>() }

        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                .map { visibleItems ->
                    if (visibleItems.isEmpty()) return@map -1
                    val start = listState.layoutInfo.viewportStartOffset
                    val end = listState.layoutInfo.viewportEndOffset
                    visibleItems.maxByOrNull { item ->
                        val top = maxOf(item.offset, start)
                        val bottom = minOf(item.offset + item.size, end)
                        (bottom - top).coerceAtLeast(0)
                    }?.index ?: -1
                }
                .distinctUntilChanged()
                .collect { idx -> mostVisibleIndex = idx }
        }

        // 2) When most visible index changes, wait 3s; if still same, use that blog's id
        LaunchedEffect(mostVisibleIndex) {
            if (mostVisibleIndex < 0) return@LaunchedEffect
            val startIndex = mostVisibleIndex

            delay(3000)

            // recompute most-visible after delay
            val currentVisible = listState.layoutInfo.visibleItemsInfo
            val start = listState.layoutInfo.viewportStartOffset
            val end = listState.layoutInfo.viewportEndOffset
            val nowMostVisibleIndex = currentVisible.maxByOrNull { item ->
                val top = maxOf(item.offset, start)
                val bottom = minOf(item.offset + item.size, end)
                (bottom - top).coerceAtLeast(0)
            }?.index ?: -1

            if (nowMostVisibleIndex == startIndex) {
                val blog = blogsListState.getOrNull(startIndex) ?: return@LaunchedEffect
                val blogId = blog.id?.toString() ?: return@LaunchedEffect
                val isPointsGivenNow = blog.isBlogRead ?: return@LaunchedEffect


                if (isPointsGivenNow == 0  &&
                    awardedBlogIds[blogId] != true
                ) {
                    awardedBlogIds[blogId] = true // mark before call to avoid double-fire
                    blogViewModel?.blogReadPoint(blogId)
                    blogIdReaded = blogId
                    // or: showStayDialog(context, blog)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // 🚫 This stays fixed at top
            HeaderCard()

            // ✅ This is scrollable
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = sdp(60)),
                verticalArrangement = Arrangement.spacedBy(sdp(10))
            ) {
                // Category Row
                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = sdp(10)),
                        horizontalArrangement = Arrangement.spacedBy(sdp(10))
                    ) {
                        itemsIndexed(categoryListState) { index, category ->
                            CategoryItem(
                                title = category.categoryName ?: "",
                                imageUrl = category.categoryIconUrl ?: "",
                                position = index,
                                isSelected = index == selectedIndex,
                                onClick = {
                                    selectedIndex = index
                                    blogsListState.clear()
                                    categoryString = category.id.toString()
                                    blogViewModel!!.getBlogsForShopByCategory(
                                        category = categoryString,
                                        page = 0,
                                        blogType = AppConstants.BLOG_CAT_TYPE_ONE,
                                        reset = true
                                    )
                                }
                            )
                        }
                    }
                }

                // Shadow line
                item {
                    ShadowLine()
                }


                    // Blog list
                    itemsIndexed(blogsListState) { index, blog ->
                        BlogItem(
                            blogTitle = blog.title ?: "",
                            blogId = blog.id.toString(),
                            blog = blog,
                            position = index,
                            mediaContent = {
                                val hasVideo = !blog.videoUrl.isNullOrEmpty()
                                if (hasVideo) {
                                    VideoPlayer(
                                        url = BuildConfig.NODE_API_URL + blog.videoUrl?.firstOrNull()
                                            .orEmpty()
                                    )
                                } else {
                                    if (blog.mainImage?.size == 1) {
                                        val imageUrl =
                                            BuildConfig.NODE_API_URL + blog.mainImage!!.first()
                                        Image(
                                            painter = rememberAsyncImagePainter(imageUrl),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(sdp(297)),
                                            contentScale = ContentScale.FillBounds
                                        )
                                    } else {
                                        MultiImageGrid(images = blog.mainImage ?: emptyList())
                                    }
                                }
                            },
                            shop_map_id = blog.isMapToShop.toString(),
                            onClick = {
                                if (blog.isBlogRead == 0) {

                                    BlogDetailsActivity.start(
                                        requireContext(),
                                        blog.id.toString(),
                                        "yes"
                                    )
                                } else {
                                    BlogDetailsActivity.start(
                                        requireContext(),
                                        blog.id.toString(),
                                        "no"
                                    )
                                }
                            }
                        )
                    }


                if (isLoading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(vertical = sdp(20))
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }

        }
    }

    @Composable
    fun HeaderCard() {

        LaunchedEffect(categoryString, page) {
            blogViewModel!!.getBlogsForShopByCategory(
                category = categoryString,
                page = page,
                blogType = AppConstants.BLOG_CAT_TYPE_ONE ,
                reset = false
            )
        }


        val context = LocalContext.current
        val density = LocalDensity.current

        val elevation = sdp(5)
        val imageWidth = sdp(77)
        val imageHeight = sdp(59)
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = sdp(10)),
                    contentAlignment = Alignment.TopStart
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shadowElevation = elevation,
                shape = RoundedCornerShape(
                    bottomStart = sdp(18),
                    bottomEnd = sdp(18)
                ), // adjust radius if needed
                color = ContextCompat.getColor(context, R.color.start_theme_color).toComposeColor()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp), // no padding

                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.woloo_powder_room_logo),
                        contentDescription = "Header Logo",
                        modifier = Modifier
                            .width(imageWidth)
                            .height(imageHeight)
                            .padding(top = sdp(10) , bottom = sdp(10)),
                       // like fitXY
                    )
                }
            }
        }
    }



    @Composable
    fun CategoryList() {

        var selectedIndex by remember { mutableStateOf(-1) } // default none selected


        LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = sdp(10), end = sdp(10), bottom = sdp(5)),
                    horizontalArrangement = Arrangement.spacedBy(sdp(5)),
                ) {
                    itemsIndexed(categoryListState) { index, category ->
                        CategoryItem(
                            title = category.categoryName ?: "",
                            imageUrl = category.categoryIconUrl ?: "",
                            position = index,
                            isSelected = index == selectedIndex,
                            onClick = {
                                selectedIndex = index
                                blogsListState.clear()
                                categoryString = category.id.toString()
                                blogViewModel!!.getBlogsForShopByCategory(
                                    category = categoryString,
                                    page = 0,
                                    blogType = AppConstants.BLOG_CAT_TYPE_ONE ,
                                    reset = true
                                )
                            }
                        )
                    }
                }
    }

  /*  @Composable
    fun CategoryItem(title: String, imageUrl: String, position: Int, isSelected: Boolean, onClick: () -> Unit) {
        // List of background colors
        val drawableArray = intArrayOf(
            R.color.category_color_one,  // Use color resources instead of drawable shapes
            R.color.category_color_two,
            R.color.category_color_three,
            R.color.category_color_four,
            R.color.category_color_five
        )

        // Cycle through background colors in the array
        val backgroundColor = colorResource(id = drawableArray[position % drawableArray.size])


        val borderColor = if (isSelected) {
            colorResource(id = R.color.start_theme_color)
        } else {
            Color.Transparent
        }

        Column(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(bottom = sdp(7))
                .clickable { onClick() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(sdp(65))  // Size the Box to match the image size
                    .clip(CircleShape)  // Make it circular
                    .background(color = backgroundColor)
                    .border(width = sdp(2), color = borderColor, shape = CircleShape)// Set the background color here
                    .padding(sdp(10))
            ) {
                // AsyncImage for the category icon/image
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier
                        .size(sdp(55))
                        .clip(CircleShape),
                    contentScale = ContentScale.Fit
                )
            }

            Text(
                text = title,
                modifier = Modifier
                    .width(sdp(65))
                    .padding(top = sdp(10)),
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontFamily = CenturyGothic,
                    fontSize = sdp(9).value.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
*/

    @Composable
    fun CategoryItem(
        title: String,
        imageUrl: String,
        position: Int,
        isSelected: Boolean,
        onClick: () -> Unit
    ) {
        val drawableArray = intArrayOf(
            R.color.category_color_one,
            R.color.category_color_two,
            R.color.category_color_three,
            R.color.category_color_four,
            R.color.category_color_five
        )

        val backgroundColor = colorResource(id = drawableArray[position % drawableArray.size])
        val borderColor = if (isSelected) colorResource(id = R.color.start_theme_color) else Color.Transparent

        Column(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(bottom = sdp(10))
                .clickable { onClick() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(sdp(55))
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .border(sdp(2), borderColor, CircleShape),
                contentAlignment = Alignment.Center // Ensures image is perfectly centered
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    modifier = Modifier
                        .size(sdp(35)) // Ensure this is well within available space
                        .align(Alignment.Center),
                    contentScale = ContentScale.Inside // ✅ Ensures no cropping at all
                )
            }

            Text(
                text = title,
                modifier = Modifier
                    .width(sdp(55))
                    .padding(top = sdp(8)),
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = CenturyGothic,
                    lineHeight = sdp(11).value.sp,
                fontWeight = FontWeight.Bold,
                fontSize = sdp(9).value.sp

                ) ,
                maxLines = 2
            )
        }
    }


    @Composable
    fun ShadowLine()
    {
        Column {
            Spacer(modifier = Modifier.height(sdp(1))) // Blank space above the line

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White)
                    .shadow(
                        elevation = sdp(4),
                        shape = RectangleShape,
                        clip = false
                    )
            )
        }
    }

    @Composable
    fun BlogsList() {
        val listState = rememberLazyListState()

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = sdp(5), end = sdp(5), bottom = sdp(60)),
            verticalArrangement = Arrangement.spacedBy(sdp(5)),
        ) {
            itemsIndexed(blogsListState) { index, blog ->
                BlogItem(
                    blogTitle = blog.title ?: "",
                    blogId = blog.id.toString(),
                    blog = blog,
                    position = index,
                    mediaContent = {
                        val hasVideo = !blog.videoUrl.isNullOrEmpty()
                        if (hasVideo) {
                            VideoPlayer(
                                url = BuildConfig.NODE_API_URL + blog.videoUrl?.firstOrNull()
                                    .orEmpty()
                            )
                        } else {
                            if (blog.mainImage!!.size == 1) {
                                val imageUrl =
                                    BuildConfig.NODE_API_URL + blog.mainImage!!.firstOrNull()
                                Image(
                                    painter = rememberAsyncImagePainter(imageUrl),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(sdp(297)),
                                    contentScale = ContentScale.FillBounds
                                )
                            } else {
                                MultiImageGrid(images = blog.mainImage!!)
                            }
                        }
                    },
                    shop_map_id = blog.isMapToShop.toString(),
                    onClick = {
                        if(blog.isBlogRead == 0)
                        {
                          //  blogViewModel!!.blogReadPoint(blog.id.toString())
                            BlogDetailsActivity.start(
                                requireContext(),
                                blog.id.toString(),
                                "yes"
                            )// , blog.mainImage!! , blog.videoUrl!!)
                        }
                        else {
                            BlogDetailsActivity.start(
                                requireContext(),
                                blog.id.toString(),
                                "no"
                            )// , blog.mainImage!! , blog.videoUrl!!)
                        }
                    }
                )
            }
        }

        /*      // ✅ Observe scrolling continuously and load next page when needed
        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collectLatest { lastVisibleItemIndex ->
                    if (lastVisibleItemIndex == blogsListState.size - 1) {
                       loadNextPage()
                    }
                    Log.d("Pagination", "Reached bottom at index $lastVisibleItemIndex")
                }
        }
    */

        var isLoading by remember { mutableStateOf(false) }

        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .collectLatest { lastVisibleItemIndex ->
                    if (!isLoading && lastVisibleItemIndex == blogsListState.size - 1) {
                        isLoading = true
                        loadNextPage {
                            isLoading = false
                        }
                    }
                }
        }

    }

    @Composable
    fun BlogItem(
      //  profileImageUrl: String,
        blogTitle: String,
        blogId: String,
        blog: Blog,
        position: Int,
        mediaContent: @Composable () -> Unit,
        shop_map_id : String,
        onClick: () -> Unit,
        showNowVisible: Boolean = true,
    ) {
        var showPopup by remember { mutableStateOf(false) }
        var iconPosition by remember { mutableStateOf(Offset.Zero) }
        var isFavourite by remember { mutableStateOf(blog.isFavourite == 1) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = sdp(5), vertical = sdp(5))
                .shadow(sdp(4), shape = RoundedCornerShape(sdp(18)))
                .background(Color.White, shape = RoundedCornerShape(sdp(18)))
                .clickable { onClick() }
        ) {
            // Top section with avatar, title, and action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = sdp(6), horizontal = sdp(10)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(sdp(38))
                            .clip(CircleShape)
                            .background(colorResource(id = R.color.start_theme_color)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.woloo_powder_room_logo),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(
                                    sdp(
                                        30
                                    )
                                )
                                .clip(CircleShape)

                        )
                    }
                    Spacer(modifier = Modifier.width(sdp(10)))
                    Text(
                        text = "Woloo Powder Room ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = CenturyGothic,
                            fontWeight = FontWeight.Bold,
                            fontSize = sdp(10).value.sp
                        )
                    )
                }

             /*   Row(verticalAlignment = Alignment.CenterVertically) {
                    if (showNowVisible) {
                        Text(
                            text = "Shop Now",
                            modifier = Modifier
                                .background(Color.Yellow, shape = RoundedCornerShape(sdp(4)))
                                .padding(horizontal = sdp(10), vertical = sdp(6))
                                .clickable {

                                    storeViewModel!!.getProductDetailsWithPrice(
                                        shop_map_id,
                                        "*variants.calculated_price,+variants.inventory_quantity,*categories",
                                        SharedPrefSettings.getPreferences.fetchRegionId().toString()
                                    )


                                },
                            style = MaterialTheme.typography.labelMedium.copy( fontFamily = CenturyGothic,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(sdp(5)))
                    }
                   *//* Icon(
                        painter = painterResource(id = R.drawable.show_more),
                        contentDescription = "More",
                        modifier = Modifier.size(sdp(12))
                            .clickable { showPopup = true}
                    )*//*
                }*/
            }

            if (showPopup) {
                Popup(
                    alignment = Alignment.TopEnd,
                    offset = IntOffset(
                        iconPosition.x.toInt(),
                        iconPosition.y.toInt() + sdp(12).value.toInt()
                    ), // Open just below the icon
                    onDismissRequest = { showPopup = false }
                ) {
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = sdp(4)),
                        shape = RoundedCornerShape(sdp(8)),
                        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white_95)),
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(sdp(5))
                            .clickable {
                                showPopup = false
                                // Handle Block click
                                println("Blocked")
                                var request = BlockBlogRequest()
                                request.blog_id = blogId
                                blogViewModel!!.blockBlog(request)
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = sdp(16), vertical = sdp(10))
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.block_blog), // Replace with your block icon
                                contentDescription = "Block",
                                modifier = Modifier.size(sdp(18)),
                                tint = Color.Black
                            )

                            Spacer(modifier = Modifier.width(sdp(10)))

                            Text(
                                text = "Block",
                                fontFamily = CenturyGothic,
                                fontWeight = FontWeight.Bold,
                                fontSize = sdp(11).value.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            // Media content (image/video/multi-image)
            Box(
                modifier = Modifier
                    .padding(horizontal = sdp(0))
                    .clip(RoundedCornerShape(sdp(0)))

            ) {
                mediaContent()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = sdp(10)),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
            // Action icons (like, comment, share)
            Row(
                modifier = Modifier
                    .padding(vertical = sdp(10), horizontal = sdp(15)),
                horizontalArrangement = Arrangement.spacedBy(sdp(20))
            ) {
                Icon(painter = painterResource(
                    id = if (isFavourite) R.drawable.favorite_blogs_icon else R.drawable.like_blog
                ), contentDescription = "Like",
                    modifier = Modifier.size(sdp(20))
                        .clickable {
                            onClickBlogFavourite(blog, position)
                            isFavourite = !isFavourite
                        },
                            tint = Color.Unspecified
                )
                Icon(painter = painterResource(R.drawable.comment_blog),
                    contentDescription = "Comment",
                    modifier = Modifier.size(sdp(20))
                        .clickable {
                            val bottomSheetFragment = CommentsPopup.newInstance(blogId)
                            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
                        }
                )
                Icon(painter = painterResource(R.drawable.share_blog), contentDescription = "Share",
                    modifier = Modifier.size(sdp(20))
                        .clickable {
                            onClickBlogShare(blog)
                        }
                )

            }
            Row(
                modifier = Modifier
                    .background(
                        color = colorResource(id = R.color.start_theme_color),
                        shape = RoundedCornerShape(sdp(5))
                    )
                    .padding(horizontal = sdp(5), vertical = sdp(1)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.coins_points),
                    contentDescription = "Coins",
                    modifier = Modifier
                        .size(sdp(20))
                        .padding(end = sdp(3))
                )

                Text(
                    text = "10",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = colorResource(id = R.color.subscription_dialog_button_color),
                        fontFamily = CenturyGothic,
                        fontWeight = FontWeight.Bold,
                        fontSize = sdp(15).value.sp
                    ),
                    modifier = Modifier
                        .padding(end = sdp(5), top =sdp(5) , bottom = sdp(5) )
                )
            }
        }
            // Blog title
            Text(
                text = blogTitle,
                modifier = Modifier
                    .padding(horizontal = sdp(15), vertical = sdp(10))
                    .widthIn(max = Dp.Infinity),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = CenturyGothicRegular,
                    fontSize = sdp(11).value.sp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

        }
    }


    @Composable
    fun MultiImageGrid(images: List<String>) {
        val maxDisplay = 4
        val displayImages = images.take(maxDisplay)
        val extraImagesCount = images.size - maxDisplay

        Column(modifier = Modifier.fillMaxWidth()) {
            when (images.size) {
                1 -> {
                    GridImageItem(
                        imageUrl = BuildConfig.NODE_API_URL + images[0],
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(sdp(297))
                    )
                }

                2 -> {
                    images.forEach { imageUrl ->
                        GridImageItem(
                            imageUrl = BuildConfig.NODE_API_URL + imageUrl,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(sdp(148))
                                .padding(vertical = sdp(3))
                        )
                    }
                }

                3 -> {
                    // 1st image full width on top
                    GridImageItem(
                        imageUrl = BuildConfig.NODE_API_URL + images[0],
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(sdp(148))
                    )

                    Spacer(modifier = Modifier.height(sdp(5)))

                    // Bottom two side-by-side
                    Row(modifier = Modifier.fillMaxWidth()) {
                        for (i in 1..2) {
                            val modifier = Modifier
                                .weight(1f)
                                .height(sdp(148))
                                .then(
                                    if (i == 1)
                                        Modifier.padding(end = sdp(3))
                                    else
                                        Modifier.padding(start = sdp(3))
                                )

                            GridImageItem(
                                imageUrl = BuildConfig.NODE_API_URL + images[i],
                                modifier = modifier
                            )
                        }
                    }
                }

                else -> {
                    // First Row (0, 1)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        for (i in 0 until 2) {
                            val modifier = Modifier
                                .weight(1f)
                                .height(sdp(148))
                                .then(
                                    if (i == 0)
                                        Modifier.padding(end = sdp(3))
                                    else
                                        Modifier.padding(start = sdp(3))
                                )

                            GridImageItem(
                                imageUrl = BuildConfig.NODE_API_URL + displayImages[i],
                                modifier = modifier
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(sdp(5)))

                    // Second Row (2, 3)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        for (i in 2 until displayImages.size) {
                            val modifier = Modifier
                                .weight(1f)
                                .height(sdp(148))
                                .then(
                                    if (i == 2)
                                        Modifier.padding(end = sdp(3))
                                    else
                                        Modifier.padding(start = sdp(3))
                                )

                            if (i == 3 && extraImagesCount > 0) {
                                Box(
                                    modifier = modifier.clip(RoundedCornerShape(sdp(0))),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(BuildConfig.NODE_API_URL + displayImages[i]),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .matchParentSize()
                                            .clip(RoundedCornerShape(sdp(0)))
                                    )
                                    Box(
                                        modifier = Modifier
                                            .matchParentSize()
                                            .background(Color.Black.copy(alpha = 0.5f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "+$extraImagesCount",
                                            color = Color.White,
                                            fontFamily = CenturyGothic,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = sdp(20).value.sp
                                        )
                                    }
                                }
                            } else {
                                GridImageItem(
                                    imageUrl = BuildConfig.NODE_API_URL + displayImages[i],
                                    modifier = modifier
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun GridImageItem(imageUrl: String, modifier: Modifier) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = modifier
                .clip(RoundedCornerShape(sdp(0)))
        )
    }

   /* @OptIn(UnstableApi::class)
    @Composable
    fun VideoPlayer(url: String) {
        val context = LocalContext.current
        val exoPlayer = remember {
            ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(url))
                prepare()
                playWhenReady = false
            }
        }

        LaunchedEffect(Unit) {
            exoPlayer.playWhenReady = true
        }

        DisposableEffect(Unit) {
            onDispose {
                exoPlayer.release()
            }
        }


        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(sdp(297)) // Match height as in image layout
        )
    }
*/
   @OptIn(UnstableApi::class)
   @Composable
   fun VideoPlayer(url: String) {
       val context = LocalContext.current

       val exoPlayer = remember {
           val renderersFactory = DefaultRenderersFactory(context)
               .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF)
               .setEnableDecoderFallback(true)

           val trackSelector = DefaultTrackSelector(context).apply {
               parameters = buildUponParameters()
                   .setMaxVideoSize(1920, 1080)
                   .build()
           }

           ExoPlayer.Builder(context, renderersFactory)
               .setTrackSelector(trackSelector)
               .build().apply {
               setMediaItem(MediaItem.fromUri(url))
               prepare()
               playWhenReady = false
           }
       }

       // Start playback when composition enters
       LaunchedEffect(Unit) {
           exoPlayer.playWhenReady = true
       }

       // Release player when composition leaves
       DisposableEffect(Unit) {
           onDispose {
               exoPlayer.release()
           }
       }

       AndroidView(
           factory = {
               PlayerView(it).apply {
                   player = exoPlayer
                   useController = false
                   resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                   layoutParams = FrameLayout.LayoutParams(
                       ViewGroup.LayoutParams.MATCH_PARENT,
                       ViewGroup.LayoutParams.MATCH_PARENT
                   )
               }
           },
           modifier = Modifier
               .fillMaxWidth()
               .height(sdp(297))
       )

       exoPlayer.addListener(object : Player.Listener {
           override fun onPlayerError(error: PlaybackException) {
               Log.e("ExoPlayerError", "Playback failed: ${error.message}", error)
               // Show fallback UI or retry lower-quality stream
           }
       })
   }

   /* @OptIn(UnstableApi::class)
    @Composable
    fun VideoPlayer(url: String) {
        val context = LocalContext.current
        var safeUrl by remember { mutableStateOf<String?>(null) }
        var showError by remember { mutableStateOf(false) }

        LaunchedEffect(url) {
            withContext(Dispatchers.IO) {
                if (context.isVideoPlayable(url)) {
                    safeUrl = url
                } else {
                    showError = true
                    // Optional: start FFmpeg transcoding here
                }
            }
        }

        when {
            showError -> {
                Text(
                    text = "This video is not supported.",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            safeUrl != null -> {
                val exoPlayer = remember {
                    val renderersFactory = DefaultRenderersFactory(context)
                        .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF)
                        .setEnableDecoderFallback(true)

                    val trackSelector = DefaultTrackSelector(context).apply {
                        parameters = buildUponParameters()
                            .setMaxVideoSize(1920, 1080)
                            .build()
                    }

                    ExoPlayer.Builder(context, renderersFactory)
                        .setTrackSelector(trackSelector)
                        .build().apply {
                            setMediaItem(MediaItem.fromUri(safeUrl!!))
                            prepare()
                            playWhenReady = false
                        }
                }

                LaunchedEffect(Unit) {
                    exoPlayer.playWhenReady = true
                }

                DisposableEffect(Unit) {
                    onDispose { exoPlayer.release() }
                }

                AndroidView(
                    factory = {
                        PlayerView(it).apply {
                            player = exoPlayer
                            useController = false
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                            layoutParams = FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(sdp(297))
                )

                exoPlayer.addListener(object : Player.Listener {
                    override fun onPlayerError(error: PlaybackException) {
                        Log.e("ExoPlayerError", "Playback failed: ${error.message}", error)
                    }
                })
            }

            else -> {
                // Show loading indicator
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
    }*/



    @Composable
    fun sdp(dpValue: Int): Dp {
        val context = LocalContext.current
        val density = LocalDensity.current
        val dimenId = remember(dpValue) {
            context.resources.getIdentifier("_${dpValue}sdp", "dimen", context.packageName)
        }
        return if (dimenId != 0) {
            with(density) { context.resources.getDimension(dimenId).toDp() }
        } else {
            dpValue.dp // fallback
        }
    }

    fun Int.toComposeColor(): Color = Color(this)


    fun loadNextPage(onComplete: () -> Unit) {
        if(blogsListState.size % 9 == 0) {
            page += 1
            blogViewModel!!.getBlogsForShopByCategory(
                category = categoryString,
                page = page,
                blogType = AppConstants.BLOG_CAT_TYPE_ONE
            )
            Log.d("Pagination", "Reached bottom at index $page")
            onComplete()
        }
        Log.d("Pagination out ", "Reached bottom at index $page")
    }



    fun onClickBlogFavourite(blog: Blog, position: Int) {
        blogs[position].isFavourite = if (blog.isFavourite == 0) 1 else 0
        blogViewModel!!.ctaFavourite(blog.id.toString())

    }

   fun onClickBlogShare(blog: Blog) {
        if (TextUtils.isEmpty(blog.title) || TextUtils.isEmpty(blog.shortLink)) {
            return
        }
        val share = Intent(Intent.ACTION_SEND)
        share.setType("text/plain")
        share.putExtra(
            Intent.EXTRA_TEXT, """
     ${blog.title}
     ${blog.shortLink}
     """.trimIndent()
        )
        startActivity(Intent.createChooser(share, null))
    }

    val CenturyGothic = FontFamily(
        Font(R.font.gothic)
    )

    val CenturyGothicRegular = FontFamily(
        Font(R.font.century_gothic)
    )

    fun Context.isVideoPlayable(url: String): Boolean {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(url, HashMap())
            val width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toIntOrNull() ?: 0
            val height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toIntOrNull() ?: 0
            retriever.release()

            width <= 1920 && height <= 1080
        } catch (e: Exception) {
            Log.e("VideoCheck", "Failed to check video resolution", e)
            false // If check fails, treat as not playable
        }
    }

    fun showSuccessDialog(context: Context , currentPage : Int)  {

        try {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_coins_success)
            val displayMetrics = Resources.getSystem().displayMetrics
            val screenWidth = displayMetrics.widthPixels

// Calculate 80% of screen width
            val dialogWidth = (screenWidth).toInt()

// Apply the calculated width and wrap_content height to the dialog window
            dialog.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.CENTER)
            val btnCloseDialog = dialog.findViewById<View>(R.id.tv_go_back_to_home) as TextView
            val gifImageView = dialog.findViewById<View>(R.id.gifImageView) as ImageView

            val btnSuccessTextDialog = dialog.findViewById<View>(R.id.tv_logout) as TextView
            btnSuccessTextDialog.setText("Woohoo! You Earned 10 Woloo Points!")
            Glide.with(this)
                .load(R.drawable.coins_animate) // your gif in res/drawable
                .into(gifImageView)

            btnCloseDialog.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }

            dialog.setOnDismissListener {
                // Clear old list if required
                blogs.clear()
                categories.clear()
                blogsListState.clear()

                // Call API again
                blogViewModel?.getBlogsForUserByCategory("all", currentPage, AppConstants.BLOG_CAT_TYPE_ONE)
            }


            dialog.show()
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }




    }



    companion object {
        const val TAG = "ContentCommerceComposeFragment"
    }
}