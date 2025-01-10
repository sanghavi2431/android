package `in`.woloo.www.shopping.model

class HomeProductModel(
    @JvmField var image: String, /*
    public String getSub_cat_id() {
        return sub_cat_id;
    }

    public void setSub_cat_id(String sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
    }
*/
    @JvmField var name: String,
    @JvmField var id: String,
    var from: String,
    @JvmField var count: String,
    @JvmField var desc: String,
    @JvmField var price: String
)


