package com.spacebitlabs.plantly.plants

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.data.entities.Plant
import com.spacebitlabs.plantly.inflate
import com.spacebitlabs.plantly.plantdetail.PlantDetailFragment
import com.spacebitlabs.plantly.plants.PlantsAdapter.PlantHolder
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.plants_list_item.*

/**
 * Adapter for plant list
 */
class PlantsAdapter : RecyclerView.Adapter<PlantHolder>() {

    private val plantList: ArrayList<Plant> = ArrayList()
    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = plantList[position].id

    override fun getItemCount(): Int = plantList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantHolder {
        return PlantHolder(parent.inflate(R.layout.plants_list_item))
    }

    override fun onBindViewHolder(holder: PlantHolder, position: Int) {
        val plant = plantList[position]
        holder.bind(plant, tracker?.isSelected(plant.id) ?: false)
    }

    fun setPlantList(plants: List<Plant>) {
        val diffResult = DiffUtil.calculateDiff(PlantDiffCallback(plantList, plants))
        plantList.clear()
        plantList.addAll(plants)
        diffResult.dispatchUpdatesTo(this)
    }

    class PlantHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), View.OnClickListener, LayoutContainer {
        lateinit var plant: Plant

        init {
            containerView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            view?.let { PlantDetailFragment.show(it, plant) }
        }

        fun bind(plant: Plant, isSelected: Boolean) {
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
            containerView.isActivated = isSelected
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long>? {
            return PlantDetails(adapterPosition, itemId)
        }

        class PlantDetails(
            private val adapterPosition: Int,
            private val itemId: Long
        ) : ItemDetailsLookup.ItemDetails<Long>() {
            override fun getPosition(): Int = adapterPosition
            override fun getSelectionKey(): Long? = itemId
        }
    }

}
