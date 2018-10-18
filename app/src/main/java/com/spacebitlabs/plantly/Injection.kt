package com.spacebitlabs.plantly

import android.annotation.SuppressLint
import android.arch.persistence.room.Room
import android.content.Context
import com.spacebitlabs.plantly.data.PlantDatabase
import com.spacebitlabs.plantly.data.source.UserPlantsStore

/**
 * Created by afzal_najam on 2018-02-20.
 */
class Injection private constructor(private val appContext: Context) {

    private fun provideContext(): Context {
        return appContext
    }

    private fun provideDatabase(): PlantDatabase {
        return Room.databaseBuilder(provideContext(), PlantDatabase::class.java, DATABASE_FILE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    fun providePlantStore(): UserPlantsStore {
        val userPlantsStore = UserPlantsStore(provideDatabase())

        userPlantsStore.loadMockData()

        return userPlantsStore
    }

    companion object {
        private const val DATABASE_FILE_NAME: String = "plant_db"

        @SuppressLint("StaticFieldLeak", "Keeping app context is safe here")
        @Volatile
        private lateinit var INSTANCE: Injection

        fun init(appContext: Context) {
            INSTANCE = Injection(appContext)
        }

        fun get(): Injection {
            return INSTANCE
        }
    }
}