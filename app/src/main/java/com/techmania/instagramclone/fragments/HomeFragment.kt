package com.techmania.instagramclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.play.integrity.internal.ac
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.techmania.instagramclone.Models.Post
import com.techmania.instagramclone.Models.user
import com.techmania.instagramclone.R
import com.techmania.instagramclone.adapter.FollowAdapter
import com.techmania.instagramclone.adapter.PostAdapter
import com.techmania.instagramclone.databinding.ActivityReelsBinding
import com.techmania.instagramclone.databinding.FragmentHomeBinding
import com.techmania.instagramclone.utils.FOLLOW
import com.techmania.instagramclone.utils.POST

class HomeFragment : Fragment() {
    lateinit var binding:FragmentHomeBinding
    private var postList=ArrayList<Post>()
    private lateinit var adapter: PostAdapter

    private var followList=ArrayList<user>()
    private lateinit var followAdapter: FollowAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater, container, false)
        adapter= PostAdapter(requireContext(),postList)
        binding.postRv.layoutManager=LinearLayoutManager(requireContext())
        binding.postRv.adapter=adapter

        followAdapter= FollowAdapter(requireContext(),followList)
        binding.followRv.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.followRv.adapter=followAdapter

        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.materialToolbar2)

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ FOLLOW).get()
            .addOnSuccessListener {
                var tempList= arrayListOf<user>()!!
                followList.clear()
                for(i in it.documents){
                    var user: user=i.toObject<user>()!!
                    tempList.add(user)
                }
                followList.addAll(tempList)
                followAdapter.notifyDataSetChanged()
            }

        Firebase.firestore.collection(POST).get().addOnSuccessListener {
            var tempList=ArrayList<Post>()
            postList.clear()
            for(i in it.documents){
                var post:Post=i.toObject<Post>()!!
                tempList.add(post)
            }
            postList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }
        return binding.root
    }

    companion object {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}