package ru.leti.kneeapp.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Training (
    val nameStringResourceId: Int,
    val explanationStringResourceId: Int,
    @DrawableRes
    val trainingPreview: Int,
    val passMark: String,
    @DrawableRes
    val exerciseImage1: Int,
    @DrawableRes
    val exerciseImage2: Int?
): Parcelable