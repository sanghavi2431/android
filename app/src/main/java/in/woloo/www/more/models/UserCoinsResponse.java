package in.woloo.www.more.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserCoinsResponse {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("total_coins")
        @Expose
        private Integer totalCoins;
        @SerializedName("gift_coins")
        @Expose
        private Integer giftCoins;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Integer getTotalCoins() {
            return totalCoins;
        }

        public void setTotalCoins(Integer totalCoins) {
            this.totalCoins = totalCoins;
        }

        public Integer getGiftCoins() {
            return giftCoins;
        }

        public void setGiftCoins(Integer giftCoins) {
            this.giftCoins = giftCoins;
        }

    }

}
