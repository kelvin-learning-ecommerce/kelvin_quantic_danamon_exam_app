package com.kelvinquantic.danamon.room.daomodel

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserDaoModel(
    @PrimaryKey(autoGenerate = true) var uid: Int? = null,
    @ColumnInfo("username") var username: String? = null,
    @ColumnInfo("email") var email: String? = null,
    @ColumnInfo("role") var role: String? = null,
    @ColumnInfo("password") var password: String? = null,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(uid)
        parcel.writeString(username)
        parcel.writeString(email)
        parcel.writeString(role)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserDaoModel> {
        override fun createFromParcel(parcel: Parcel): UserDaoModel {
            return UserDaoModel(parcel)
        }

        override fun newArray(size: Int): Array<UserDaoModel?> {
            return arrayOfNulls(size)
        }
    }
}
