package in.woloo.www.shopping.model;

public class OrderModel {
    private String id,pro_id,title,price,desc,qty,image, date_time , order_id, status, can_return , can_cancel, amount ;

    public OrderModel() {
    }

    public OrderModel(String id, String pro_id, String title, String price, String desc, String qty, String image, String date_time, String order_id, String status, String can_return, String can_cancel, String amount) {
        this.id = id;
        this.pro_id = pro_id;

        this.title = title;
        this.image = image;

        this.price = price;
        this.desc = desc;
        this.qty = qty;


        this.date_time = date_time;
        this.order_id = order_id;
        this.status = status;
        this.can_return = can_return;
        this.can_cancel = can_cancel;
        this.amount = amount;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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







    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }


    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCan_return() {
        return can_return;
    }

    public void setCan_return(String can_return) {
        this.can_return = can_return;
    }

    public String getCan_cancel() {
        return can_cancel;
    }

    public void setCan_cancel(String can_cancel) {
        this.can_cancel = can_cancel;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }
}

