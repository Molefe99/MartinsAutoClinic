package com.example.martinsautoclinic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.martinsautoclinic.RequestQuote.Companion
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuotesActivity : AppCompatActivity() {

    private lateinit var buttonAddImage: Button
    private lateinit var removeButton : Button
    private lateinit var imageView: ImageView
    private lateinit var clientText: EditText
    private lateinit var sendRequest : Button

    private lateinit var URIfor: Uri;
    private var imageFound: Boolean = false;
    private lateinit var dbRef: DatabaseReference
    private lateinit var saveIMageRealtime: String
    private val FIREBASE_IMAGE_PATH = "RequestedQuoteImages"


    companion object {

        val Image_request_code = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_quotes)


        removeButton = findViewById(R.id.buttonRemove)
        buttonAddImage = findViewById(R.id.buttonAddImage)
        imageView = findViewById(R.id.imageViewChosenPicture)
        clientText = findViewById(R.id.editTextClientRequestText)
        sendRequest = findViewById(R.id.buttonSendRequest)

        clearText()

        val btnBack = findViewById<Button>(R.id.buttonBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        buttonAddImage.setOnClickListener {
            pickGallaryImage()
        }

        removeButton.setOnClickListener {
            clearImage()
        }


        sendRequest.setOnClickListener {

            dbRef = FirebaseDatabase.getInstance().getReference("RequestQuotes");

            if (clientText.text.isEmpty()) {
                clientText.error = "Data field is empty"
            }

            if (clientText.text.isNotEmpty() && !imageFound) {

                val privateKey = dbRef.push().key.toString();
                val requestQuote = ModelRequestQuote(clientText.text.toString());

                dbRef.child(privateKey).setValue(requestQuote).addOnCompleteListener {
                    Toast.makeText(this, "Data has been inserted", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "An Error Occurred", Toast.LENGTH_LONG).show()
                }

            } else if (clientText.text.isNotEmpty() && imageFound) {

                CoroutineScope(Dispatchers.Default).launch {
                    uploadFirebase(URIfor)
                }
            }
        }
    }

    private fun pickGallaryImage() {
        val intent = Intent(Intent.ACTION_PICK)
        //item picked is of type image
        intent.type = "image/*"
        startActivityForResult(intent, RequestQuote.Image_request_code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestQuote.Image_request_code && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                imageView.setImageURI(uri)
                URIfor = uri
                imageFound = true
                removeButton.visibility = View.VISIBLE
                buttonAddImage.text = "Image added"
            } ?: run {
                Toast.makeText(this, "Image selection failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun uploadFirebase(imageUri: Uri) {

        val storageRef = FirebaseStorage.getInstance().reference

        val fileRef =
            storageRef.child(FIREBASE_IMAGE_PATH).child(imageUri.lastPathSegment ?: "default.jpg")

        fileRef.putFile(imageUri).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { uri ->
                saveIMageRealtime = uri.toString()
                saveQuoteToDatabase()
                Toast.makeText(this, "Image Uploaded", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Image upload failed", Toast.LENGTH_LONG).show()
        }
    }

    private fun saveQuoteToDatabase() {
        val privateKey = dbRef.push().key.toString()
        val requestQuote = ModelRequestQuote(clientText.text.toString(), saveIMageRealtime)
        dbRef.child(privateKey).setValue(requestQuote).addOnCompleteListener {
            Toast.makeText(this, "Data has been inserted", Toast.LENGTH_LONG).show()
        }.addOnFailureListener { err ->
            Toast.makeText(this, "Data insertion failed", Toast.LENGTH_LONG).show()
        }
    }

    private fun clearText(){
        clientText.text.clear()
        imageView.setImageResource(R.drawable.logo)
    }

    private fun clearImage(){
        val defaultMessage =  "Add Image"

        buttonAddImage.text = defaultMessage
        imageFound = false;
        imageView.setImageResource(R.drawable.logo)//original
        removeButton.visibility = View.GONE
    }

}