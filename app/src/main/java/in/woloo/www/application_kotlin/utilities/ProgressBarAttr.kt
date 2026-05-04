package `in`.woloo.www.application_kotlin.utilities

import android.os.Parcel
import android.os.Parcelable

class ProgressBarAttr : Parcelable {

    private lateinit var message: String
    var isShow: Boolean = false

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ProgressBarAttr> = object : Parcelable.Creator<ProgressBarAttr> {
            override fun createFromParcel(source: Parcel): ProgressBarAttr = ProgressBarAttr(source)
            override fun newArray(size: Int): Array<ProgressBarAttr?> = arrayOfNulls(size)
        }
    }

    constructor()

    constructor(source: Parcel): this(){
        this.message = source.readString().toString()
        this.isShow = 1 == source.readInt()
    }


    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(message)
        writeInt((if (isShow) 1 else 0))
    }
}