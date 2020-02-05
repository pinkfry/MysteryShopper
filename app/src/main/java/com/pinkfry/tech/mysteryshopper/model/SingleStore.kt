package com.pinkfry.tech.mysteryshopper.model


data class SingleStore(var name:String,var AnsGiven:ArrayList<Int>,var address:String,var phoneNo:String,var totalClient:Int){
    constructor():this(name="",AnsGiven = ArrayList(),address = "",phoneNo = "",totalClient = 0)
}