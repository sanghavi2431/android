package in.woloo.www.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;

public class HomeCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private int NEAREST_WALKS_VIEW = 100, SHOP_CATEGORY_VIEW = 200, NEWS_VIEW = 300,ARTICALS_VIEW = 400;
    private NearestWalkViewHolder nearestWalkViewHolder;
    private List<NearByStoreResponse.Data> nearByStoreResponseList = new ArrayList<NearByStoreResponse.Data>();

    public HomeCategoryAdapter(Context context,List<NearByStoreResponse.Data> nearByStoreResponseList) {
        this.context = context;
        this.nearByStoreResponseList = nearByStoreResponseList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(viewType == NEAREST_WALKS_VIEW){
            View listItem= layoutInflater.inflate(R.layout.nearest_walk_item, parent, false);
            return new NearestWalkViewHolder(listItem);
        }else if(viewType == SHOP_CATEGORY_VIEW){
            View listItem= layoutInflater.inflate(R.layout.shop_category_item, parent, false);
            return new ShopCategoryViewHolder(listItem);
        }else if(viewType == NEWS_VIEW){
            View listItem= layoutInflater.inflate(R.layout.news_item, parent, false);
            return new NewsViewHolder(listItem);
        }else if(viewType == ARTICALS_VIEW){
            View listItem= layoutInflater.inflate(R.layout.articals_item, parent, false);
            return new ArticalsViewHolder(listItem);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try{
            if(holder != null){
                if(holder instanceof NearestWalkViewHolder){
                    try{
                      ((NearestWalkViewHolder) holder).setData();
                    }catch (Exception ex){
                         CommonUtils.printStackTrace(ex);
                    }
                }else if(holder instanceof ShopCategoryViewHolder){
                    try{
                        ((ShopCategoryViewHolder) holder).setData();
                    }catch (Exception ex){
                         CommonUtils.printStackTrace(ex);
                    }
                }else if(holder instanceof NewsViewHolder){
                    try{
                        ((NewsViewHolder) holder).setData();
                    }catch (Exception ex){
                         CommonUtils.printStackTrace(ex);
                    }
                }else if(holder instanceof ArticalsViewHolder){
                    try{
                        ((ArticalsViewHolder) holder).setData();
                    }catch (Exception ex){
                         CommonUtils.printStackTrace(ex);
                    }
                }
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
             return NEAREST_WALKS_VIEW;
        }else if(position == 1){
             return SHOP_CATEGORY_VIEW;
        }else if(position == 2){
             return  NEWS_VIEW;
        }else if(position == 3){
            return  ARTICALS_VIEW;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class NearestWalkViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rvNearestWalk)
        RecyclerView rvNearestWalk;

        private NearestWalkAdapter adapter;

        public NearestWalkViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(){
            try{
                adapter = new NearestWalkAdapter(context,nearByStoreResponseList);
                rvNearestWalk.setHasFixedSize(true);
                rvNearestWalk.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
                rvNearestWalk.setAdapter(adapter);
            } catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
        }

        public void setNearByWolooData(List<NearByStoreResponse.Data> nearByStoreList){
            try{
                if(nearByStoreResponseList != null){
                    nearByStoreResponseList.clear();
                }
                nearByStoreResponseList.addAll(nearByStoreList);
                adapter.notifyDataSetChanged();
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
        }

    }

    public class ShopCategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rvShopCategory)
        RecyclerView rvShopCategory;

        public ShopCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(){
            try{
                ShopCategoryAdapter adapter = new ShopCategoryAdapter(context);
                rvShopCategory.setHasFixedSize(true);
                rvShopCategory.setLayoutManager(new GridLayoutManager(context, 2));
                rvShopCategory.setAdapter(adapter);
            } catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
        }
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rvNews)
        RecyclerView rvNews;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(){
            try{
                NewsAdapter adapter = new NewsAdapter(context);
                rvNews.setHasFixedSize(true);
                rvNews.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
                rvNews.setAdapter(adapter);
            } catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
        }
    }

    public class ArticalsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rvArticals)
        RecyclerView rvArticals;

        public ArticalsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(){
            try{
                ArticalAdapter adapter = new ArticalAdapter(context);
                rvArticals.setHasFixedSize(true);
                rvArticals.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
                rvArticals.setAdapter(adapter);
            } catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
        }
    }

    public NearestWalkViewHolder getNearestWalkViewHolder(){
        try{
            return nearestWalkViewHolder;
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
        return null;
    }

}
