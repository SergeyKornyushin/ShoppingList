package com.example.shoppinglist.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.example.shoppinglist.R
import com.example.shoppinglist.activities.MainApp
import com.example.shoppinglist.activities.NewNoteActivity
import com.example.shoppinglist.databinding.FragmentNoteBinding
import com.example.shoppinglist.entities.NoteItem
import com.example.shoppinglist.rv_adapter.NoteAdapter
import com.example.shoppinglist.view_models.MainViewModel

class NoteFragment : BaseFragment(), NoteAdapter.Listener {
    private lateinit var binding: FragmentNoteBinding
    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onEditResult()
//        mainViewModel.allNotes.observe(this) {
//            it
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvAdapter = NoteAdapter(this)
        binding.rvNoteFragment.adapter = rvAdapter
        mainViewModel.allNotes.observe(viewLifecycleOwner) {
            rvAdapter.submitList(it)
        }
    }

    override fun onClickNew() {
        editLauncher.launch(Intent(activity, NewNoteActivity::class.java))
    }

    companion object {
        const val NEW_NOTE_KEY = "title_key"
        @JvmStatic
        fun newInstance() = NoteFragment()
    }

    private fun onEditResult(){
        editLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                mainViewModel.insertNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem)
                Log.i("test4", "onEditResult: ${it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem}")
            }
        }
    }

    override fun deleteItem(id: Int) {
        mainViewModel.deleteNote(id)
    }
}