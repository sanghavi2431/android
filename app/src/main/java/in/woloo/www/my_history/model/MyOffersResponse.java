package in.woloo.www.my_history.model;

import java.util.List;

import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;

public class MyOffersResponse{
	private List<NearByStoreResponse.Data> data;
	private String message;
	private String status;

	public List<NearByStoreResponse.Data> getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public String getStatus(){
		return status;
	}
}