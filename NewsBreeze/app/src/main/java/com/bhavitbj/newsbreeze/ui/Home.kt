package com.bhavitbj.newsbreeze.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bhavitbj.newsbreeze.api.ApiClient
import com.bhavitbj.newsbreeze.model.Article
import com.bhavitbj.newsbreeze.adapters.NewsAdapter
import com.bhavitbj.newsbreeze.model.NewsData
import com.bhavitbj.newsbreeze.databinding.ActivityHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Home : AppCompatActivity() {

    private var binding: ActivityHomeBinding? = null
    private val COUNTRY_CODE = "in"
    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(getLayoutInflater())
        setContentView(binding!!.getRoot())
        LoadData()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun LoadData() {

        recyclerView = binding?.recyclerView
        recyclerView?.setLayoutManager(LinearLayoutManager(this))
        val llm = LinearLayoutManager(this)
        llm.setOrientation(LinearLayoutManager.VERTICAL)
        recyclerView?.setLayoutManager(llm)

        binding?.homeSavedCV?.setOnClickListener { v ->
            startActivity(Intent(this, SavedActivity::class.java))
        }

        getFilteredData("")

        binding?.searchBar?.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.getAction() === MotionEvent.ACTION_UP) {
                if (event.getRawX() >= binding!!.searchBar.getRight() - binding!!.searchBar.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {
                    binding!!.searchBar.setText("")
                    getFilteredData("")
                }
            }
            false
        }


        binding?.searchBar?.setOnEditorActionListener { v, actionId, event ->
            if (actionId === EditorInfo.IME_ACTION_SEARCH) {
                val filter: String = binding?.searchBar?.getText().toString()
                getFilteredData(filter)
            }
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding?.searchBar?.getWindowToken(), 0)
            true
        }
    }


    private fun getFilteredData(filter: String) {
        val call: Call<NewsData?>? = ApiClient.instance
            ?.api
            ?.getNewsData(COUNTRY_CODE, "4c46ecc978d341fc974724f894dfa64c")
        if (call != null) {
            call.enqueue(object : Callback<NewsData?> {
                override fun onResponse(call: Call<NewsData?>?, response: Response<NewsData?>) {
                    if (response.isSuccessful()) {
                        if (filter.isEmpty()) {
                            recyclerView?.setAdapter(
                                NewsAdapter(
                                    this@Home,
                                    response.body()?.articles!!
                                )
                            )
                        } else {
                            val articleList: List<Article> = response.body()?.articles!!
                            for (data in articleList) {
                                if (data.title?.toLowerCase()!!.startsWith(filter)) {
                                    recyclerView?.setAdapter(
                                        NewsAdapter(
                                            this@Home, listOf(data)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<NewsData?>?, t: Throwable?) {
                    Toast.makeText(
                        this@Home,
                        "Unknown error occurred", Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

}