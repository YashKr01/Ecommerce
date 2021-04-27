package com.shopping.bloom.model

import android.os.Parcel
import android.os.Parcelable

data class CategoryTypes(
        val categoryName: String,
        val categoryId: String,
        val parentId: String,
        var isSelected: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readString().toString(),
            parcel.readByte() != 0.toByte()) {
    }

    constructor(categoryName: String, categoryId: String, parentId: String) :
            this(categoryName, categoryId, parentId,false)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(categoryName)
        parcel.writeString(categoryId)
        parcel.writeString(parentId)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryTypes> {
        override fun createFromParcel(parcel: Parcel): CategoryTypes {
            return CategoryTypes(parcel)
        }

        override fun newArray(size: Int): Array<CategoryTypes?> {
            return arrayOfNulls(size)
        }
    }
}
