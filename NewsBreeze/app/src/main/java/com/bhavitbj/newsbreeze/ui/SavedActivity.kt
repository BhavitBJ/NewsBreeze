package com.bhavitbj.newsbreeze.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bhavitbj.newsbreeze.database.DatabaseHelper
import com.bhavitbj.newsbreeze.adapters.SavedNewsAdapter
import com.bhavitbj.newsbreeze.databinding.ActivitySavedBinding
import java.util.*
import kotlin.collections.ArrayList

class SavedActivity : AppCompatActivity() {

    var binding: ActivitySavedBinding? = null
    var databaseHelper: DatabaseHelper? = null


    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBinding.inflate(getLayoutInflater())
        setContentView(binding!!.getRoot())
        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        databaseHelper = DatabaseHelper(this)

        binding?.savedBackButton?.setOnClickListener { v -> finish() }

        binding?.recyclerView?.setLayoutManager(LinearLayoutManager(this))
        binding?.recyclerView?.setHasFixedSize(true)

        displayNotes("")
        binding?.searchBar?.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.getAction() === MotionEvent.ACTION_UP) {
                if (event.getRawX() >= binding!!.searchBar.getRight() - binding!!.searchBar.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    binding!!.searchBar.setText("")
                    displayNotes("")
                }
            }
            false
        }


        binding?.searchBar?.setOnEditorActionListener { v, actionId, event ->
            if (actionId === EditorInfo.IME_ACTION_SEARCH) {
                val filter: String = binding?.searchBar?.getText().toString()
                displayNotes(filter)
            }
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding?.searchBar?.getWindowToken(), 0)
            true
        }
    }

    fun displayNotes(filter: String) {
        val arrayList = ArrayList(databaseHelper!!.articleData)
        if (filter.isEmpty()) {
            binding?.recyclerView?.setAdapter(SavedNewsAdapter(this, arrayList))
        } else {
            for (article in arrayList) {
                if (article.title!!.lowercase(Locale.getDefault()).startsWith(filter)) {
                    binding?.recyclerView?.setAdapter(SavedNewsAdapter(this, listOf(article)))
                }
            }
        }
    }
}