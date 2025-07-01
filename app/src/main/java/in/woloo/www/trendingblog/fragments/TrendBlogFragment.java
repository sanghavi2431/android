package in.woloo.www.trendingblog.fragments;

import android.Manifest;
import android.app.Dialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.epoxy.EpoxyRecyclerView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.dashboard.ui.home.HomeFragment;
import in.woloo.www.more.models.UserProfileMergedResponse;
import in.woloo.www.my_account.MyAccountFragment;
import in.woloo.www.search.SearchWolooActivity;
import in.woloo.www.shopping.fragments.ShoppingFragment;
import in.woloo.www.trendingblog.adapter.utils.EndlessRecyclerViewScrollListener;
import in.woloo.www.trendingblog.epoxy.BlogController;
import in.woloo.www.trendingblog.model.CategoriesResponse;
import in.woloo.www.trendingblog.model.NearByWolooAndOfferCountResponse;
import in.woloo.www.trendingblog.model.blog.Blog;
import in.woloo.www.trendingblog.model.blog.Category;
import in.woloo.www.trendingblog.mvp.NearByWolooAndOfferCountPresenter;
import in.woloo.www.trendingblog.mvp.NearByWolooAndOfferCountView;
import in.woloo.www.utils.Logger;
import in.woloo.www.v2.blog.model.BlogByCategoryResponse;
import in.woloo.www.v2.blog.viewmodel.BlogViewModel;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.data.remote.MessageResponse;
import in.woloo.www.v2.home.model.NearByWolooAndOfferCount;
import in.woloo.www.v2.home.model.NearByWolooAndOfferCountRequest;
import in.woloo.www.v2.home.viewmodel.HomeViewModel;
import in.woloo.www.v2.profile.model.UserProfile;
import in.woloo.www.v2.profile.viewmodel.ProfileViewModel;
import in.woloo.www.v2.search.SearchActivity;
import in.woloo.www.webview.WebViewFragment;

public class TrendBlogFragment extends Fragment implements NearByWolooAndOfferCountView, BlogController.OnClickBlogViewItems {
    public static final String TAG = "TrendingBlogFragment";
    @BindView(R.id.epoxyRV)
    EpoxyRecyclerView epoxyRecyclerView;

    private NearByWolooAndOfferCountPresenter nearByWolooAndOfferCountPresenter;
    private HomeViewModel homeViewModel;
    private ProfileViewModel profileViewModel;
    private BlogViewModel blogViewModel;

    private BlogController blogController;
    private List<Category> categories = new ArrayList<>();
    private List<Blog> blogs = new ArrayList<>();
    private boolean isCategoryLoaded = false;
    private FusedLocationProviderClient fusedLocationClient;
    //private LoadMoreRecyclerViewScrollListener loadMoreScrollListener;
    private EndlessRecyclerViewScrollListener loadMoreScrollListener;
    private int page = 1;
    private int previousPage = page;
    private String category = "all";
    private Blog currentBlog = null;

    public TrendBlogFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((WolooDashboard) requireActivity()).hideToolbar();
        View rootView = inflater.inflate(R.layout.fragment_trend_blog, container, false);
        ButterKnife.bind(this, rootView);

        nearByWolooAndOfferCountPresenter = new NearByWolooAndOfferCountPresenter(getContext(), this);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        blogViewModel = new ViewModelProvider(this).get(BlogViewModel.class);

        getNearbyWolooAndOffers();
        setLiveData();
        //nearByWolooAndOfferCountPresenter.getBlogCategories();
//        nearByWolooAndOfferCountPresenter.getBlogs(category, page);
        blogViewModel.getBlogsForUserByCategory(category, page);
        profileViewModel.getUserProfile();
        // Epoxy
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity());
        epoxyRecyclerView.setLayoutManager(linearLayoutManager);
        /*loadMoreScrollListener = new LoadMoreRecyclerViewScrollListener((LinearLayoutManager) epoxyRecyclerView.getLayoutManager()) {
            @Override
            public void fetchNextPage() {
                Log.i(TAG, "fetchNextPage");
                page++;
                nearByWolooAndOfferCountPresenter.getBlogs("all", page);
            }
        };*/
        loadMoreScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int p, int totalItemsCount, RecyclerView view) {
                Logger.d(TAG, "onLoadMore " + page + " " + totalItemsCount);
                if (page > previousPage) {
//                    nearByWolooAndOfferCountPresenter.getBlogs(category, page);
                    blogViewModel.getBlogsForUserByCategory(category, page);
                    previousPage = page;
                }
            }
        };
        epoxyRecyclerView.addOnScrollListener(loadMoreScrollListener);
        blogController = new BlogController(requireActivity(), this);
        epoxyRecyclerView.setController(blogController);
        return rootView;
    }

    private void setLiveData() {
        homeViewModel.observeNearByWolooAndOfferCount().observe(getViewLifecycleOwner(), new Observer<BaseResponse<NearByWolooAndOfferCountResponse.Data>>() {
            @Override
            public void onChanged(BaseResponse<NearByWolooAndOfferCountResponse.Data> response) {
                if(response != null && response.getData() != null) {
                    blogController.setNearbyWolooAndOffer(response.getData());
                }
            }
        });

        profileViewModel.observeUserProfile().observe(getViewLifecycleOwner(), new Observer<BaseResponse<UserProfile>>() {
            @Override
            public void onChanged(BaseResponse<UserProfile> response) {
                if(response != null && response.getData() != null) {
                    blogController.setUserProfileDetails(response.getData());
                }
            }
        });

        blogViewModel.observeCtaFavourite().observe(getViewLifecycleOwner(), new Observer<BaseResponse<JSONObject>>() {
            @Override
            public void onChanged(BaseResponse<JSONObject> response) {
                if(response != null && response.getData() != null){
                    onFavouriteABlog();
                }
            }
        });

        blogViewModel.observeCtaLikes().observe(getViewLifecycleOwner(), new Observer<BaseResponse<JSONObject>>() {
            @Override
            public void onChanged(BaseResponse<JSONObject> response) {
                if(response != null && response.getData() != null){
                    onLikeABlog();
                }
            }
        });

        blogViewModel.observeCtaBlogRead().observe(getViewLifecycleOwner(), new Observer<BaseResponse<JSONObject>>() {
            @Override
            public void onChanged(BaseResponse<JSONObject> response) {
                if(response != null && response.getData() != null){
                    onReadABlog();
                }
            }
        });

        blogViewModel.observeBlogReadPoint().observe(getViewLifecycleOwner(), new Observer<BaseResponse<MessageResponse>>() {
            @Override
            public void onChanged(BaseResponse<MessageResponse> response) {
                if(response != null && response.getData() != null){
                    onBlogReadPointsAdded();
                }
            }
        });

        blogViewModel.observeBlogsForUserByCategory().observe(getViewLifecycleOwner(), new Observer<BaseResponse<BlogByCategoryResponse>>() {
            @Override
            public void onChanged(BaseResponse<BlogByCategoryResponse> response) {
                if(response != null){
                if (response.getSuccess()) {
                    // Set categories
                    if (!isCategoryLoaded) {
                        Category categoryAll = new Category();
                        categoryAll.setId(0);
                        categoryAll.setCategoryName("All");

                        categories.add(0, categoryAll);
                        categories.addAll(response.getData().getCategories());
                        blogController.setCategories(categories, 0);
                        isCategoryLoaded = true;
                    }

                    // Set blogs
                    List<Blog> blogsTemp = response.getData().getBlogs();
                    if (blogsTemp != null) {
                        if (blogsTemp.size() == 10) {
                            page++;
                        }
                        //loadMoreScrollListener.setHasMoreToLoad(blogs.size() == 10);
                        blogs.addAll(blogsTemp);
                        blogController.setBlogItems(blogs);
                    }
                }
                }
            }
        });
    }

    private void getNearbyWolooAndOffers() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireActivity(), R.string.location_validation, Toast.LENGTH_SHORT).show();
        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
//                        nearByWolooAndOfferCountPresenter.getNearByWolooAndOffer(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                        NearByWolooAndOfferCountRequest request = new NearByWolooAndOfferCountRequest();
                        request.setLat(location.getLatitude());
                        request.setLng(location.getLongitude());
                        homeViewModel.getNearByWolooAndOfferCount(request);
                    }
                }
            });
        }
    }

    @Override
    public void nearByWolooAndOfferCountResponse(NearByWolooAndOfferCountResponse nearByWolooAndOfferCountResponse) {
        if (nearByWolooAndOfferCountResponse.getStatus().equals("success")) {
            blogController.setNearbyWolooAndOffer(nearByWolooAndOfferCountResponse.getData());
        }
    }

    @Override
    public void getCategories(CategoriesResponse categoriesResponse) {
        /*if (categoriesResponse.getStatus().equals("success")) {
            CategoriesResponse response = new CategoriesResponse();
            CategoriesResponse.Category categoryAll = response.new Category();
            categoryAll.setId(0);
            //categoryAll.setSelected(true);
            categoryAll.setCategoryName("All");

            categories.add(0, categoryAll);
            categories.addAll(categoriesResponse.getData().getCategories());
            blogController.setCategories(categories, 0);
        }*/
    }

    @Override
    public void getBlogs(in.woloo.www.trendingblog.model.blog.BlogsResponse blogsResponse) {
        if (blogsResponse.getStatus().equals("success")) {
            // Set categories
            if (!isCategoryLoaded) {
                Category categoryAll = new Category();
                categoryAll.setId(0);
                categoryAll.setCategoryName("All");

                categories.add(0, categoryAll);
                categories.addAll(blogsResponse.getData().getCategories());
                blogController.setCategories(categories, 0);
                isCategoryLoaded = true;
            }

            // Set blogs
            List<Blog> blogs = blogsResponse.getData().getBlogs();
            if (blogs != null) {
                if (blogs.size() == 10) {
                    page++;
                }
                //loadMoreScrollListener.setHasMoreToLoad(blogs.size() == 10);
                this.blogs.addAll(blogs);
                blogController.setBlogItems(this.blogs);
            }
        }
    }

    @Override
    public void onFavouriteABlog() {
        blogController.setBlogItems(blogs);
    }

    @Override
    public void onLikeABlog() {
        blogController.setBlogItems(blogs);
    }

    @Override
    public void onReadABlog() {
        if(currentBlog != null) {
//            nearByWolooAndOfferCountPresenter.addBlogReadPoints(currentBlog);
            blogViewModel.blogReadPoint(currentBlog.getId().toString());
        }
    }

    @Override
    public void onBlogReadPointsAdded() {
        blogController.setBlogItems(blogs);
        navigateToWebView();
    }

    @Override
    public void setUserProfileMergedResponse(UserProfileMergedResponse userProfileMergedResponse) {

    }

    public void offerPopUpwindow() {
        final Dialog dialog = new Dialog(getContext(), R.style.CustomAlertDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.transparent)));
        dialog.setContentView(R.layout.feature_benefits_offer_popup);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        ImageView cancelImg = dialog.findViewById(R.id.ivClose);
        View visitOurWoloo = dialog.findViewById(R.id.visitWolooButtonOffer);
        cancelImg.setOnClickListener(v -> dialog.dismiss());
        visitOurWoloo.setOnClickListener(view -> {
            dialog.dismiss();
            if (requireActivity() instanceof WolooDashboard) {
                ((WolooDashboard) requireActivity()).loadFragment(new HomeFragment(), "HomeFragment");
                ((WolooDashboard) requireActivity()).changeIcon(((WolooDashboard) requireActivity()).nav_view.getMenu().findItem(R.id.navigation_home));
            }

        });
        dialog.show();
    }

    @Override
    public void onClickLocateLoo() {
        offerPopUpwindow();
    }

    @Override
    public void onClickBlogItem(Blog blog, int position) {
        /*int colorInt = Color.parseColor("#FFFFFF");
        CustomTabColorSchemeParams defaultColors = new CustomTabColorSchemeParams.Builder()
                .setToolbarColor(colorInt)
                .build();

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setDefaultColorSchemeParams(defaultColors);
        CustomTabsIntent customTabsIntent = builder.build();

        customTabsIntent.launchUrl(requireActivity(), Uri.parse(blog.getDetailedBlogLink()));*/
        currentBlog = blog;
        if (requireActivity() instanceof WolooDashboard) {
            if (blog.getIsBlogRead() == 0) {// Not yet read
                blogs.get(position).setIsBlogRead(1);
//                nearByWolooAndOfferCountPresenter.readABlog(blog);
                blogViewModel.ctaBlogRead(blog.getId().toString());

            } else {
                navigateToWebView();
            }
        }
    }

    private void navigateToWebView() {
        if (currentBlog != null) {
            WebViewFragment webViewFragment = WebViewFragment.newInstance(currentBlog.getTitle(), currentBlog.getShortLink() , TrendBlogFragment.TAG);
            ((WolooDashboard) requireActivity()).loadMenuFragment(webViewFragment, WebViewFragment.TAG);
            ((WolooDashboard) requireActivity()).changeIcon(((WolooDashboard) requireActivity()).nav_view.getMenu().findItem(R.id.navigation_home));
        }
    }

    @Override
    public void onClickBlogLike(Blog blog, int position) {
        blogs.get(position).setIsLiked((blog.getIsLiked() == 0) ? 1 : 0);
//        nearByWolooAndOfferCountPresenter.likeABlog(blog);
        blogViewModel.ctaLikes(blog.getId().toString());
    }

    @Override
    public void onClickBlogFavourite(Blog blog, int position) {
        blogs.get(position).setIsFavourite((blog.getIsFavourite() == 0) ? 1 : 0);
//        nearByWolooAndOfferCountPresenter.favouriteABlog(blog);
        blogViewModel.ctaFavourite(blog.getId().toString());
    }

    @Override
    public void onClickBlogShare(Blog blog, int position) {
        if (TextUtils.isEmpty(blog.getTitle()) || TextUtils.isEmpty(blog.getShortLink())) {
            return;
        }
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, blog.getTitle() + "\n" + blog.getShortLink());
        startActivity(Intent.createChooser(share, null));
    }

    @Override
    public void onClickPagerItemWolooLocation() {
        if (requireActivity() instanceof WolooDashboard) {
            ((WolooDashboard) requireActivity()).loadFragment(new HomeFragment(), "HomeFragment");
            ((WolooDashboard) requireActivity()).changeIcon(((WolooDashboard) requireActivity()).nav_view.getMenu().findItem(R.id.navigation_home));
        }
    }

    @Override
    public void onClickPagerItemWolooOffers() {
        Intent intent = new Intent(requireActivity(), SearchActivity.class);
//        intent.putExtra(SearchWolooActivity.ARG_SHOW_OFFERS, true);
        requireActivity().startActivity(intent);
    }

    @Override
    public void onClickPagerItemShopCoupon(String code) {
        Logger.i(TAG, code);
        ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("woloo_coupon", code);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(requireActivity(), "Coupon copied!", Toast.LENGTH_SHORT).show();
        if (requireActivity() instanceof WolooDashboard) {
            ((WolooDashboard) requireActivity()).loadFragment(ShoppingFragment.newInstance("coupon", code), "ShoppingFragment");
            ((WolooDashboard) requireActivity()).changeIcon(((WolooDashboard) requireActivity()).nav_view.getMenu().findItem(R.id.navigation_home));
        }
    }

    @Override
    public void onSelectCategory(int position) {
        Logger.i(TAG, position + "");
        /*for (CategoriesResponse.Category category : categories) {
            category.setSelected(false);
        }
        categories.get(position).setSelected(true);*/
        loadMoreScrollListener.resetState();
        page = 1;
        previousPage = page;
        blogController.setCategories(categories, position);
        category = (position == 0) ? "all" : String.valueOf(categories.get(position).getId());
        blogs.clear();
//        nearByWolooAndOfferCountPresenter.getBlogs(category, page);
        blogViewModel.getBlogsForUserByCategory(category, page);
    }

    @Override
    public void onClickShop() {
        if (requireActivity() instanceof WolooDashboard) {
            ((WolooDashboard) requireActivity()).loadFragment(new ShoppingFragment(), "ShoppingFragment");
            ((WolooDashboard) requireActivity()).changeIcon(((WolooDashboard) requireActivity()).nav_view.getMenu().findItem(R.id.navigation_home));
        }
    }

    @Override
    public void onClickUserThumb() {
        if (requireActivity() instanceof WolooDashboard) {
            ((WolooDashboard) requireActivity()).loadFragment(MyAccountFragment.newInstance(false), MyAccountFragment.TAG);
            ((WolooDashboard) requireActivity()).changeIcon(((WolooDashboard) requireActivity()).nav_view.getMenu().findItem(R.id.navigation_chatty_cat));
        }
    }
}