package com.pinkfry.tech.mysteryshopper.model

data class SIngleResponseModel(var date:String,var eachAns:ArrayList<AnsGivenModel>) {
    constructor():this(date="",eachAns = ArrayList())
}