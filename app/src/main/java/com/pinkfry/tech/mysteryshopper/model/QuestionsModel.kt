package com.pinkfry.tech.mysteryshopper.model

data class QuestionsModel(var Question:String,var Options:ArrayList<OptionModels>,var type:Int,var visible:Int) {
    constructor():this(Question="hello",Options = ArrayList(),type = 1,visible = 1)
}