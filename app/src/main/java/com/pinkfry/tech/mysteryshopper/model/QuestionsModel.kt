package com.pinkfry.tech.mysteryshopper.model

data class QuestionsModel(var Question:String,var Options:ArrayList<String>) {
    constructor():this(Question="hello",Options = ArrayList())
}