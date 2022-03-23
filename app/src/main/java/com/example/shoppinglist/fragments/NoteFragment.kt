package com.example.shoppinglist.fragments

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.shoppinglist.activities.MainApp
import com.example.shoppinglist.activities.NewNoteActivity
import com.example.shoppinglist.databinding.FragmentNoteBinding
import com.example.shoppinglist.entities.NoteItem
import com.example.shoppinglist.rv_adapter.NoteAdapter
import com.example.shoppinglist.view_models.MainViewModel

class NoteFragment : BaseFragment(), NoteAdapter.Listener {
    private lateinit var binding: FragmentNoteBinding
    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private lateinit var sharedPreferences: SharedPreferences
    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onEditResult()
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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(view.context)
        val rvAdapter = NoteAdapter(this, sharedPreferences)
        binding.rvNoteFragment.layoutManager = getLayoutManager()
        binding.rvNoteFragment.adapter = rvAdapter
        mainViewModel.allNotes.observe(viewLifecycleOwner) {
            rvAdapter.submitList(it)
        }
    }

    private fun getLayoutManager(): RecyclerView.LayoutManager =
        if (sharedPreferences.getString("note_style_key", "Linear").equals("Linear"))
            LinearLayoutManager(activity)
         else StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)


    override fun onClickNew() {
        editLauncher.launch(Intent(activity, NewNoteActivity::class.java))
    }

    private fun onEditResult(){
        editLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK){
                if (it.data?.getStringExtra(EDIT_STATE_KEY) == CREATE_NOTE_KEY){
                    mainViewModel.insertNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem)
                } else {
                    mainViewModel.updateNote(it.data?.getSerializableExtra(NEW_NOTE_KEY) as NoteItem)
                }
            }
        }
    }

    override fun deleteItem(id: Int) {
        mainViewModel.deleteNote(id)
    }

    override fun onClickItem(noteItem: NoteItem) {
        editLauncher.launch(Intent(activity, NewNoteActivity::class.java).apply {
            putExtra(NEW_NOTE_KEY, noteItem)
        })
    }

    companion object {
        const val NEW_NOTE_KEY = "title_key"
        const val EDIT_STATE_KEY = "edit_state_key"
        const val CREATE_NOTE_KEY = "create_note_key"
        const val UPDATE_NOTE_KEY = "update_note_key"
        @JvmStatic
        fun newInstance() = NoteFragment()
    }
}