package com.spacebitlabs.plantly.plants

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.data.models.Plant
import com.spacebitlabs.plantly.inflate
import com.spacebitlabs.plantly.plants.PlantsAdapter.PlantHolder
import kotlinx.android.synthetic.main.plants_list_item.view.*

/**
 * Adapter for plant list
 */
class PlantsAdapter : RecyclerView.Adapter<PlantHolder>() {

    private val plantList: ArrayList<Plant> = ArrayList()

    override fun getItemCount(): Int = plantList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantHolder {
        return PlantHolder(parent.inflate(R.layout.plants_list_item))
    }

    override fun onBindViewHolder(holder: PlantHolder, position: Int) {
        holder.bind(plantList[position])
    }

    fun setPlantList(plants: List<Plant>) {
        val diffResult = DiffUtil.calculateDiff(PlantDiffCallback(plantList, plants))
        plantList.clear()
        plantList.addAll(plants)
        diffResult.dispatchUpdatesTo(this)
    }

    class PlantHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(plant: Plant) = with(itemView) {
            name.text = plant.name
        }
    }
}