package com.spacebitlabs.plantly.data.source

import com.spacebitlabs.plantly.data.models.Plant

/**
 * Created by afzal on 2017-12-04.
 */
class UserPlantsStore {
    val plants: ArrayList<Plant> = ArrayList()

    init {
        plants.add(Plant("Sansa"))
        plants.add(Plant("Drogo"))
        plants.add(Plant("Cro"))
    }
}