package com.eddyvarela.peter_and_friends.screen_fragments


import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import com.eddyvarela.peter_and_friends.R
import com.eddyvarela.peter_and_friends.data.Post
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.create_posting_fragment.*
import kotlinx.android.synthetic.main.job_post_row.*
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.net.URLEncoder
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class CreatePosting : DialogFragment() {

    companion object {
        const val TAG = "Create"
        private const val PERMISSION_REQUEST_CODE = 101
        private const val CAMERA_REQUEST_CODE = 102
    }

    var uploadBitmap: Bitmap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.create_posting_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btnSubmit.setOnClickListener {
            sendClick(view!!)
        }
        btnCaptureJobPhoto.setOnClickListener {
            val intentStartCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intentStartCamera, CAMERA_REQUEST_CODE)
        }
        requestNeededPermission()
    }


    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(
                context!!,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    android.Manifest.permission.CAMERA
                )
            ) {
                Toast.makeText(
                    context!!,
                    "I need it for camera", Toast.LENGTH_SHORT
                ).show()
            }

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CODE
            )
        } else {
            // we already have permission
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "CAMERA perm granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "CAMERA perm NOT granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun sendClick(v: View) {
        if (ivJobImg.visibility == View.GONE) {
            uploadPost()
        } else {
            uploadPostWithImage()
        }
    }

    fun uploadPost(imgURL: String = "") {

        var db = FirebaseFirestore.getInstance()
        var postCollection = db.collection("posts")


        var payment:Float = etJobPayment.text.toString().toFloat()

        var formatted = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var timeStamp = formatted.format(Date())

        val post = Post(
            FirebaseAuth.getInstance().currentUser!!.uid,
            timeStamp,
            FirebaseAuth.getInstance().currentUser!!.displayName!!,
            etJobTitle.text.toString(),
            etJobDescription.text.toString(),
            "",
                    imgURL,
            "%.2f".format(payment)
        )

        postCollection.add(post).addOnSuccessListener {
            Toast.makeText(activity, "Post saved successfully! ", Toast.LENGTH_LONG).show()

        }.addOnFailureListener {
            Toast.makeText(activity, "Post was not saved: ${it.message}", Toast.LENGTH_LONG).show()
        }
    }

    @Throws(Exception::class)
    private fun uploadPostWithImage() {

        val baos = ByteArrayOutputStream()
        uploadBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageInBytes = baos.toByteArray()

        val storageRef = FirebaseStorage.getInstance().reference
        val newImage = URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8") + ".jpg"
        val newImagesRef = storageRef.child("images/$newImage")

        newImagesRef.putBytes(imageInBytes)
            .addOnFailureListener { exception ->
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {

                newImagesRef.downloadUrl.addOnCompleteListener { task -> uploadPost(task.result.toString()) }
            }
    }


}