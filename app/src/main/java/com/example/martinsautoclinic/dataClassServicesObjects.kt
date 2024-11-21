package com.example.martinsautoclinic

data class dataClassServicesObjects(
    val primaryKey:String,
    val serviceName:String,
    val servicePrice:String,
    val serviceType:String,
    var orderNumber:Int?
)