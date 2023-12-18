package com.example.shouldiski.SQLiteDatabase.ResortDatabase

class Resort {
    var id : Int = 0
    var destination : String = ""
    var resortName : String = ""
    var hotelID : String = ""
    var resortLocation : String = ""

    constructor(destination: String,resortName: String,hotelID: String,resortLocation: String){
        this.destination = destination
        this.resortName = resortName
        this.hotelID = hotelID
        this.resortLocation = resortLocation
    }
}