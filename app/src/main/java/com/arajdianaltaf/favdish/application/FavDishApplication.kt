package com.arajdianaltaf.favdish.application

import android.app.Application
import com.arajdianaltaf.favdish.model.entities.database.FavDishRepository
import com.arajdianaltaf.favdish.model.entities.database.FavDishRoomDatabase

class FavDishApplication: Application() {

    private val database by lazy { FavDishRoomDatabase.getDatabase(this@FavDishApplication) }

    val repository by lazy { FavDishRepository(database.favDishDao()) }

}