package com.arajdianaltaf.favdish.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arajdianaltaf.favdish.R
import com.arajdianaltaf.favdish.databinding.ActivityAddUpdateDishBinding

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var myBinding: ActivityAddUpdateDishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)

        setContentView(myBinding.root)

        setupActionBar()

        myBinding.ivAddDishImage.setOnClickListener(this)
    }

    private fun setupActionBar() {
        setSupportActionBar(myBinding.toolbarAddDishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        myBinding.toolbarAddDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onClick(v: View?) {
        if(v != null){
            when (v.id) {
                R.id.iv_add_dish_image -> {
                    Toast.makeText(this, "You have clicked the ImageView", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }
    }

}