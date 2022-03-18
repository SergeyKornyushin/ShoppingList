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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
        val intent = Intent().apply {
            putExtra(NoteFragment.NEW_NOTE_KEY, createNewNote())
        }
        setResult(RESULT_OK, intent)
        finish()
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