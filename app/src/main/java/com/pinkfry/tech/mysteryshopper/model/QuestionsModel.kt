package com.pinkfry.tech.mysteryshopper.model

data class QuestionsModel(var Question:String,var Options:ArrayList<String>,var Answers:ArrayList<Int>) {
    constructor():this(Question="hello",Options = ArrayList(),Answers=ArrayList())
}