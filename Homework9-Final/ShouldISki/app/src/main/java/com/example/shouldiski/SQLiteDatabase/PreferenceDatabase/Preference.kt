package com.example.shouldiski.SQLiteDatabase.PreferenceDatabase

class Preference {
    var id : Int = 0
    var powderSnow : Int = 0
    var groomedSnow : Int = 0
    var coldWeather : Int = 0
    var warmWeather : Int = 0
    var snowWeather : Int = 0
    var clearWeather : Int = 0
    var carveSkill : Int = 0
    var parkSkill : Int = 0
    var levelP : Int = 0

    constructor(powderSnow: Int,groomedSnow: Int,
                coldWeather: Int,warmWeather: Int,
                snowWeather: Int,clearWeather: Int,
                carveSkill: Int,parkSkill: Int,levelP: Int){
        this.powderSnow = powderSnow
        this.groomedSnow = groomedSnow
        this.coldWeather = coldWeather
        this.warmWeather = warmWeather
        this.snowWeather = snowWeather
        this.clearWeather = clearWeather
        this.carveSkill = carveSkill
        this.parkSkill = parkSkill
        this.levelP = levelP
    }
    constructor()
}