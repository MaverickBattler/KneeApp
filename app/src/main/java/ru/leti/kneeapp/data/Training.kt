package ru.leti.kneeapp.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

//Класс, который хранит данные об упражнении, которое показывается в
//ExerciseActivity и в RecyclerView в TrainingFragment
//Аннотация @Parcelize позволяет передавать класс целиком через intent
@Parcelize
data class Training (
    //Id строкового ресурса с названием упражнения
    val nameStringResourceId: Int,
    //Id строкового ресурса с описанием упражнения
    val explanationStringResourceId: Int,
    @DrawableRes
    //Id ресурса с маленькой картинкой (превью) упражнения
    val trainingPreview: Int,
    //Отметка о выполнении упражнения
    val passMark: String,
    @DrawableRes
    //Id ресурса с картинкой с выполнением упражнения
    val exerciseImage1: Int,
    @DrawableRes
    //Id второго ресурса с картинкой с выполнением упражнения, может быть null
    val exerciseImage2: Int?
): Parcelable