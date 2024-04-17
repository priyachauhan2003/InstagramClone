package com.techmania.instagramclone.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.techmania.instagramclone.Models.user
import com.techmania.instagramclone.R
import com.techmania.instagramclone.SignupActivity
import com.techmania.instagramclone.databinding.FragmentProfileBinding
import com.techmania.instagramclone.utils.USER_NODE

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.editProfile.setOnClickListener {
            val intent= Intent(activity,SignupActivity::class.java)
            intent.putExtra("MODE",1)
            activity?.startActivity(intent)
        }

        return binding.root
    }

    companion object {

    }

    override fun onStart() {
        super.onStart()
        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                val user:user=it.toObject<user>()!!
                binding.personName.text=user.name
                binding.personBio.text=user.email

                if(!user.image.isNullOrEmpty()){
                    Picasso.get().load(user.image).into(binding.profileImage2)
                }
            }
    }
}