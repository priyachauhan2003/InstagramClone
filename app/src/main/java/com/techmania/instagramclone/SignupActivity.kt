package com.techmania.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.techmania.instagramclone.Models.user
import com.techmania.instagramclone.databinding.ActivitySignupBinding
import com.techmania.instagramclone.utils.USER_NODE
import com.techmania.instagramclone.utils.USER_PROFILE_FOLDER
import com.techmania.instagramclone.utils.uploadImage

class SignupActivity : AppCompatActivity() {
    val binding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    lateinit var user:user
    private val launcher=registerForActivityResult(ActivityResultContracts.GetContent()){
        uri->
        uri?.let{
                uploadImage(uri, USER_PROFILE_FOLDER){
                    if(it==null){

                    }
                    else{
                        user.image=it
                        binding.profileImage.setImageURI(uri)
                    }
                }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val text="<font color=#FF000000>Already have an account? </font> <font color=#1E88E5>Login</font>"
        binding.login.setText(Html.fromHtml(text))

        user=user()
        if(intent.hasExtra("MODE")){
            if(intent.getIntExtra("MODE",-1)==1){
                binding.signUpButton.text="Update Profile"

                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                    .addOnSuccessListener {
                        user=it.toObject<user>()!!
                        if(!user.image.isNullOrEmpty()){
                            Picasso.get().load(user.image).into(binding.profileImage)
                        }
                        binding.name.editText?.setText(user.name)
                        binding.email.editText?.setText(user.email)
                        binding.password.editText?.setText(user.password)
                    }
            }
        }
        binding.signUpButton.setOnClickListener {
            if(intent.hasExtra("MODE")){
                if(intent.getIntExtra("MODE",-1)==1){
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid).set(user).
                        addOnSuccessListener {
                        startActivity(Intent(this@SignupActivity,HomeActivity::class.java))
                        finish()
                    }
                }
            }
            else {
                if(binding.name.editText?.text.toString().equals("") or
                    binding.email.editText?.text.toString().equals("") or
                    binding.password.editText?.text.toString().equals("")){
                    Toast.makeText(this@SignupActivity,"Please fill the all information",Toast.LENGTH_SHORT).show()
                }
                else{
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.email.editText?.text.toString(),
                        binding.password.editText?.text.toString()
                    ).addOnCompleteListener {
                            result ->

                        if(result.isSuccessful){
                            user.name=binding.name.editText?.text.toString()
                            user.email=binding.email.editText?.text.toString()
                            user.password=binding.password.editText?.text.toString()
                            Firebase.firestore.collection(USER_NODE)
                                .document(Firebase.auth.currentUser!!.uid).set(user).
                                addOnSuccessListener {
                                    startActivity(Intent(this@SignupActivity,HomeActivity::class.java))
                                    finish()
                                }
                        }
                        else{
                            Toast.makeText(this@SignupActivity,result.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        binding.addImage.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.login.setOnClickListener {
            startActivity(Intent(this@SignupActivity,LoginActivity::class.java))
            finish()
        }
    }
}