package com.example.a15_notes_app.ui.views

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.a15_notes_app.R
import com.example.a15_notes_app.data.model.NotesItem
import com.example.a15_notes_app.databinding.SingleNotesItemBinding


class NotesAdapter(private val notesOnClickInterface:NotesOnClickInterface) : ListAdapter<NotesItem, NotesAdapter.NotesViewHolder>(DiffUtil())
{

    inner class NotesViewHolder(val binding: SingleNotesItemBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(note: NotesItem)
        {
            binding.tvTitle.text = note.title
            binding.tvDesc.text= note.description

            binding.root.setOnClickListener{
                notesOnClickInterface.noteClickResponse(note)
            }

 //           binding.root.setOnLongClickListener {
//                var marginMultiplier=10
//                val paramsTitle = binding.tvTitle.layoutParams as ViewGroup.MarginLayoutParams
//                val paramsDesc = binding.tvDesc.layoutParams as ViewGroup.MarginLayoutParams
//
//                paramsTitle.marginEnd = paramsTitle.marginEnd * marginMultiplier
//                paramsDesc.marginEnd = paramsDesc.marginEnd * marginMultiplier
//                binding.btnDelMainFrag.visibility = View.VISIBLE
//            }

//            binding.root.setOnLongClickListener{
///*
//                binding.root.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.White))
//*/
////                binding.root.setBackgroundColor(Color.GREEN)
//                this.itemView.setBackgroundColor(Color.DKGRAY)
//            }

            binding.root.setOnLongClickListener{

                binding.root.setBackgroundColor(Color.WHITE)
                notesOnClickInterface.onLongPressResponse(note)
                true
            }

            binding.btnDelMainFrag.setOnClickListener{
                notesOnClickInterface.onLongPressResponse(note)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        var binding= SingleNotesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<NotesItem>()
    {
        override fun areItemsTheSame(oldItem: NotesItem, newItem: NotesItem): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: NotesItem, newItem: NotesItem): Boolean {
            return oldItem == newItem
        }
    }


}
