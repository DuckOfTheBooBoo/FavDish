package com.arajdianaltaf.favdish.viewmodel

import androidx.annotation.WorkerThread
import androidx.lifecycle.*
import com.arajdianaltaf.favdish.model.entities.FavDish
import com.arajdianaltaf.favdish.model.entities.database.FavDishRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FavDishViewModel(private val repository: FavDishRepository): ViewModel() {

    @WorkerThread
    fun insert(dish: FavDish) = viewModelScope.launch {
        repository.insertFavDishData(dish)
    }

    val allDishesList: LiveData<List<FavDish>> = repository.allDishesList.asLiveData()

}

class FavDishViewModelFactory(private val repository: FavDishRepository): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(FavDishViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return FavDishViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel Class")

    }

}