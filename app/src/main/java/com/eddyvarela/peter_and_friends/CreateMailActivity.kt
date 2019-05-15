package com.eddyvarela.peter_and_friends


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import com.eddyvarela.peter_and_friends.data.Mail
import com.eddyvarela.peter_and_friends.data.ReceiverModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.zxing.Result
import com.livinglifetechway.quickpermissions.annotations.WithPermissions
import kotlinx.android.synthetic.main.mail_create.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.util.*

class CreateMailActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 101
        private const val CAMERA_REQUEST_CODE = 102
    }

    var uploadBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mail_create)

        btnAttach.setOnClickListener {
            val intentStartCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intentStartCamera, CAMERA_REQUEST_CODE)
        }

        btnQR.setOnClickListener {
            zxingView.visibility = View.VISIBLE
        }

        requestNeededPermission()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            uploadBitmap = data!!.extras.get("data") as Bitmap
            imgAttach.setImageBitmap(uploadBitmap)
            imgAttach.visibility = View.VISIBLE
        }
    }


    private fun requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.CAMERA
                )
            ) {
                Toast.makeText(
                    this,
                    "I need it for camera", Toast.LENGTH_SHORT
                ).show()
            }

            ActivityCompat.requestPermissions(
                this,
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
                    Toast.makeText(this, "CAMERA perm granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "CAMERA perm NOT granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun sendClick(v: View) {
        if (imgAttach.visibility == View.GONE) {
            uploadMail()
        } else {
            uploadMailWithImage()
        }
    }

    fun uploadMail(imageUrl: String = "") {
        val mail = Mail(
            FirebaseAuth.getInstance().currentUser!!.uid,
            //TODO
            Date(1,1,1,1,1,1),
            FirebaseAuth.getInstance().currentUser!!.email!!,
            etTitle.text.toString(),
            etReceiver.text.toString(),
            etBody.text.toString(),
            imageUrl
        )

        var mailsCollection = FirebaseFirestore.getInstance().collection(
            "mails"
        )

        mailsCollection.add(
            mail
        ).addOnSuccessListener {
            Toast.makeText(
                this@CreateMailActivity,
                "Mail saved", Toast.LENGTH_LONG
            ).show()

            ReceiverModel.receiver = etReceiver.text.toString()

            finish()
        }.addOnFailureListener {
            Toast.makeText(
                this@CreateMailActivity,
                "Error: ${it.message}", Toast.LENGTH_LONG
            ).show()
        }
    }


    @Throws(Exception::class)
    private fun uploadMailWithImage() {

        val baos = ByteArrayOutputStream()
        uploadBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageInBytes = baos.toByteArray()

        val storageRef = FirebaseStorage.getInstance().getReference()
        val newImage = URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8") + ".jpg"
        val newImagesRef = storageRef.child("images/$newImage")

        newImagesRef.putBytes(imageInBytes)
            .addOnFailureListener { exception ->
                Toast.makeText(this@CreateMailActivity, exception.message, Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener { taskSnapshot ->
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.

                newImagesRef.downloadUrl.addOnCompleteListener(object : OnCompleteListener<Uri> {
                    override fun onComplete(task: Task<Uri>) {
                        uploadMail(task.result.toString())
                    }
                })
            }
    }

    // QR Code

    public override fun onResume() {
        super.onResume()
        startCamera()
    }

    @WithPermissions(
        permissions = [Manifest.permission.CAMERA]
    )
    private fun startCamera() {
        zxingView.setResultHandler(this) // Register ourselves as a handler for scan results.
        zxingView.startCamera()          // Start camera on resume
    }

    public override fun onPause() {
        super.onPause()
        zxingView.stopCamera()           // Stop camera on pause
    }

    override fun handleResult(rawResult: Result) {

        val res: String = rawResult.text
        etBody.setText(res)

        // If you would like to resume scanning, call this method below:
        zxingView.resumeCameraPreview(this)
    }
}