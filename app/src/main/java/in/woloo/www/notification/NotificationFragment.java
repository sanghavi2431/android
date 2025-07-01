package in.woloo.www.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.subscribe.models.PlanResponse;
import in.woloo.www.subscribe.mvp.SubscribePresenter;
import in.woloo.www.utils.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link in.woloo.www.subscribe.fragments.SubscribeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment  {

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.recyclerView_subscribe)
    RecyclerView recyclerView_subscribe;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String currentSubscriptionPlan;
    private String msg;
    ArrayList<PlanResponse.Data> subscriptionArrayList;
    private SubscribePresenter subscribePresenter;
    private boolean isEmail = false;
    public static String TAG= NotificationFragment.class.getSimpleName();
    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param isEmail
     * @return A new instance of fragment SubscribeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2, boolean isEmail) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putBoolean(ARG_PARAM3, isEmail);
        fragment.setArguments(args);
        return fragment;
    }
    /*calling on onCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentSubscriptionPlan = getArguments().getString(ARG_PARAM1);
            msg = getArguments().getString(ARG_PARAM2);
            isEmail = getArguments().getBoolean(ARG_PARAM3);
        }
        Logger.i(TAG, "onCreate");
    }
    /*calling on onCreateView*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.notification_fragment, container, false);
        ButterKnife.bind(this,root);
        initViews();
        Logger.i(TAG, "onCreateView");
        return root;
    }
    /*calling on initViews*/
    private void initViews() {
        Logger.i(TAG, "initViews");
        subscriptionArrayList=new ArrayList<PlanResponse.Data>();
        try{
//            subscribePresenter = new SubscribePresenter(getContext(), NotificationFragment.this,subscriptionArrayList,recyclerView_subscribe, NotificationFragment.this,currentSubscriptionPlan,msg,isEmail);
//            subscribePresenter.getSubscriptionPlans();

//            setSearchResults();

            tvTitle.setText(getResources().getString(R.string.notification));
            ivBack.setOnClickListener(v -> {
                getActivity().onBackPressed();
            });

        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

}