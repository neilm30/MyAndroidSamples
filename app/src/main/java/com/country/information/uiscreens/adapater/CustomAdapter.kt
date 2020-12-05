package com.country.information.uiscreens.adapater

import CountryDetailsDiffCallback
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.country.information.R
import com.country.information.common.RowResponse
import com.country.information.extensions.shareImageByUrl
import com.country.information.networking.model.response.Rows
import com.country.information.utils.EspressoIdlingResource
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_recyclerview.view.*


class CustomAdapter : RecyclerView.Adapter<CustomAdapter.CountryViewHolder>() {

    private var countryInformationList = mutableListOf<RowResponse>()

    /*
     add new items to the list
     @ para
     **/
    fun updateCountryListItems(rowObjects: List<RowResponse> = emptyList()) {
        val diffResult = DiffUtil.calculateDiff(
            CountryDetailsDiffCallback(
                this.countryInformationList,
                rowObjects
            )
        )
        clearCountryListItems()
        EspressoIdlingResource.increment()
        countryInformationList.addAll(rowObjects)
        diffResult.dispatchUpdatesTo(this)
        EspressoIdlingResource.decrement()
    }

    //clear the list
    fun clearCountryListItems() = countryInformationList.clear()

    class CountryViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindItems(rows: RowResponse) {
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
                setIconFromImageUrl(imageUrl, containerView.thumbnailImage)
            }
        }

        private fun setIconFromImageUrl(
            userImageUrl: String? = null,
            thumbnailImage: ImageView
        ) {
            userImageUrl?.let {
                itemView.context.shareImageByUrl(it, thumbnailImage)
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

    override fun getItemCount() = countryInformationList.size

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) =
        holder.bindItems(countryInformationList[position])
}