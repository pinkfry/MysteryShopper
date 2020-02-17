package com.pinkfry.tech.mysteryshopper.model


data class SingleStore(var name:String,var AnsGiven:HashMap<String,ArrayList<SIngleResponseModel>>,var address:String,var phoneNo:String,var totalClient:Int){
    constructor():this(name="",address = "",AnsGiven = hashMapOf(),phoneNo = "",totalClient = 0)
}