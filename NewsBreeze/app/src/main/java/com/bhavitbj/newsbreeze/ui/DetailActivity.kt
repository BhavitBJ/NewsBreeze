package com.bhavitbj.newsbreeze.ui

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bhavitbj.newsbreeze.database.DatabaseHelper
import com.bhavitbj.newsbreeze.utils.Konstant
import com.bhavitbj.newsbreeze.R
import com.bhavitbj.newsbreeze.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    var binding: ActivityDetailBinding? = null
    var databaseHelper: DatabaseHelper? = null
    private var isSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(getLayoutInflater())

        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        setContentView(binding!!.getRoot())

        initView()
    }


    private fun initView() {
        databaseHelper = DatabaseHelper(this)
        binding?.backBtn?.setOnClickListener { v -> finish() }


        if (getIntent().getStringExtra(Konstant.KEY_IMAGE_URL) != null) {
            Glide.with(this).load(getIntent().getStringExtra(Konstant.KEY_IMAGE_URL)).into(binding!!.image)
        }
        if (getIntent().getStringExtra(Konstant.KEY_TITLE) != null) {
            binding?.title?.setText(getIntent().getStringExtra(Konstant.KEY_TITLE))
        }
        if (getIntent().getStringExtra(Konstant.KEY_AUTHOR) != null) {
            binding?.author?.setText(getIntent().getStringExtra(Konstant.KEY_AUTHOR))
        }
        if (getIntent().getStringExtra(Konstant.KEY_CONTENT) != null) {
            binding?.content?.setText(getIntent().getStringExtra(Konstant.KEY_CONTENT))
        }


        val str: Array<String> = intent.getStringExtra(Konstant.KEY_DATE)?.split("T")!!.toTypedArray()
        binding?.date?.setText(str[0])
        if (databaseHelper!!.getSpecificItemsID(getIntent().getStringExtra(Konstant.KEY_DATE)!!)) {
            isSelected = true
            binding?.bookmarkBtn?.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_filled))
        }
        binding?.bookmarkBtn?.setOnClickListener { v ->
            if (isSelected) {
                databaseHelper!!.deleteDataFromDB(getIntent().getStringExtra(Konstant.KEY_DATE)!!)
                binding!!.bookmarkBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_unfilled))
            } else {
                AddDataToDB(
                    getIntent().getStringExtra(Konstant.KEY_TITLE)!!,
                    getIntent().getStringExtra(Konstant.KEY_IMAGE_URL)!!,
                    getIntent().getStringExtra(Konstant.KEY_DESCRIPTION)!!,
                    getIntent().getStringExtra(Konstant.KEY_AUTHOR)!!,
                    getIntent().getStringExtra(Konstant.KEY_CONTENT)!!,
                    getIntent().getStringExtra(Konstant.KEY_CONTENT_URL)!!,
                    getIntent().getStringExtra(Konstant.KEY_DATE)!!
                )
            }
        }
    }

    private fun AddDataToDB(
        title: String,
        image_url: String,
        description: String,
        author: String,
        content: String,
        url: String,
        date: String
    ) {
        val insertData: Boolean = databaseHelper!!.addData(
            title, image_url,
            description, author, content, url, date
        )
        if (insertData) {
            binding?.bookmarkBtn?.setImageDrawable(getResources().getDrawable(R.drawable.ic_bookmark_filled))
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "unknown error occurred", Toast.LENGTH_SHORT).show()
        }
    }


}