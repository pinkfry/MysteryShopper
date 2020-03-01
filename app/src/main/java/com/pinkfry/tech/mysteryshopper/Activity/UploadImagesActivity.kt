package com.pinkfry.tech.mysteryshopper.Activity

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.pinkfry.tech.mysteryshopper.R
import com.pinkfry.tech.mysteryshopper.model.GalleryModel
import kotlinx.android.synthetic.main.activity_add_client.btnChooseImage
import kotlinx.android.synthetic.main.activity_add_client.imageShowSelectedImage
import kotlinx.android.synthetic.main.activity_add_client.tvPercentage
import kotlinx.android.synthetic.main.activity_upload_images.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet.btnUploadImage


class UploadImagesActivity : AppCompatActivity() {
    private val PICK_REQUEST = 2020
    private val CAMERA_PIC_REQUEST = 316
    lateinit var filePath: Uri
    var count = 0
    lateinit var clientName: String
    lateinit var storeName: String
    lateinit var storage: StorageReference
    private val databaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_images)
        storage = FirebaseStorage.getInstance().reference
        clientName=intent.getStringExtra("clientName")!!
        storeName=intent.getStringExtra("storeName")!!
        btnChooseImage.setOnClickListener { choosePhoto() }
        btnUploadImage.setOnClickListener { uploadPhoto(clientName,storeName) }
        btnOpenCamera.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED&& ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST)
            }
            else{
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE),1805)

            }
        }
    }

    private fun choosePhoto() {
        var intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT;
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                filePath = data!!.data!!
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                    imageShowSelectedImage.setImageBitmap(bitmap)

                    count = 1
                } catch (e: Exception) {

                }

            }

        } else if (requestCode == CAMERA_PIC_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val photo =data!!.extras!!.get("data") as Bitmap
                var path = MediaStore.Images.Media.insertImage(contentResolver, photo, "Title", null)

                filePath = Uri.parse(path)
                try {
//                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                    imageShowSelectedImage.setImageBitmap(photo)

                    count = 1
                } catch (e: Exception) {

                }
            }
        }
    }

    private fun uploadPhoto(clientName: String,storeName:String) {
        if (count == 1) {
            var childReference = storage.child("IMAGES").child(clientName.toUpperCase()).child(storeName.toUpperCase())
                .child(System.currentTimeMillis().toString())
            childReference.putFile(filePath).addOnSuccessListener { it ->
                Toast.makeText(this, "${it.storage.downloadUrl}", Toast.LENGTH_SHORT).show()
                childReference.downloadUrl.addOnSuccessListener {
                    databaseReference.child(resources.getString(R.string.FirebaseClient)).child(clientName).child(resources.getString(R.string.firebaseStore)).child(storeName).child("gallery").push().setValue(GalleryModel(it.toString(),etTagLine.text.toString()))

                }
                Toast.makeText(this, resources.getString(R.string.successfullyAdded), Toast.LENGTH_SHORT).show()
                count = 0
            }.addOnProgressListener {
                val perUploade = it.bytesTransferred / it.totalByteCount * 100.0
                tvPercentage.text = "$perUploade%"

            }.addOnFailureListener {
                Toast.makeText(this, resources.getString(R.string.failed), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, resources.getString(R.string.pleaseSelectImage), Toast.LENGTH_SHORT).show()
        }
    }
}
