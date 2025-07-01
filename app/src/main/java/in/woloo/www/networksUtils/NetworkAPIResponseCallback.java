package in.woloo.www.networksUtils;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface NetworkAPIResponseCallback extends NetworkTimeOut,InternetConnection{
    void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel);
    void onFailure(VolleyError volleyError, NetworkAPICallModel networkAPICallModel);
}
