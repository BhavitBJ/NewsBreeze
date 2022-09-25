package com.bhavitbj.newsbreeze.adapters

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import android.text.Html
import android.view.View
import android.widget.Toast
import com.bhavitbj.newsbreeze.model.Article
import com.bhavitbj.newsbreeze.database.DatabaseHelper
import com.bhavitbj.newsbreeze.utils.Konstant
import com.bhavitbj.newsbreeze.R
import com.bhavitbj.newsbreeze.databinding.NewsDataCardBinding
import com.bhavitbj.newsbreeze.ui.DetailActivity
import org.jetbrains.annotations.NotNull

class NewsAdapter(private val context: Context, articleList: List<Article>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    var articleList: List<Article>
    var databaseHelper: DatabaseHelper? = null

    init {
        this.articleList = articleList
    }

    @NotNull
    override fun onCreateViewHolder(@NotNull parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NewsDataCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(@NotNull holder: ViewHolder, position: Int) {
        databaseHelper = DatabaseHelper(context)
        val article: Article = articleList[position]

        if (article.urlToImage != null) {
            Glide.with(context).load(article.urlToImage).into(holder.binding.image)
        }
        if (article.title != null) {
            holder.binding.title.setText(article.title)
        }
        if (article.description != null) {
            holder.binding.description.setText(Html.fromHtml(article.description))
        }

        val str: Array<String> = article.publishedAt!!.split("T").toTypedArray()
        holder.binding.date.setText(str[0])
        holder.binding.newsCardView.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(Konstant.KEY_IMAGE_URL, article.urlToImage)
            intent.putExtra(Konstant.KEY_TITLE, article.title)
            intent.putExtra(Konstant.KEY_SOURCE_NAME, article.source?.name)
            intent.putExtra(Konstant.KEY_AUTHOR, article.author)
            intent.putExtra(Konstant.KEY_DESCRIPTION, article.description)
            intent.putExtra(Konstant.KEY_CONTENT_URL, article.url)
            intent.putExtra(Konstant.KEY_CONTENT, article.content)
            intent.putExtra(Konstant.KEY_DATE, article.publishedAt)
            context.startActivity(intent)
        })
        holder.binding.readBtn.setOnClickListener { v ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(Konstant.KEY_IMAGE_URL, article.urlToImage)
            intent.putExtra(Konstant.KEY_TITLE, article.title)
            intent.putExtra(Konstant.KEY_SOURCE_NAME, article.source?.name)
            intent.putExtra(Konstant.KEY_AUTHOR, article.author)
            intent.putExtra(Konstant.KEY_DESCRIPTION, article.description)
            intent.putExtra(Konstant.KEY_CONTENT_URL, article.url)
            intent.putExtra(Konstant.KEY_CONTENT, article.content)
            intent.putExtra(Konstant.KEY_DATE, article.publishedAt)
            context.startActivity(intent)
        }
        holder.binding.saveBtn.setOnClickListener { v ->
            bookmarkSelector(
                article.title!!,
                article.urlToImage!!,
                article.description!!,
                article.author!!,
                article.content!!,
                article.url!!,
                article.publishedAt!!,
                holder
            )
        }
        holder.binding.bookmarkBtn.setOnClickListener { v ->
            bookmarkSelector(
                article.title!!,
                article.urlToImage!!,
                article.description!!,
                article.author!!,
                article.content!!,
                article.url!!,
                article.publishedAt!!,
                holder
            )
        }


        if (databaseHelper!!.getSpecificItemsID(article.publishedAt!!)) {
            holder.binding.bookmarkBtn.setCardBackgroundColor(context.resources.getColor(R.color.green))
        }
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    inner class ViewHolder(binding: NewsDataCardBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: NewsDataCardBinding

        init {
            this.binding = binding
        }
    }

    private fun bookmarkSelector(
        title: String,
        image_url: String,
        description: String,
        author: String,
        content: String,
        url: String,
        date: String,
        holder: ViewHolder
    ) {
        if (databaseHelper!!.getSpecificItemsID(date)) {
            DeleteDataFromDB(date, holder)
        } else {

            AddDataToDB(
                title,
                image_url,
                description,
                author,
                content,
                url,
                date, holder
            )
        }
    }

    private fun DeleteDataFromDB(date: String, holder: ViewHolder) {
        databaseHelper?.deleteDataFromDB(date)
        holder.binding.bookmarkBtn.setCardBackgroundColor(context.resources.getColor(R.color.deep_grey))
        notifyDataSetChanged()
        Toast.makeText(context, "Unsaved", Toast.LENGTH_SHORT).show()
    }

    private fun AddDataToDB(
        title: String,
        image_url: String,
        description: String,
        author: String,
        content: String,
        url: String,
        date: String, holder: ViewHolder
    ) {
        val insertData: Boolean = databaseHelper!!.addData(
            title, image_url,
            description, author, content, url, date
        )
        if (insertData) {
            holder.binding.bookmarkBtn.setCardBackgroundColor(context.resources.getColor(R.color.green))
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
            notifyDataSetChanged()
        } else {
            Toast.makeText(context, "unknown error occurred", Toast.LENGTH_SHORT).show()
        }
    }
}