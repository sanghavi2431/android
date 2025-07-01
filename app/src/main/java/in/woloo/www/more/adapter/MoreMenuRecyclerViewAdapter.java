package in.woloo.www.more.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.woloo.www.R;
import in.woloo.www.more.callbacks.MenuClickCallback;


public class MoreMenuRecyclerViewAdapter extends RecyclerView.Adapter<MoreMenuRecyclerViewAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private Context context;
    private List<String> menuList;
    private MenuClickCallback menuClickCallback;

    public MoreMenuRecyclerViewAdapter(Context context, List<String> menuList, MenuClickCallback menuClickCallback) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.menuList = menuList;
        this.menuClickCallback = menuClickCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.more_menu_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String animal = menuList.get(position);
        viewHolder.tvMenu.setText(animal);
        viewHolder.llContentLayout.setOnClickListener(v -> {
            menuClickCallback.menuItemClick(position);
        });
        setMenuIcon(viewHolder,position);
    }

    private void setMenuIcon(ViewHolder viewHolder, int position) {
        String menu = menuList.get(position);
        switch (menu){
            case "Notification":
                viewHolder.ivMenu.setImageResource(R.drawable.ic_notification);
                break;
            case "My Cart":
                viewHolder.ivMenu.setImageResource(R.drawable.ic_my_cart);
                break;
            case "Buy Pee’rs Club Membership":
                viewHolder.ivMenu.setImageResource(R.drawable.ic_buy_subscription);
                break;
            case "My History":
                viewHolder.ivMenu.setImageResource(R.drawable.ic_history);
                break;
            case "Invite A Friend":
                viewHolder.ivMenu.setImageResource(R.drawable.ic_invite);
                 break;
            case "My Account":
                viewHolder.ivMenu.setImageResource(R.drawable.ic_account);
                break;
            case "Woloo Gift-Card":
                viewHolder.ivMenu.setImageResource(R.drawable.ic_gift_card);
                break;
            case "Become A Woloo Host":
                viewHolder.ivMenu.setImageResource(R.drawable.ic_w_host);
                break;
            case "Period Reminder":
                viewHolder.ivMenu.setImageResource(R.drawable.ic_gift_card);
                break;
            case "Add Review":
                viewHolder.ivMenu.setImageResource(R.drawable.ic_gift_card);
                break;
            case "Logout":
                viewHolder.ivMenu.setImageResource(R.drawable.ic_logout);
                break;
            case "About":
                viewHolder.ivMenu.setImageResource(R.drawable.ic_about);
                break;
            case "Terms of Use":
                viewHolder.ivMenu.setImageResource(R.drawable.ic_terms_of_use);
                break;
            case "Refer a Woloo Host":
                viewHolder.ivMenu.setImageResource(R.drawable.ic_refer_woloo);
                break;
            case "Become a Woloo Host":
                viewHolder.ivMenu.setImageResource(R.drawable.ic_about);
                break;
            case "Discontinue Pee’rs Club Membership":
                viewHolder.ivMenu.setImageResource(R.drawable.unsubsribe_icon);
                break;
            case "Delete Account":
                viewHolder.ivMenu.setImageResource(R.drawable.unsubsribe_icon);
                break;
            case "Offer Cart":
                viewHolder.ivMenu.setImageResource(R.drawable.ic_account);
                break;

        }
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMenu;
        ImageView ivMenu;
        LinearLayout llContentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenu = itemView.findViewById(R.id.tvMenu);
            ivMenu = itemView.findViewById(R.id.ivMenu);
            llContentLayout = itemView.findViewById(R.id.llContentLayout);
        }
    }
}
