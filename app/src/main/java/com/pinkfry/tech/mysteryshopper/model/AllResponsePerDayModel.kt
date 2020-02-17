package com.pinkfry.tech.mysteryshopper.model

data class AllResponsePerDayModel(var date:String,var singleResponseModelArrayList: ArrayList<SIngleResponseModel>) {
    constructor():this(date="",singleResponseModelArrayList = ArrayList())
}