package com.twoinc.twopay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val TAG = "FragmentActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun signUpPage(view: View){
        val changePage = Intent(this,SignUp::class.java)
        startActivity(changePage)
    }

    fun goToHomePage(){
        val changePage = Intent(this,HomePage::class.java)
        var usernameField : EditText = findViewById(R.id.usernameField)
        var username = usernameField.text.toString()
        var passwordField : EditText = findViewById(R.id.passwordField)
        var password = passwordField.text.toString()
        changePage.putExtra("Username",username)
        changePage.putExtra("Password",password)
        startActivity(changePage)
    }

    fun login(view: View){
        val db = Firebase.firestore
        val colRef = db.collection("Users")

        val usernameField : EditText = findViewById(R.id.usernameField)
        if(usernameField.text.toString().isNotEmpty()){
            Log.v("EditText","Username : " + usernameField.text.toString())
        }else(
                Log.d(TAG,"Username is null")
        )

        val passwordField : EditText = findViewById(R.id.passwordField)
        if(passwordField.text.toString().isNotEmpty()){
            Log.v("EditText","Password : " + passwordField.text.toString())
        }else(
                Log.d(TAG,"Password is null")
                )
        
        colRef.addSnapshotListener{ value, e ->
            if(e != null) {
                Log.w(TAG,"Listen Failed." , e)
                return@addSnapshotListener
            }

            val users = ArrayList<String>()
            for(doc in value!!){
                doc.getString("Username")?.let{
                    users.add(it)
                }
            }
            Log.d(TAG,"User list : $users")
            if(usernameField.text.toString().isNotBlank() && passwordField.text.toString().isNotEmpty()){
                for(username in users){
                    if(usernameField.text.toString() == username){
                        val username = usernameField.text.toString()
                        Log.d(TAG, "User available")
                        db.collection("Users")
                                .whereEqualTo("Username", username)
                                .get()
                                .addOnSuccessListener { documents ->
                                    for (document in documents) {
                                        val data = document.data
                                        val password = passwordField.text.toString()
                                        if(password == data["Password"].toString() ){
                                            Log.d(TAG, "${document.id} => ${data["Password"]} + Success")
                                            goToHomePage()
                                        }

                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.w(TAG, "Error getting documents: ", exception)
                                }
                    }else(
                            Log.d(TAG,"Wrong Password or Username")
                    )
                }
            }


    }


}}




