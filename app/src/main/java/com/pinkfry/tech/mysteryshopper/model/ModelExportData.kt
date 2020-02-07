package com.pinkfry.tech.mysteryshopper.model

 data class ModelExportData(var clientName: String,var storeName:String,var question:String,var date:String,var ans:String) {
     constructor():this("","","","","")
}