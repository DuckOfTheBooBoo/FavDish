package com.arajdianaltaf.favdish.utils

object Constants{
    const val DISH_TYPE: String = "DishType"
    const val DISH_CATEGORY: String = "DishCategory"
    const val DISH_COOKING_TIME: String = "DishCookingTime"

    fun dishTypes(): ArrayList<String>{
        val list = ArrayList<String>()

        list.add("breakfast")
        list.add("lunch")
        list.add("snacks")
        list.add("dinner")
        list.add("salad")
        list.add("side dish")
        list.add("dessert")
        list.add("other")

        return list
    }

    fun dishCategories(): ArrayList<String> {
        val list = ArrayList<String>()

        list.add("Pizza")
        list.add("BBQ")
        list.add("Bakery")
        list.add("Burger")
        list.add("Cafe")
        list.add("Chicken")
        list.add("Dessert")
        list.add("Drinks")
        list.add("Hot Dogs")
        list.add("Juices")
        list.add("Sandwich")
        list.add("Tea & Coffee")
        list.add("Wraps")
        list.add("Other")

        return list
    }

    fun dishCookTime(): ArrayList<String> {
        val list = ArrayList<String>()
        val times = listOf<String>(
            "10",
            "15",
            "20",
            "30",
            "45",
            "50",
            "60",
            "90",
            "120",
            "150",
            "180"
        )

        for (time in times) {
            list.add(time)
        }

        return list
    }

}