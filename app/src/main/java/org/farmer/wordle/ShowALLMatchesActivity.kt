package org.farmer.wordle

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hardgusol.sol.R
import android.content.Intent
import android.text.Editable
import android.widget.EditText
import org.farmer.wordle.Data.allMatchedWords


class ShowALLMatchesActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_all_matches)

        actionBar!!.setDisplayShowTitleEnabled(true)
        actionBar!!.title = "All Matches"

       val editText:EditText  = findViewById<EditText>(R.id.allMatchesTextView)


        editText.setText(allMatchedWords)


    }
}