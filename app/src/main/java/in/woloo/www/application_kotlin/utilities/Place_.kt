package `in`.woloo.www.application_kotlin.utilities

import android.os.Parcel
import android.os.Parcelable

open class Place_ : Parcelable {
    var id: String? = null
    var address: String? = null
    var name: String? = null
    var latitude = 0.0
    var longitude = 0.0
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(address)
        dest.writeString(name)
        dest.writeDouble(latitude)
        dest.writeDouble(longitude)
    }

    constructor()
    protected constructor(`in`: Parcel) {
        id = `in`.readString()
        address = `in`.readString()
        name = `in`.readString()
        latitude = `in`.readDouble()
        longitude = `in`.readDouble()
    }

    companion object CREATOR : Parcelable.Creator<Place_> {
        override fun createFromParcel(parcel: Parcel): Place_ {
            return Place_(parcel)
        }

        override fun newArray(size: Int): Array<Place_?> {
            return arrayOfNulls(size)
        }
    }
}
