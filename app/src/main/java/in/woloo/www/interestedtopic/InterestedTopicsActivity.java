package in.woloo.www.interestedtopic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.boarding.BoardingActivity;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.interestedtopic.adapter.InterestedTopicAdapter;
import in.woloo.www.interestedtopic.mvp.InterestedTopicPresenter;
import in.woloo.www.interestedtopic.mvp.InterestedTopicView;
import in.woloo.www.search.SearchWolooResponse;
import in.woloo.www.trendingblog.model.CategoriesResponse;
import in.woloo.www.utils.Logger;
import in.woloo.www.v2.blog.viewmodel.BlogViewModel;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.woloo.viewmodel.WolooViewModel;

public class InterestedTopicsActivity extends AppCompatActivity implements InterestedTopicAdapter.OnItemCheckListener {
    private static final String TAG = InterestedTopicsActivity.class.getSimpleName();
    @BindView(R.id.interestedScreentopImage)
    ImageView screenTopImage;

    @BindView(R.id.interestedScreenTitle)
    TextView screenTitle;

    @BindView(R.id.interestedTopicRecyclerview)
    RecyclerView allTopicRecyclerview;

    @BindView(R.id.interestedScreenNext)
    TextView screenNextText;

    private InterestedTopicAdapter interestedTopicAdapter;
//    private InterestedTopicPresenter presenter;
    BlogViewModel blogViewModel;
    private List<CategoriesResponse.Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_topics);
        Logger.i(TAG, "onCreate");
        ButterKnife.bind(this);
        blogViewModel = new ViewModelProvider(this).get(BlogViewModel.class);

        setLiveData();
        blogViewModel.getCategories();

//        presenter.getCategories();
    }

    void setLiveData(){
        blogViewModel.observeGetCategories().observe(this, new Observer<BaseResponse<ArrayList<CategoriesResponse.Category>>>() {
            @Override
            public void onChanged(BaseResponse<ArrayList<CategoriesResponse.Category>> response) {
                if(response != null && response.getData() != null && !response.getData().isEmpty()){
                    categories = response.getData();
                    Log.d(TAG, "onChanged: $categories");
                    interestedTopicAdapter = new InterestedTopicAdapter(getApplicationContext(), categories);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3, GridLayoutManager.VERTICAL, false);
                    allTopicRecyclerview.setLayoutManager(gridLayoutManager);
                    allTopicRecyclerview.setAdapter(interestedTopicAdapter);
                }else {
                    WolooApplication.setErrorMessage("");
                }
            }
        });

        blogViewModel.observeSaveUserCategory().observe(this, new Observer<BaseResponse<JSONObject>>() {
            @Override
            public void onChanged(BaseResponse<JSONObject> response) {
                if (response != null && response.getData() != null) {
                    onSaveUserCategories();
                } else {
                    WolooApplication.setErrorMessage("");
                }
            }
        });
    }

//    @Override
//    public void getCategories(CategoriesResponse categoriesResponse) {
////        categories = categoriesResponse.getData().getCategories();
////        interestedTopicAdapter = new InterestedTopicAdapter(this, categories);
////        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
////        allTopicRecyclerview.setLayoutManager(gridLayoutManager);
////        allTopicRecyclerview.setAdapter(interestedTopicAdapter);
//    }


    public void onSaveUserCategories() {
        Logger.i("LOG", "saved");
        startActivity(new Intent(this, WolooDashboard.class));
        finish();
    }

    @Override
    public void onItemClick(int position) {
        Logger.i("LOG", categories.get(position).getCategoryName());
        CategoriesResponse.Category category = categories.get(position);
        category.setSelected(!category.isSelected());
        interestedTopicAdapter.notifyItemChanged(position);
        /*int length = getSelectedCategories().length();

        if (length > 0) {
            screenNextText.setEnabled(true);
        } else {
            screenNextText.setEnabled(false);
        }*/
    }

    @OnClick(R.id.interestedScreenNext)
    protected void onClickNext() {
        ArrayList<Integer> categoryList = new ArrayList<>();
        if (categories != null && categories.size() == 0) {
            Toast.makeText(this, "Select at least one category to proceed", Toast.LENGTH_SHORT).show();
        }else if (categories != null && categories.size() > 0) {
            for (CategoriesResponse.Category category : categories) {
                if (category.isSelected()) {
                    Logger.i("TAG", category.getId() + "");
                    categoryList.add(category.getId());
                }
            }
            blogViewModel.saveUserCategory(categoryList);
        } else {
            onSaveUserCategories();
        }
//        JSONArray selectedCategories = getSelectedCategories();
//        if (selectedCategories != null && selectedCategories.length() == 0) {
//            Toast.makeText(this, "Select at least one category to proceed", Toast.LENGTH_SHORT).show();
//            return;
//        } else if (selectedCategories != null && selectedCategories.length() > 0) {
//            presenter.saveUserCategories(selectedCategories);
//
//        } else {
//            onSaveUserCategories();
//        }
    }

    private JSONArray getSelectedCategories() {
        JSONArray selectedCategories = new JSONArray();
        if (categories != null && categories.size() > 0) {
            for (CategoriesResponse.Category category : categories) {
                if (category.isSelected()) {
                    Logger.i("TAG", category.getId() + "");
                    selectedCategories.put(category.getId());
                }
            }
            return selectedCategories;
        } else {
            return null;
        }
    }
}