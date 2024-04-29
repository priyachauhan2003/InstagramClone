package com.techmania.instagramclone.Post

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.techmania.instagramclone.HomeActivity
import com.techmania.instagramclone.Models.Post
import com.techmania.instagramclone.Models.Reel
import com.techmania.instagramclone.Models.user
import com.techmania.instagramclone.R
import com.techmania.instagramclone.databinding.ActivityReelsBinding
import com.techmania.instagramclone.utils.POST
import com.techmania.instagramclone.utils.REEL
import com.techmania.instagramclone.utils.REEL_FOLDER
import com.techmania.instagramclone.utils.USER_NODE
import com.techmania.instagramclone.utils.uploadVideo

class ReelsActivity : AppCompatActivity() {
    val binding by lazy{
        ActivityReelsBinding.inflate(layoutInflater)
    }
    private lateinit var videoUrl:String
    lateinit var progressDialog:ProgressDialog
    private  val launcher=registerForActivityResult(ActivityResultContracts.GetContent()){
        uri->
        uri?.let {
            uploadVideo(uri, REEL_FOLDER,progressDialog){
                url->
                if(url!=null){
                    videoUrl=url
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        progressDialog=ProgressDialog(this)

        binding.selectReel.setOnClickListener {
            launcher.launch("video/*")
        }

        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this@ReelsActivity, HomeActivity::class.java))
            finish()
        }

        binding.postButton.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
                var user: user =it.toObject<user>()!!

                val reel:Reel = Reel(videoUrl!!,binding.caption.editText?.text.toString(),user.image!!)
                Firebase.firestore.collection(REEL).document().set(reel).addOnSuccessListener {
                    Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+REEL).document().set(reel).addOnSuccessListener {
                        startActivity(Intent(this@ReelsActivity,HomeActivity::class.java))
                        finish()
                    }
                }

            }

        }
    }
}