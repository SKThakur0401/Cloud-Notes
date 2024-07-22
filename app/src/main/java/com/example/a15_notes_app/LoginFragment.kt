package com.example.a15_notes_app

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.a15_notes_app.data.api.NotesApi
import com.example.a15_notes_app.data.model.NetworkResult
import com.example.a15_notes_app.data.model.NotesItem
import com.example.a15_notes_app.data.model.UserLoginData
import com.example.a15_notes_app.databinding.FragmentLoginBinding
import com.example.a15_notes_app.ui.ViewModel.AuthViewModel
import com.example.a15_notes_app.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    lateinit var binding:FragmentLoginBinding
    lateinit var authViewModel:AuthViewModel

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    lateinit var notesApi: NotesApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel= ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)
        clickListener()
    }

    fun clickListener()
    {
        binding.btnLogin.setOnClickListener{

            var basicCheck= authViewModel.basicValidations(Credentials_Entered_By_User(), true)


            if(basicCheck.first == true)
            {
                authViewModel.signIn(Credentials_Entered_By_User())
            }

            else display(basicCheck.second, true)
        }

        binding.tvCreateNewAcc.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        observer()
    }

    private fun observer()
    {
        authViewModel.userApiResponseLD.observe(viewLifecycleOwner, Observer{event->

            binding.progressBar.isVisible=false

            event.getContentIfNotHandled()?.let {

                when(it)
                {
                    is NetworkResult.Loading->{
                        binding.progressBar.isVisible=true
                    }

                    is NetworkResult.Success->{
                        binding.progressBar.isVisible=false
                        tokenManager.saveToken(it.data!!.token)

                        findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                    }

                    is NetworkResult.Error->{
                        binding.progressBar.isVisible=false
                        display("Some Error occured!", false)
                    }
                }
            }
        })
    }


    fun Credentials_Entered_By_User(): UserLoginData {
        val username = ""
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        return UserLoginData(username, email, password)
    }

    fun display(msg: String, isShort: Boolean) {

        if (isShort) {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        authViewModel.userApiResponseLD.removeObservers(viewLifecycleOwner)
    }
}