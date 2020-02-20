package com.pinkfry.tech.mysteryshopper.model

import androidx.annotation.Keep

@Keep
data class SingleClient(var name:String,var total:Int,var imagePosition:Int,var imageUrl:String){
    constructor():this(name="",total = 0,imagePosition = 0,imageUrl = "")
}