package com.spacebitlabs.plantly.plants

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.data.entities.Plant
import com.spacebitlabs.plantly.inflate
import com.spacebitlabs.plantly.plantdetail.PlantDetailFragment
import com.spacebitlabs.plantly.plants.PlantsAdapter.PlantHolder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.plants_list_item.view.*

/**
 * Adapter for plant list
 */
class PlantsAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<PlantHolder>() {

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

    class PlantHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView), View.OnClickListener {
        lateinit var plant: Plant
        private val name = itemView.name
        private val image = itemView.image

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            view?.let { PlantDetailFragment.show(it, plant) }
        }

        fun bind(plant: Plant) {
            this.plant = plant
            name.text = plant.name
            if (plant.coverPhoto.filePath.isEmpty()) {
                image.setImageResource(R.drawable.sample_plant)
            } else {
                Picasso.get()
                    .load("file://${plant.coverPhoto.filePath}")
                    .fit()
                    .centerCrop()
                    .into(image)
            }
        }
    }
}