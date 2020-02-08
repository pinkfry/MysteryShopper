package com.pinkfry.tech.mysteryshopper.model

data class UpperAnsGivenModel(var shortedByDate:HashMap<String,AnsGivenModel>,var type:Int,var question:String) {
    constructor():this(shortedByDate= hashMapOf(),type =1,question = "" )
}