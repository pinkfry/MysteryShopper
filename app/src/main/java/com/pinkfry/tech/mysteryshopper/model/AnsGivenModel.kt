package com.pinkfry.tech.mysteryshopper.model

data class AnsGivenModel(var ans:String,var question:String,var type:Int,var value:Int,var visible:Int){
    constructor():this(ans="",value = 0,question = "",type=1,visible = 0)
}