package in.woloo.www.shopping.model;

public class HomeProductModel {
    private String image;
     private String id;
     private String name;
    private String from;
    private String count;
    private String desc;
    private String  price;


    public HomeProductModel(String image, String name, String id, String from, String count, String desc , String price ) {
        super();
        this.image = image;
         this.id=id;
         this.name=name;
        this.from=from;
        this.count=count;
        this.desc=desc;
        this.price = price;


    }


    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }





    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


/*
    public String getSub_cat_id() {
        return sub_cat_id;
    }

    public void setSub_cat_id(String sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
    }
*/


      public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }






    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }



}


