package com.spacebitlabs.plantly.plants

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.data.entities.Plant
import com.spacebitlabs.plantly.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.today_list_item.*

class TodayAdapter : RecyclerView.Adapter<TodayAdapter.TodayHolder>() {
    private val plantList: ArrayList<Plant> = ArrayList()

    override fun getItemCount(): Int = plantList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayHolder {
        return TodayHolder(parent.inflate(R.layout.today_list_item))
    }

    override fun onBindViewHolder(holder: TodayHolder, position: Int) {
        holder.bind(plantList[position])
    }

    fun setPlantList(plants: List<Plant>) {
        val diffResult = DiffUtil.calculateDiff(PlantDiffCallback(plantList, plants))
        plantList.clear()
        plantList.addAll(plants)
        diffResult.dispatchUpdatesTo(this)
    }

    class TodayHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(plant: Plant) {
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