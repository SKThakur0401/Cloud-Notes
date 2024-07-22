package com.example.a15_notes_app

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.a15_notes_app.data.model.NetworkResult
import com.example.a15_notes_app.data.model.UserLoginData
import com.example.a15_notes_app.databinding.FragmentRegistrationBinding
import com.example.a15_notes_app.ui.ViewModel.AuthViewModel
import com.example.a15_notes_app.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [registrationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class registrationFragment : Fragment() {

    lateinit var binding:FragmentRegistrationBinding
    lateinit var authViewModel: AuthViewModel

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onAttach(context: Context) {   // overriding to handle the case where user has
        super.onAttach(context)                 // already signed-in and needs to directly
                                                // navigate to main fragment
        if(tokenManager.isLoggedIn() == true)
        {
            findNavController().navigate(R.id.action_registrationFragment_to_mainFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel= ViewModelProvider(requireActivity())[AuthViewModel::class.java]

        listeners()
        observers()
    }


    fun listeners()
    {
        binding.tvAlreadyHaveAcc.setOnClickListener{
            it.findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
        }

        binding.btnSignup.setOnClickListener{

            var basicChecks= authViewModel.basicValidations(Credentials_Entered_By_User(),false)

            if(basicChecks.first)
            {
                authViewModel.signUp(Credentials_Entered_By_User())
            }

            else display(basicChecks.second, true)
        }
    }


    fun observers()
    {
        authViewModel.userApiResponseLD.observe(viewLifecycleOwner){event->


            event.getContentIfNotHandled()?.let {

                when(it)
                {
                    is NetworkResult.Error->{
                        binding.progressBarRegFrag.isVisible=false
                        display(it.msg?: "Something wrong, idk what", true)
                    }

                    is NetworkResult.Success->{
                        binding.progressBarRegFrag.isVisible=false

                        tokenManager.saveToken(it.data!!.token)

                        findNavController().navigate(R.id.action_registrationFragment_to_mainFragment)
                    }

                    is NetworkResult.Loading->{
                        binding.progressBarRegFrag.isVisible=true
                    }
                }
            }
        }
    }




    fun Credentials_Entered_By_User(): UserLoginData {
        val username = binding.etUsername.text.toString()
        val email = binding.etRegEmail.text.toString()
        val password = binding.etRegPassword.text.toString()
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