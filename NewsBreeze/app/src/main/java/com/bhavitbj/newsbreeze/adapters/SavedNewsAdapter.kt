package com.bhavitbj.newsbreeze.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bhavitbj.newsbreeze.model.Article
import com.bhavitbj.newsbreeze.database.DatabaseHelper
import com.bhavitbj.newsbreeze.utils.Konstant
import com.bhavitbj.newsbreeze.databinding.SavedNewsDataCardBinding
import com.bhavitbj.newsbreeze.ui.DetailActivity
import com.bumptech.glide.Glide

class SavedNewsAdapter(private val context: Context, var articles: List<Article>) :
    RecyclerView.Adapter<SavedNewsAdapter.ViewHolder?>() {
    var databaseHelper: DatabaseHelper? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SavedNewsDataCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        databaseHelper = DatabaseHelper(context)
        val data = articles[position]
        Glide.with(context).load(data.urlToImage).into(holder.binding.image)
        holder.binding.title.setText(data.title)

        val str = data.publishedAt!!.split("T").toTypedArray()
        holder.binding.dateNauthor.setText(str[0] + " . " + data.author)
        holder.binding.savedCV.setOnClickListener { v ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(Konstant.KEY_IMAGE_URL, data.urlToImage)
            intent.putExtra(Konstant.KEY_TITLE, data.title)
            intent.putExtra(Konstant.KEY_AUTHOR, data.author)
            intent.putExtra(Konstant.KEY_DESCRIPTION, data.description)
            intent.putExtra(Konstant.KEY_CONTENT_URL, data.url)
            intent.putExtra(Konstant.KEY_CONTENT, data.content)
            intent.putExtra(Konstant.KEY_DATE, data.publishedAt)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    inner class ViewHolder(binding: SavedNewsDataCardBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: SavedNewsDataCardBinding

        init {
            this.binding = binding
        }
    }
}