package com.pinkfry.tech.mysteryshopper.model


data class SingleStore(var name:String,var AnsGiven:ArrayList<UpperAnsGivenModel>,var address:String,var phoneNo:String,var totalClient:Int){
    constructor():this(name="",address = "",AnsGiven = arrayListOf(),phoneNo = "",totalClient = 0)
}