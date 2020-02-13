package com.pinkfry.tech.mysteryshopper.model

data class UpperAnsGivenModel(var shortedByDate:HashMap<String,AnsGivenModel>,var type:Int,var question:String,var visible:Int) {
    constructor():this(shortedByDate= hashMapOf(),type =1,question = "",visible = 1 )
}