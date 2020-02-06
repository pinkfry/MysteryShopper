package com.pinkfry.tech.mysteryshopper.model

data class AnsGivenModel(var ans:ArrayList<String>,var value:Int){
    constructor():this(ans=ArrayList(),value = 0)
}