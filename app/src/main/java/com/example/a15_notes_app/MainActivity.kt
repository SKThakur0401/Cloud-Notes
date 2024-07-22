package com.example.a15_notes_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.a15_notes_app.databinding.ActivityMainBinding
import com.example.a15_notes_app.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null

    @Inject
    lateinit var tokenManager:TokenManager

    lateinit var navController:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        // Code to decide which fragment to display when the app is starting --v  (If user has signed in then his note,
        // else registration fragment

//        if(tokenManager.isLoggedIn()==null)
//        {
//            findNavController(androidx.navigation.fragment.R.id.nav_host_fragment_container).navigate(R.id.mainFragment)
//        }
//
//        else
//        {
//            findNavController(androidx.navigation.fragment.R.id.nav_host_fragment_container).navigate(R.id.registrationFragment)
//        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}