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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a15_notes_app.data.model.NetworkResult
import com.example.a15_notes_app.data.model.NotesItem
import com.example.a15_notes_app.databinding.FragmentMainBinding
import com.example.a15_notes_app.ui.ViewModel.AuthViewModel
import com.example.a15_notes_app.ui.ViewModel.NotesViewModel
import com.example.a15_notes_app.ui.views.NotesAdapter
import com.example.a15_notes_app.ui.views.NotesOnClickInterface
import com.example.a15_notes_app.utils.Constants
import com.example.a15_notes_app.utils.Event
import com.example.a15_notes_app.utils.TokenManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment : Fragment(), NotesOnClickInterface {

    lateinit var binding:FragmentMainBinding

    @Inject
    lateinit var tokenManager: TokenManager

    lateinit var viewModel: NotesViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel= ViewModelProvider(requireActivity())[NotesViewModel::class.java]

        viewModel.getNotes()
        observer()
        listeners()
    }

    fun observer()
    {
        viewModel.notesLiveData.observe(viewLifecycleOwner){event->

            event.getContentIfNotHandled()?.let {

                when(it)
                {
                    is NetworkResult.Loading->{
                        binding.progressBarMainFrag.isVisible=true
                    }

                    is NetworkResult.Success->{

                        binding.progressBarMainFrag.isVisible=false
                        var adapter = NotesAdapter(this)

                        adapter.submitList(it.data)

                        binding.rvNotes.adapter= adapter
                        binding.rvNotes.layoutManager= LinearLayoutManager(requireContext())
                    }

                    is NetworkResult.Error->{
                        binding.progressBarMainFrag.isVisible=false
                        Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT)
                    }
                }
            }
        }
    }


    fun listeners()
    {
        binding.fabAddNote.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }

        binding.include.btnLogout.setOnClickListener{
            tokenManager.change_user_signIn_status_to(false)
            Log.d("@@@", "tokenManager.isLoggedIn() :  ${tokenManager.isLoggedIn()}")
            findNavController().navigate(R.id.action_mainFragment_to_registrationFragment)
        }
    }

    override fun noteClickResponse(note: NotesItem) {
        var bundle = Bundle()
        bundle.putString("note", Gson().toJson(note))
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment, bundle)
    }

    override fun onLongPressResponse(note: NotesItem) {
        Toast.makeText(requireContext(), "Long pressed!", Toast.LENGTH_SHORT).show()
    }
}

