package com.arajdianaltaf.favdish.model.entities.database

import androidx.room.Dao
import androidx.room.Insert
import com.arajdianaltaf.favdish.model.entities.FavDish

@Dao
interface FavDishDao {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)

}