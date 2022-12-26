package com.arajdianaltaf.favdish.model.entities.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arajdianaltaf.favdish.model.entities.FavDish

@Database(entities = [FavDish::class], version = 1)
abstract class FavDishRoomDatabase: RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: FavDishRoomDatabase? = null

        fun getDatabase(context: Context): FavDishRoomDatabase {
            // If the INSTANCE is not null, then return i,
            // If it is, then create the database

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavDishRoomDatabase::class.java,
                    "fav_dish_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}