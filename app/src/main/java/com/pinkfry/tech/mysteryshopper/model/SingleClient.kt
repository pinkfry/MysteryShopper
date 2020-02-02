package com.pinkfry.tech.mysteryshopper.model

import androidx.annotation.Keep

@Keep
data class SingleClient(var name:String){
    constructor():this(name="")
}