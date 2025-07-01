package in.woloo.www.shopping.model;

public class AddressModel {

    private String  id,name,phone,pincode,city,state,area,flat_building,landmark;

    public AddressModel() {
    }

    public AddressModel(String id, String name, String phone, String pincode, String city, String state , String area, String flat_building , String landmark) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.pincode = pincode;
        this.city = city;
        this.state = state;
        this.area = area;
        this.flat_building = flat_building;
        this.landmark = landmark;


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




    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }




    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }





    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFlat_building() {
        return flat_building;
    }

    public void setFlat_building(String flat_building) {
        this.flat_building = flat_building;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }


}
