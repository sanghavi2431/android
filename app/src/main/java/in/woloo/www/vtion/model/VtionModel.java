package in.woloo.www.vtion.model;


public class VtionModel {

    private String ownershipIcon;
    private String ownershipName;



    private transient boolean isSelected = false;


    public String getOwnershipIcon() {
        return ownershipIcon;
    }

    public void setOwnershipIcon(String ownershipIcon) {
        this.ownershipIcon = ownershipIcon;
    }

    public String getOwnershipName() {
        return ownershipName;
    }

    public void setOwnershipName(String ownershipName) {
        this.ownershipName = ownershipName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }




}
