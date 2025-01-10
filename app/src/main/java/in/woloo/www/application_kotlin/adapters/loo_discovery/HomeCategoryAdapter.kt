package `in`.woloo.www.application_kotlin.adapters.loo_discovery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.common.CommonUtils

class HomeCategoryAdapter(
    private val context: Context,
    nearByStoreResponseList: MutableList<NearByStoreResponse.Data>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val NEAREST_WALKS_VIEW = 100
    private val SHOP_CATEGORY_VIEW = 200
    private val NEWS_VIEW = 300
    private val ARTICALS_VIEW = 400
    val nearestWalkViewHolder: NearestWalkViewHolder? = null
        get() {
            try {
                return field
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
            return null
        }
    private var nearByStoreResponseList: MutableList<NearByStoreResponse.Data>? = ArrayList()

    init {
        this.nearByStoreResponseList = nearByStoreResponseList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View = when (viewType) {
            NEAREST_WALKS_VIEW -> layoutInflater.inflate(R.layout.nearest_walk_item, parent, false)
            SHOP_CATEGORY_VIEW -> layoutInflater.inflate(R.layout.shop_category_item, parent, false)
            NEWS_VIEW -> layoutInflater.inflate(R.layout.news_item, parent, false)
            ARTICALS_VIEW -> layoutInflater.inflate(R.layout.articals_item, parent, false)
            else -> throw IllegalArgumentException("Invalid view type")
        }
        return when (viewType) {
            NEAREST_WALKS_VIEW -> NearestWalkViewHolder(listItem)
            SHOP_CATEGORY_VIEW -> ShopCategoryViewHolder(listItem)
            NEWS_VIEW -> NewsViewHolder(listItem)
            ARTICALS_VIEW -> ArticalsViewHolder(listItem)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            if (holder != null) {
                if (holder is NearestWalkViewHolder) {
                    try {
                        holder.setData()
                    } catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }
                } else if (holder is ShopCategoryViewHolder) {
                    try {
                        holder.setData()
                    } catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }
                } else if (holder is NewsViewHolder) {
                    try {
                        holder.setData()
                    } catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }
                } else if (holder is ArticalsViewHolder) {
                    try {
                        holder.setData()
                    } catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }
                }
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return NEAREST_WALKS_VIEW
        } else if (position == 1) {
            return SHOP_CATEGORY_VIEW
        } else if (position == 2) {
            return NEWS_VIEW
        } else if (position == 3) {
            return ARTICALS_VIEW
        }
        return 0
    }

    override fun getItemCount(): Int {
        return 4
    }

    inner class NearestWalkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.rvNearestWalk)
        var rvNearestWalk: RecyclerView? = null
        private var adapter: NearestWalkAdapter? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun setData() {
            try {
                adapter = NearestWalkAdapter(context, nearByStoreResponseList!!)
                rvNearestWalk!!.setHasFixedSize(true)
                rvNearestWalk!!.layoutManager = LinearLayoutManager(
                    context, RecyclerView.HORIZONTAL, false
                )
                rvNearestWalk!!.adapter = adapter
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }

        fun setNearByWolooData(nearByStoreList: List<NearByStoreResponse.Data>?) {
            try {
                nearByStoreResponseList?.clear()
                nearByStoreResponseList!!.addAll(nearByStoreList!!)
                adapter!!.notifyDataSetChanged()
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }
    }

    inner class ShopCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.rvShopCategory)
        var rvShopCategory: RecyclerView? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun setData() {
            /*  try{
                ShopCategoryAdapter adapter = new ShopCategoryAdapter(context);
                rvShopCategory.setHasFixedSize(true);
                rvShopCategory.setLayoutManager(new GridLayoutManager(context, 2));
                rvShopCategory.setAdapter(adapter);
            } catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }*/
        }
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.rvNews)
        var rvNews: RecyclerView? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun setData() {
            /* try{
                NewsAdapter adapter = new NewsAdapter(context);
                rvNews.setHasFixedSize(true);
                rvNews.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
                rvNews.setAdapter(adapter);
            } catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }*/
        }
    }

    inner class ArticalsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.rvArticals)
        var rvArticals: RecyclerView? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun setData() {
            /* try {
                 val adapter = ArticalAdapter(context)
                 rvArticals!!.setHasFixedSize(true)
                 rvArticals!!.layoutManager =
                     LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                 rvArticals!!.adapter = adapter
             } catch (ex: Exception) {
                 CommonUtils.printStackTrace(ex)
             }*/
        }
    }
}
