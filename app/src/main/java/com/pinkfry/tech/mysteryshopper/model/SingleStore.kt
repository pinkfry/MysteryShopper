package com.pinkfry.tech.mysteryshopper.model


data class SingleStore(var name:String,var AnsGiven:ArrayList<Int>){
    constructor():this(name="",AnsGiven = ArrayList())
}