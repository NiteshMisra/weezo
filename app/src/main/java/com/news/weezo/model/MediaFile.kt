package com.news.weezo.model

import android.os.Parcel
import android.os.Parcelable

class MediaFile() :Parcelable{
    var id=""
        var music_filename=""
    var file_qbuid=""
    var file_size=""
    var name=""
    var description=""
    var datetime=""
    var likes=""
    var views=""
    var is_liked=""
    var type=""

    constructor(parcel: Parcel) : this() {
        id = parcel.readString().toString()
        music_filename = parcel.readString().toString()
        file_qbuid = parcel.readString().toString()
        file_size = parcel.readString().toString()
        name = parcel.readString().toString()
        description = parcel.readString().toString()
        datetime = parcel.readString().toString()
        likes = parcel.readString().toString()
        views = parcel.readString().toString()
        is_liked = parcel.readString().toString()
        type = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(music_filename)
        parcel.writeString(file_qbuid)
        parcel.writeString(file_size)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(datetime)
        parcel.writeString(likes)
        parcel.writeString(views)
        parcel.writeString(is_liked)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MediaFile> {
        override fun createFromParcel(parcel: Parcel): MediaFile {
            return MediaFile(parcel)
        }

        override fun newArray(size: Int): Array<MediaFile?> {
            return arrayOfNulls(size)
        }
    }

}