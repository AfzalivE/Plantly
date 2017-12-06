package com.spacebitlabs.plantly.data.source

import com.spacebitlabs.plantly.data.models.Plant

/**
 * Store for the user's plants
 */
class UserPlantsStore {
    val plants: ArrayList<Plant> = ArrayList()

    init {
        plants.add(Plant(0,"Sansa"))
        plants.add(Plant(1,"Drogo"))
        plants.add(Plant(2,"Cro"))
    }

    fun savePlant(plant: Plant) {
        plants.add(plant)
    }
}