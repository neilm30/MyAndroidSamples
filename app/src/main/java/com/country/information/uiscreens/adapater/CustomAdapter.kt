package com.example.myapplication.adapater

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.country.information.R
import com.country.information.networking.model.response.Rows
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_recyclerview.view.*

class CustomAdapter(
    private val countryInformation: List<Rows>,
) : RecyclerView.Adapter<CustomAdapter.CountryViewHolder>() {

    class CountryViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindItems(rows: Rows) {
            rows.apply {
                // set title
                containerView.txt_title.apply {
                    if (title.isNullOrEmpty().not())
                        text = title
                    else
                        visibility = View.GONE
                }

                // set description
                containerView.txt_details.apply {
                    if (description.isNullOrEmpty().not()) {
                        text = description
                        visibility = View.VISIBLE
                    } else visibility = View.GONE
                }

                // set image icon
                setIconForCountryDetails(imageHref, containerView.thumbnailImage)
            }
        }

        private fun setIconForCountryDetails(
            userImageUrl: String? = null,
            thumbnailImage: ImageView
        ) {

            userImageUrl?.let {
                thumbnailImage.visibility = View.VISIBLE
                Glide.with(itemView.context).load(it).listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        thumbnailImage.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                }).fitCenter().into(thumbnailImage)
            } ?: kotlin.run {
                thumbnailImage.visibility = View.GONE
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false)
        return CountryViewHolder(v)
    }

    override fun getItemCount() = countryInformation.size

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) =
        holder.bindItems(countryInformation[position])

}