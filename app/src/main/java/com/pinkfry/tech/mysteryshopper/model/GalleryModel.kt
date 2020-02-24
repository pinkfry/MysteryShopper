package com.pinkfry.tech.mysteryshopper.model

data class GalleryModel(var url:String,var tagLine:String){
    constructor():this(url="",tagLine = "")
}