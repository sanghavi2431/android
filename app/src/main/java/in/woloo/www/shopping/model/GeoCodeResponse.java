package in.woloo.www.shopping.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GeoCodeResponse{

    @SerializedName("data")
    private List<DataItem> data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    public List<DataItem> getData(){
        return data;
    }

    public String getMessage(){
        return message;
    }

    public String getStatus(){
        return status;
    }

    public class DataItem{

        @SerializedName("formatted_address")
        private String formattedAddress;

        @SerializedName("types")
        private List<String> types;

        @SerializedName("geometry")
        private Geometry geometry;

        @SerializedName("address_components")
        private List<AddressComponentsItem> addressComponents;

        @SerializedName("plus_code")
        private PlusCode plusCode;

        @SerializedName("place_id")
        private String placeId;

        public String getFormattedAddress(){
            return formattedAddress;
        }

        public List<String> getTypes(){
            return types;
        }

        public Geometry getGeometry(){
            return geometry;
        }

        public List<AddressComponentsItem> getAddressComponents(){
            return addressComponents;
        }

        public PlusCode getPlusCode(){
            return plusCode;
        }

        public String getPlaceId(){
            return placeId;
        }
    }

    public class AddressComponentsItem{

        @SerializedName("types")
        private List<String> types;

        @SerializedName("short_name")
        private String shortName;

        @SerializedName("long_name")
        private String longName;

        public List<String> getTypes(){
            return types;
        }

        public String getShortName(){
            return shortName;
        }

        public String getLongName(){
            return longName;
        }
    }

    public class Bounds{

        @SerializedName("southwest")
        private Southwest southwest;

        @SerializedName("northeast")
        private Northeast northeast;

        public Southwest getSouthwest(){
            return southwest;
        }

        public Northeast getNortheast(){
            return northeast;
        }
    }

    public class Geometry{

        @SerializedName("viewport")
        private Viewport viewport;

        @SerializedName("bounds")
        private Bounds bounds;

        @SerializedName("location")
        private Location location;

        @SerializedName("location_type")
        private String locationType;

        public Viewport getViewport(){
            return viewport;
        }

        public Bounds getBounds(){
            return bounds;
        }

        public Location getLocation(){
            return location;
        }

        public String getLocationType(){
            return locationType;
        }
    }

    public class Location{

        @SerializedName("lng")
        private double lng;

        @SerializedName("lat")
        private double lat;

        public double getLng(){
            return lng;
        }

        public double getLat(){
            return lat;
        }
    }

    public class Northeast{

        @SerializedName("lng")
        private double lng;

        @SerializedName("lat")
        private double lat;

        public double getLng(){
            return lng;
        }

        public double getLat(){
            return lat;
        }
    }

    public class PlusCode{

        @SerializedName("compound_code")
        private String compoundCode;

        @SerializedName("global_code")
        private String globalCode;

        public String getCompoundCode(){
            return compoundCode;
        }

        public String getGlobalCode(){
            return globalCode;
        }
    }

    public class Southwest{

        @SerializedName("lng")
        private double lng;

        @SerializedName("lat")
        private double lat;

        public double getLng(){
            return lng;
        }

        public double getLat(){
            return lat;
        }
    }

    public class Viewport{

        @SerializedName("southwest")
        private Southwest southwest;

        @SerializedName("northeast")
        private Northeast northeast;

        public Southwest getSouthwest(){
            return southwest;
        }

        public Northeast getNortheast(){
            return northeast;
        }
    }
}