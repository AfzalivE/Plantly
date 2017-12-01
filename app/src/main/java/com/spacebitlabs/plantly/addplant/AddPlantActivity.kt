package com.spacebitlabs.plantly.addplant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.spacebitlabs.plantly.R
import kotlinx.android.synthetic.main.activity_addplant.*

/**
 * Created by afzal on 2017-12-01.
 */
class AddPlantActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addplant)

        save.setOnClickListener {
            // TODO Validate input

        }
    }

    companion object {
        fun show(context: Context) {
            context.startActivity(Intent(context, AddPlantActivity::class.java))
        }
    }
}