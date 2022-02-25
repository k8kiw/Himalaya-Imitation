package com.kotori.common.sdk

import android.os.Parcel
import android.os.Parcelable
import com.ximalaya.ting.android.opensdk.model.word.QueryResult

/**
 * 实现 Parcelable 序列化的联想词
 */
class ParcelableQueryResult() : QueryResult(), Parcelable {

    constructor(parcel: Parcel) : this() {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {

    }

    companion object CREATOR : Parcelable.Creator<ParcelableQueryResult> {
        override fun createFromParcel(parcel: Parcel): ParcelableQueryResult {
            return ParcelableQueryResult(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableQueryResult?> {
            return arrayOfNulls(size)
        }
    }

}