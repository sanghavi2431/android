package in.woloo.www.shopping.model;

public class ProductModel {
    private String id,title,price,desc,qty,commission,image;

    public ProductModel() {
    }

    public ProductModel(String id,  String title, String price, String desc, String qty,String image) {
        this.id = id;

        this.title = title;
        this.image = image;

        this.price = price;
        this.desc = desc;
        this.qty = qty;




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











}

