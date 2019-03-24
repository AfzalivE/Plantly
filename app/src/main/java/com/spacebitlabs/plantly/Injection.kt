@file:Suppress("MemberVisibilityCanBePrivate")

package com.spacebitlabs.plantly

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room
import com.spacebitlabs.plantly.data.PlantDatabase
import com.spacebitlabs.plantly.data.Prefs
import com.spacebitlabs.plantly.data.UserPlantsStore
import com.spacebitlabs.plantly.reminder.WorkReminder

/**
 * Created by afzal_najam on 2018-02-20.
 */
class Injection private constructor(private val appContext: Context) {

    private val database: PlantDatabase by lazy {
        Room.databaseBuilder(provideContext(), PlantDatabase::class.java, DATABASE_FILE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    private val userPlantsStore: UserPlantsStore by lazy {
        UserPlantsStore(provideDatabase(), provideWorkReminder())
    }

    private val prefs: Prefs by lazy {
        Prefs(provideContext())
    }

    private val workReminder: WorkReminder by lazy {
        WorkReminder(providePrefs())
    }

    fun provideContext(): Context = appContext

    fun provideDatabase(): PlantDatabase = database

    fun providePlantStore() = userPlantsStore.also {
//        it.loadMockSeedData()
    }

    fun providePrefs() = prefs

    fun provideWorkReminder() = workReminder

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