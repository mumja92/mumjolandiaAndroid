package com.mumjolandia.android.ui.notepad

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.android.R

import android.content.SharedPreferences


class NotepadFragment : Fragment() {

    private var editTextNotepadText: EditText? = null
    private val preferencesString = "note_preferences_"
    private val minimumNoteRows = 30
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notepad, container, false)

        sharedPreferences = requireActivity().applicationContext.getSharedPreferences(
            preferencesString,
            Context.MODE_PRIVATE,
        )

        editTextNotepadText = root?.findViewById(R.id.editTextNotepadText)
        editTextNotepadText?.setText(getNote())
        val buttonClear = root.findViewById<Button>(R.id.buttonNotepadClear)
        buttonClear.setOnClickListener {
            saveNote(null)
            editTextNotepadText?.setText(getNote())
        }

        val buttonSave = root.findViewById<Button>(R.id.buttonNotepadSave)
        buttonSave.setOnClickListener {
            saveNote(editTextNotepadText?.text.toString())
            editTextNotepadText?.setText(getNote())
        }

        return root
    }

    override fun onPause() {
        super.onPause()
        saveNote(editTextNotepadText?.text.toString())
    }

    private fun getNote(): String{
        var note = sharedPreferences?.getString(preferencesString, null) ?: ""
        while (note.lines().size < minimumNoteRows){
            note+="\n"
        }
        return note
    }

    private fun saveNote(note: String?){
        with (sharedPreferences?.edit()) {
            if (note == null){
                this?.remove(preferencesString)
            }
            else{
                this?.putString(preferencesString, note)
            }
            this?.apply()
        }
    }
}