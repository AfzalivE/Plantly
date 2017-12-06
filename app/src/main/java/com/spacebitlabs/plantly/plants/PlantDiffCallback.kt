package com.spacebitlabs.plantly.plants

import android.support.v7.util.DiffUtil
import com.spacebitlabs.plantly.data.models.Plant

/**
 * DiffUtil to dispatch updates to the adapter data
 */
class PlantDiffCallback(
        private val oldList: List<Plant>,
        private val newList: List<Plant>) : DiffUtil.Callback() {


    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPlant = oldList[oldItemPosition]
        val newPlant = newList[newItemPosition]

        return oldPlant == newPlant
    }
}