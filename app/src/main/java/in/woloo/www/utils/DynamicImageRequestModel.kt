package `in`.woloo.www.utils

import com.google.gson.annotations.SerializedName

class DynamicImageRequestModel// this.bucket = bucket;
//public DynamicImageRequestModel(String bucket, String key, Edits edits) {
    (/*  public String getBucket() {
       return bucket;
   }*/
     /*    @SerializedName("bucket")
         private final String bucket;*/@field:SerializedName("key") val key: String,
     @field:SerializedName(
         "edits"
     ) val edits: Edits
) {
    class Edits(
        @field:SerializedName("resize") val resize: Resize, //value between 0.3 and 1000 representing the sigma of the Gaussian mask
        @field:SerializedName("blur") val blur: Double
    ) {
        class Resize// this.height = height;
        // public Resize(int width, int height, String fit) {
            (
            @field:SerializedName("width") val width: Int, /* public int getHeight() {
               return height;
           }*/
            /* @SerializedName("height")
                        private final int height;*/
            @field:SerializedName("fit") val fit: String
        )
    }
}
