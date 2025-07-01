package in.woloo.www.utils;

import com.google.gson.annotations.SerializedName;

public class DynamicImageRequestModel {
/*    @SerializedName("bucket")
    private final String bucket;*/

    @SerializedName("key")
    private final String key;

    @SerializedName("edits")
    private final Edits edits;

    //public DynamicImageRequestModel(String bucket, String key, Edits edits) {
    public DynamicImageRequestModel(String key, Edits edits) {
       // this.bucket = bucket;
        this.key = key;
        this.edits = edits;
    }

  /*  public String getBucket() {
        return bucket;
    }*/

    public String getKey() {
        return key;
    }

    public Edits getEdits() {
        return edits;
    }

    public static class Edits {
        @SerializedName("resize")
        private final Resize resize;

        @SerializedName("blur")
        private final double blur; //value between 0.3 and 1000 representing the sigma of the Gaussian mask

        public Edits(Resize resize,double blur) {
            this.resize = resize;
            this.blur = blur;
        }

        public Resize getResize() {
            return resize;
        }

        public double getBlur() {
            return blur;
        }

        public static class Resize {
            @SerializedName("width")
            private final int width;

           /* @SerializedName("height")
            private final int height;*/

            @SerializedName("fit")
            private final String fit;

           // public Resize(int width, int height, String fit) {
            public Resize(int width, String fit) {
                this.width = width;
               // this.height = height;
                this.fit = fit;
            }

            public int getWidth() {
                return width;
            }

           /* public int getHeight() {
                return height;
            }*/

            public String getFit() {
                return fit;
            }
        }
    }
}
