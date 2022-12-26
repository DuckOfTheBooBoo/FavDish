package com.arajdianaltaf.favdish.model.entities.database

import androidx.annotation.WorkerThread
import com.arajdianaltaf.favdish.model.entities.FavDish

class FavDishRepository(private val favDishDao: FavDishDao) {

    @WorkerThread
    suspend fun insertFavDishData(favDish: FavDish) {
        favDishDao.insertFavDishDetails(favDish)
    }

}