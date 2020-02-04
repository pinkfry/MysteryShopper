package com.pinkfry.tech.mysteryshopper.model

data class QuestionsModel(var Question:String,var Options:ArrayList<OptionModels>) {
    constructor():this(Question="hello",Options = ArrayList())
}