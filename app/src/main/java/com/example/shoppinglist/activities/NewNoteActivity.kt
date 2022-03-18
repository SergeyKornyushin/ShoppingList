package com.example.shoppinglist.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityNewNoteBinding
import com.example.shoppinglist.entities.NoteItem
import com.example.shoppinglist.fragments.NoteFragment
import java.text.SimpleDateFormat
import java.util.*

//todo refacor
class NewNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewNoteBinding
    private var noteItem: NoteItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getNote()
    }

    private fun getNote() = with(binding) {
        if (intent.hasExtra(NoteFragment.NEW_NOTE_KEY)) {
            noteItem = intent.getSerializableExtra(NoteFragment.NEW_NOTE_KEY) as NoteItem
            if (noteItem != null) {
                etTitle.setText(noteItem?.title)
                etDescription.setText(noteItem?.content)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.note_menu_save) {
            setMainResult()
        } else if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setMainResult() {
        val editState: String
        val tempNote = if (noteItem == null) {
            editState = NoteFragment.CREATE_NOTE_KEY
            createNewNote()
        } else {
            editState = NoteFragment.UPDATE_NOTE_KEY
            updateNote()
        }
        setResult(RESULT_OK, Intent().apply {
            putExtra(NoteFragment.NEW_NOTE_KEY, tempNote)
            putExtra(NoteFragment.EDIT_STATE_KEY, editState)
        })
        finish()
    }

    private fun updateNote(): NoteItem? = with(binding) {
        noteItem?.copy(
            title = etTitle.text.toString(),
            content = etDescription.text.toString()
        )
    }

    private fun createNewNote(): NoteItem =
        NoteItem(
            null,
            binding.etTitle.text.toString(),
            binding.etDescription.text.toString(),
            getCurrentTime(), ""
        )

    private fun getCurrentTime(): String =
        SimpleDateFormat("HH:mm - dd.MM.yy", Locale.getDefault())
            .format(Calendar.getInstance().time)
}