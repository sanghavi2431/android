package in.woloo.www.shopping.model;

public class CategoryModel {
    private String image;
     private String id;
     private String name;
    private String from;
    private String banner1;
    private String banner2;
    private String banner3;




    public CategoryModel(String image, String name, String id,String from, String banner1, String banner2, String banner3) {
        super();
        this.image = image;
         this.id=id;
         this.name=name;
        this.from=from;
        this.banner1 = banner1;
        this.banner2 = banner2;
        this.banner3 = banner3;



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

    public String getBanner1() {
        return banner1;
    }
    public String getBanner2() { return banner2; }
    public String getBanner3() { return banner3; }

    public void setBanner1(String banner1) {
        this.banner1 = banner1;
    }
    public void setBanner2(String banner2) {
        this.banner2 = banner2;
    }
    public void setBanner3(String banner3) {
        this.banner3 = banner3;
    }


}