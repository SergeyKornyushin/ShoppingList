package com.example.shoppinglist.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityNewNoteBinding
import com.example.shoppinglist.entities.NoteItem
import com.example.shoppinglist.fragments.NoteFragment
import com.example.shoppinglist.utils.HtmlManager
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
                etDescription.setText(HtmlManager.getFromHtml(noteItem?.content!!).trim())
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
        } else if (item.itemId == R.id.note_menu_bold) {
            setSelectedTextBold()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setSelectedTextBold() = with(binding) {
        val startPos = etDescription.selectionStart
        val endPos = etDescription.selectionEnd

        val styles = etDescription.text.getSpans(startPos, endPos, StyleSpan::class.java)

        var boldStyle: StyleSpan? = null
        if (styles.isNotEmpty()){
            etDescription.text.removeSpan(styles[0])
        } else {
            boldStyle = StyleSpan(Typeface.BOLD)
        }

        etDescription.text.setSpan(boldStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        etDescription.text.trim()
        etDescription.setSelection(startPos)
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
            content = HtmlManager.toHtml(etDescription.text)
        )
    }

    private fun createNewNote(): NoteItem =
        NoteItem(
            null,
            binding.etTitle.text.toString(),
            HtmlManager.toHtml(binding.etDescription.text),
            getCurrentTime(), ""
        )

    private fun getCurrentTime(): String =
        SimpleDateFormat("HH:mm - dd.MM.yy", Locale.getDefault())
            .format(Calendar.getInstance().time)
}