package com.spacebitlabs.plantly.data.source

import com.spacebitlabs.plantly.data.models.Plant

/**
 * Store for the user's plants
 */
class UserPlantsStore {
    val plants: ArrayList<Plant> = ArrayList()

    init {
        plants.add(Plant(0, "Sansa"))
        plants.add(Plant(1, "Drogo"))
        plants.add(Plant(2, "Cro"))
        plants.add(Plant(3, "Krypton"))
        plants.add(Plant(4, "Xenon"))
        plants.add(Plant(5, "Argon"))
        plants.add(Plant(6, "Sansa"))
        plants.add(Plant(7, "Drogo"))
        plants.add(Plant(8, "Cro"))
        plants.add(Plant(9, "Krypton"))
        plants.add(Plant(10, "Xenon"))
        plants.add(Plant(11, "Argon"))
    }

    fun savePlant(plant: Plant) {
        plants.add(plant)
    }
}