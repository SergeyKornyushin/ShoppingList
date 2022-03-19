package com.example.shoppinglist.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityNewNoteBinding
import com.example.shoppinglist.entities.NoteItem
import com.example.shoppinglist.fragments.NoteFragment
import com.example.shoppinglist.utils.HtmlManager
import com.example.shoppinglist.utils.TouchListener
import java.text.SimpleDateFormat
import java.util.*

//todo refacor
@SuppressLint("ClickableViewAccessibility")
class NewNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewNoteBinding
    private var noteItem: NoteItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getNote()

        binding.llColorPicker.setOnTouchListener(TouchListener())

        onClickColorPicker()
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
        } else if (item.itemId == R.id.note_menu_color_picker) {
            if (binding.llColorPicker.isShown) {
                closeColorPicker()
            } else {
                openColorPicker()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setSelectedTextBold() = with(binding.etDescription) {
        val styles = text.getSpans(selectionStart, selectionEnd, StyleSpan::class.java)
        var boldStyle: StyleSpan? = null
        if (styles.isNotEmpty()) {
            text.removeSpan(styles[0])
        } else {
            boldStyle = StyleSpan(Typeface.BOLD)
        }

        text.setSpan(boldStyle, selectionStart, selectionEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        text.trim()
        setSelection(selectionStart)
    }

    private fun onClickColorPicker() = with(binding){
        picRed.setOnClickListener { setSelectedTextColor(R.color.picker_red) }
        picBlack.setOnClickListener { setSelectedTextColor(R.color.picker_black) }
        picBlue.setOnClickListener { setSelectedTextColor(R.color.picker_blue) }
        picGreen.setOnClickListener { setSelectedTextColor(R.color.picker_green) }
        picYellow.setOnClickListener { setSelectedTextColor(R.color.picker_yellow) }
        picOrange.setOnClickListener { setSelectedTextColor(R.color.picker_orange) }
    }

    private fun setSelectedTextColor(colorId: Int) = with(binding.etDescription) {
        val styles = text.getSpans(selectionStart, selectionEnd, ForegroundColorSpan::class.java)

        if (styles.isNotEmpty()) {
            text.removeSpan(styles[0])
        }

        text.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this@NewNoteActivity, colorId)),
            selectionStart,
            selectionEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text.trim()
        setSelection(selectionStart)
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

    private fun openColorPicker() {
        binding.llColorPicker.visibility = View.VISIBLE
        val openAnim = AnimationUtils.loadAnimation(this, R.anim.open_color_picker)
        binding.llColorPicker.startAnimation(openAnim)
    }

    private fun closeColorPicker() {
        val openAnim = AnimationUtils.loadAnimation(this, R.anim.close_color_picker)
        openAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}
            override fun onAnimationEnd(p0: Animation?) {
                binding.llColorPicker.visibility = View.GONE
            }

            override fun onAnimationRepeat(p0: Animation?) {}
        })
        binding.llColorPicker.startAnimation(openAnim)
    }
}

















