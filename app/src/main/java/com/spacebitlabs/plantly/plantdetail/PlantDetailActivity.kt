package com.spacebitlabs.plantly.plantdetail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.spacebitlabs.plantly.R
import com.spacebitlabs.plantly.data.entities.Plant
import kotlinx.android.synthetic.main.activity_plant_detail.*
import timber.log.Timber

class PlantDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant_detail)
        val plantId = intent.getLongExtra(EXTRA_PLANT_ID, 0)

        val model = ViewModelProviders.of(this).get(PlantDetailViewModel::class.java)
        model.plantDetailViewState.observe(this, Observer { state ->
            render(state)
        })

        model.getPlantDetail(plantId)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun render(state: PlantDetailViewState?) {
        when (state) {
            is PlantDetailViewState.PlantDetailLoaded -> renderPlantDetail(state.plant)
        }
    }

    private fun renderPlantDetail(plant: Plant) {
        Timber.d("Rendering plant detail")
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private const val EXTRA_PLANT_ID: String = "extra_plant_id"

        fun show(view: View?, plant: Plant) {
            val intent = Intent(view?.context, PlantDetailActivity::class.java)
            intent.putExtra(EXTRA_PLANT_ID, plant.id)
            view?.context?.startActivity(intent)
        }
    }
}
