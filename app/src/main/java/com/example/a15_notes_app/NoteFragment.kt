package com.example.a15_notes_app

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
import androidx.navigation.fragment.findNavController
import com.example.a15_notes_app.data.model.NetworkResult
import com.example.a15_notes_app.data.model.Note
import com.example.a15_notes_app.data.model.NotesItem
import com.example.a15_notes_app.databinding.FragmentNoteBinding
import com.example.a15_notes_app.ui.ViewModel.NotesViewModel
import com.example.a15_notes_app.utils.Event
import com.google.gson.Gson
import javax.inject.Inject


class NoteFragment : Fragment() {

    lateinit var binding: FragmentNoteBinding
    lateinit var notesViewModel:NotesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        notesViewModel= ViewModelProvider(requireActivity())[NotesViewModel::class.java]

        setInitialValues()
        observer()
    }

    fun observer()
    {
//        notesViewModel.notesLiveData.observe(viewLifecycleOwner)      // We need to
//        {
//            showToast("Yes it is observed XDXD")
//        }

        notesViewModel.statusLiveData.observe(viewLifecycleOwner)
        {event->

            event.getContentIfNotHandled()?.let {
                when(it)
                {
                    is NetworkResult.Success->{
                        binding.progressBarNotesFrag.isVisible=false
                        it.data?.let { it1 -> showToast(it1.second) }
                    }

                    is NetworkResult.Error->{
                        it.msg?.let { it1 -> showToast(it1) }
                        binding.progressBarNotesFrag.isVisible=false
                    }

                    is NetworkResult.Loading->{
                        binding.progressBarNotesFrag.isVisible=true
                    }
                }
            }
        }
    }

    fun listeners(noteItem:NotesItem?)
    {
        binding.include2.btnSaveNote.setOnClickListener{

            var note = extractNoteFromView()
            Log.d("@@@", "Save button pressed!")

            if (noteItem != null) {     // It means noteItem is not null, which means this note already exists
                notesViewModel.updateNote(noteItem._id, note)
            }

            else notesViewModel.createNote(note)
        }

        binding.include2.btnDelNote.setOnClickListener{

            if(noteItem!=null) notesViewModel.deleteNote(noteItem._id)

            else showToast("Note was not created!")
        }

        binding.include2.btnBackToMainFrag.setOnClickListener{
            findNavController().popBackStack()
        }
    }


    fun setInitialValues()
    {
        var jsonNote= arguments?.getString("note")

        var note:NotesItem?= null
        if(jsonNote != null)
        {
            note = Gson().fromJson<NotesItem>(jsonNote, NotesItem::class.java)

            note?.let {
                binding.etNoteTitle.setText(it.title)
                binding.etInsideScrollView.setText(it.description)
                binding.tvDateTime.text = makeItReadable(it.updatedAt)
            }
        }

        listeners(note)     // Now!! The value of "note" which is a NoteItem will be "null" if a new note
                            // is being created, so we will check this in our listener, if this parameter
                        // is null, it means it is a new note and "createNote" fun should be called
                        // else we will extract " _id " from this note to call update ;)
    }






    fun makeItReadable(date:String):String
    {
        var arr = date.split("-")
        val months = listOf("Jan", "Feb", "Mar", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec")
        val date_string = arr[2].substring(0,2)+ " " + months[arr[1].toInt()-1] + " " + arr[0]
        val result = date_string + "  |  " + binding.etInsideScrollView.text.toString().length + " Characters"
        return result
    }

    fun extractNoteFromView(): Note
    {
        val title = binding.etNoteTitle.text.toString()
        val description = binding.etInsideScrollView.text.toString()

        return Note(title, description)
    }

    fun showToast(msg:String)
    {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()

        notesViewModel.statusLiveData.removeObservers(viewLifecycleOwner)
    }
}