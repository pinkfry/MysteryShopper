package com.pinkfry.tech.mysteryshopper.model

data class ResponseGiven(var name:String,var address:String,var phoneNo: String) {
    constructor():this(name = "",address = "",phoneNo = "")
}