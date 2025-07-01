package in.woloo.www.shopping.model;

public class CartModel {

    private String  pro_id,pro_name,image,price,qty,customer_margin,point_used,total_amount, coupon_value, coupon_value_unit;

    public CartModel() {
    }

    public CartModel(String pro_id, String pro_name, String image, String price, String qty, String customer_margin , String point_used, String total_amount, String coupon_value, String coupon_value_unit) {
        this.pro_id = pro_id;
        this.pro_name = pro_name;
        this.image = image;
        this.price = price;
        this.qty = qty;
        this.customer_margin = customer_margin;
        this.point_used = point_used;
        this.total_amount = total_amount;
        this.coupon_value = coupon_value;
        this.coupon_value_unit = coupon_value_unit;


    }





    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }




    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }




    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }




    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }





    public String getCustomer_margin() {
        return customer_margin;
    }

    public void setCustomer_margin(String customer_margin) {
        this.customer_margin = customer_margin;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }


    public String getPoint_used() {
        return point_used;
    }

    public void setPoint_used(String point_used) {
        this.point_used = point_used;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }


    public String getCoupon_value() {
        return coupon_value;
    }

    public void setCoupon_value(String coupon_value) {
        this.coupon_value = coupon_value;
    }




    public String getCoupon_value_unit() {
        return coupon_value_unit;
    }

    public void setCoupon_value_unit(String coupon_value_unit) {
        this.coupon_value_unit = coupon_value_unit;
    }



}
