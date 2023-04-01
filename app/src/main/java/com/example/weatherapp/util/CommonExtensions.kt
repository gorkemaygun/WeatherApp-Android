package com.example.weatherapp.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat

fun Activity.goToActivity(newActivity: Class<*>){
    val intent = Intent(this, newActivity)
    startActivity(intent)
}

fun Context.showToast(message:String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

fun Activity.color(resId:Int): Int{
    return ContextCompat.getColor(this,resId)
}

fun View.show(){
    visibility = View.VISIBLE
}
fun View.invisible(){
    visibility = View.INVISIBLE
}
fun View.hide(){
    visibility = View.GONE
}
