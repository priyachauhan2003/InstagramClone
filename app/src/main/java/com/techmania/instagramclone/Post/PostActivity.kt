package com.techmania.instagramclone.Post

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.techmania.instagramclone.HomeActivity
import com.techmania.instagramclone.Models.Post
import com.techmania.instagramclone.Models.user
import com.techmania.instagramclone.R
import com.techmania.instagramclone.databinding.ActivityPostBinding
import com.techmania.instagramclone.utils.POST
import com.techmania.instagramclone.utils.POST_FOLDER
import com.techmania.instagramclone.utils.USER_NODE
import com.techmania.instagramclone.utils.USER_PROFILE_FOLDER
import com.techmania.instagramclone.utils.uploadImage

class PostActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }
    var imageUrl: String? = null
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, POST_FOLDER) { url ->
                if (url != null) {
                    binding.selectImage.setImageURI(uri)
                    imageUrl = url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowHomeEnabled(true);

        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@PostActivity, HomeActivity::class.java))
            finish()
        }

        binding.selectImage.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this@PostActivity, HomeActivity::class.java))
            finish()
        }

        binding.postButton.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document().get().addOnSuccessListener {
                var user = it.toObject<user>()!!
                var postUrl: String = imageUrl!!
                var caption: String = binding.caption.editText?.text.toString()
                var userName: String = user.name.toString()
                var time: String = System.currentTimeMillis().toString()
                val post: Post = Post(postUrl, caption, userName, time)
                Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                    Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document()
                        .set(post).addOnSuccessListener {
                        startActivity(Intent(this@PostActivity, HomeActivity::class.java))
                        finish()
                    }
                }
            }

        }
    }

}