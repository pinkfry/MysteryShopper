package com.pinkfry.tech.mysteryshopper.model

data class ResponseGiven(var AnsGiven:ArrayList<Int>,var name:String,var address:String,var phoneNo: String) {
    constructor():this(AnsGiven= ArrayList(),name = "",address = "",phoneNo = "")
}