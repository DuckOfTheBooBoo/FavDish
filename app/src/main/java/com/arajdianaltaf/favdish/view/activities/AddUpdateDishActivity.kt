package com.arajdianaltaf.favdish.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.arajdianaltaf.favdish.R
import com.arajdianaltaf.favdish.application.FavDishApplication
import com.arajdianaltaf.favdish.databinding.ActivityAddUpdateDishBinding
import com.arajdianaltaf.favdish.databinding.DialogCustomImageSelectionBinding
import com.arajdianaltaf.favdish.databinding.DialogCustomListBinding
import com.arajdianaltaf.favdish.model.entities.FavDish
import com.arajdianaltaf.favdish.utils.Constants
import com.arajdianaltaf.favdish.view.adapters.CustomListItemAdapter
import com.arajdianaltaf.favdish.viewmodel.FavDishViewModel
import com.arajdianaltaf.favdish.viewmodel.FavDishViewModelFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var myBinding: ActivityAddUpdateDishBinding
    private var myImagePath: String = ""

    private val myFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((application as FavDishApplication).repository)
    }


    companion object {
        private const val CAMERA = 1
        private const val GALLERY = 2
        private const val IMAGE_DIR = "FavDishImages"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)

        setContentView(myBinding.root)

        setupActionBar()
        dropdownMenu()

        myBinding.ivAddDishImage.setOnClickListener(this)
        myBinding.btnAddDish.setOnClickListener(this)
    }

    private fun setupActionBar() {
        setSupportActionBar(myBinding.toolbarAddDishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        myBinding.toolbarAddDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun customImageSelectionDialog() {
        val dialog = Dialog(this)
        val binding: DialogCustomImageSelectionBinding = DialogCustomImageSelectionBinding.inflate(layoutInflater)

        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {
            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object: MultiplePermissionsListener {

                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if(report.areAllPermissionsGranted()) {
                            val camIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(camIntent, CAMERA)
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                   showRationalDialogForPermissions()
                }

            }).onSameThread().check()

            dialog.dismiss()
        }

        binding.tvGallery.setOnClickListener {

            Dexter.withContext(this).withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,

            ).withListener(object: PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, GALLERY)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(
                        this@AddUpdateDishActivity,
                        "You have denied the storage permission to select image.",
                        Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }

            }).onSameThread().check()

            dialog.dismiss()
        }

        dialog.show()


    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val wrapper = ContextWrapper(applicationContext)

        var file = wrapper.getDir(IMAGE_DIR, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file.absolutePath
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

//            Assign camera photo to the thumbnail if true
            if (requestCode == CAMERA) {
                data?.extras?.let {
                    val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap
//                    myBinding.ivDishImage.setImageBitmap(thumbnail)

                    Glide.with(this)
                        .load(thumbnail)
                        .centerCrop()
                        .into(myBinding.ivDishImage)

                    myImagePath = saveImageToInternalStorage(thumbnail)
                    Log.i("ImagePath", myImagePath)

//                    Set icon to Edit
                    myBinding.ivAddDishImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_edit_icon))
                }
            }

            if (requestCode == GALLERY) {
                data?.let {

//                    Get image uri
                    val selectedPhotoUri = data.data

//                    myBinding.ivDishImage.setImageURI(selectedPhotoUri)

                    Glide.with(this)
                        .load(selectedPhotoUri)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object: RequestListener<Drawable>{
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.e("TAG", "Error loading image", e)
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource?.let{
                                    val bitmap: Bitmap = resource.toBitmap()
                                    myImagePath = saveImageToInternalStorage(bitmap)
                                    Log.i("ImagePath", myImagePath)
                                }
                                return false
                            }

                        })
                        .into(myBinding.ivDishImage)

                    //                    Set icon to Edit
                    myBinding.ivAddDishImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_edit_icon))

                }
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "User cancelled image selection.")
        }
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this).setMessage("You turned off permissions required to this feature. Please enable it under Application Settings").setPositiveButton("GO TO SETTINGS") {
            _,_ -> try {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
            .setNegativeButton("Cancel"){
                dialog, _ -> dialog.dismiss()
            }.show()
    }

    private fun dropdownMenu(){

        val itemTypes = Constants.dishTypes()
        val itemCategories = Constants.dishCategories()
        val itemCookingTimes = Constants.dishCookTime()

        val typesAdapter = ArrayAdapter(this, R.layout.dropdown_item, itemTypes)
        val categoriesAdapter = ArrayAdapter(this, R.layout.dropdown_item, itemCategories)
        val cookingTimesAdapter = ArrayAdapter(this, R.layout.dropdown_item, itemCookingTimes)

        myBinding.actvType.setAdapter(typesAdapter)
        myBinding.actvCategory.setAdapter(categoriesAdapter)
        myBinding.actvCookingTime.setAdapter(cookingTimesAdapter)
    }

    override fun onClick(v: View?) {
        if(v != null){
            when (v.id) {
                R.id.iv_add_dish_image -> {
                    customImageSelectionDialog()
                }

                R.id.btn_add_dish -> {
                    val title = myBinding.etTitle.text.toString().trim { it <= ' '}
                    val type = myBinding.actvType.text.toString().trim { it <= ' '}
                    val category = myBinding.actvCategory.text.toString().trim { it <= ' '}
                    val ingredients = myBinding.etIngredients.text.toString().trim { it <= ' '}
                    val cookingTimeInMinutes = myBinding.actvCookingTime.text.toString().trim { it <= ' '}
                    val cookingDirection = myBinding.etDirectionToCook.text.toString().trim { it <= ' '}

                    when {
                        TextUtils.isEmpty(myImagePath) -> {
                            Toast.makeText(this@AddUpdateDishActivity, resources.getString(R.string.err_msg_select_dish_image), Toast.LENGTH_SHORT).show()
                        }

                        TextUtils.isEmpty(title) -> {
                            Toast.makeText(this@AddUpdateDishActivity, resources.getString(R.string.err_msg_enter_dish_title), Toast.LENGTH_SHORT).show()
                        }

                        TextUtils.isEmpty(type) -> {
                            Toast.makeText(this@AddUpdateDishActivity, resources.getString(R.string.err_msg_select_dish_type), Toast.LENGTH_SHORT).show()
                        }

                        TextUtils.isEmpty(category) -> {
                            Toast.makeText(this@AddUpdateDishActivity, resources.getString(R.string.err_msg_select_dish_cat), Toast.LENGTH_SHORT).show()
                        }

                        TextUtils.isEmpty(ingredients) -> {
                            Toast.makeText(this@AddUpdateDishActivity, resources.getString(R.string.err_msg_enter_dish_ingredients), Toast.LENGTH_SHORT).show()
                        }

                        TextUtils.isEmpty(cookingTimeInMinutes) -> {
                            Toast.makeText(this@AddUpdateDishActivity, resources.getString(R.string.err_msg_select_cooking_time), Toast.LENGTH_SHORT).show()
                        }

                        TextUtils.isEmpty(cookingDirection) -> {
                            Toast.makeText(this@AddUpdateDishActivity, resources.getString(R.string.err_msg_enter_dish_cook_instructions), Toast.LENGTH_SHORT).show()
                        }

                        else -> {
                            val favDishDetails: FavDish = FavDish(
                                image = myImagePath,
                                imageSource = Constants.DISH_IMAGE_SOURCE_LOCAL,
                                title = title,
                                type = type,
                                category = category,
                                ingredients = ingredients,
                                cookingTime = cookingTimeInMinutes,
                                directionToCook = cookingDirection,
                                favoriteDish = false
                            )

                            myFavDishViewModel.insert(favDishDetails)
                            Toast.makeText(
                                this@AddUpdateDishActivity,
                                "You successfully added your favorite dish details.",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("Insertion", "Success")
                            finish()
                        }

                    }
                }
            }
        }
    }

}
