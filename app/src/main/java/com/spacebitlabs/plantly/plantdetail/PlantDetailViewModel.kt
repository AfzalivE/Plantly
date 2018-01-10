package com.spacebitlabs.plantly.plantdetail

import android.arch.lifecycle.ViewModel
import com.spacebitlabs.plantly.data.source.UserPlantsStore

/**
 * ViewModel for Add Plant screen
 */
class PlantDetailViewModel : ViewModel() {

    private val userPlantsStore: UserPlantsStore = UserPlantsStore()

}