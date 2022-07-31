package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.NeoStatus
import com.udacity.asteroidradar.main.PhotoLinearAdapter

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("imgOfDay")
fun bindMainImgOfDay(imageView: ImageView, imgObj: PictureOfDay?) {
    imgObj?.let {
        if (imgObj.mediaType == "image") {
            Picasso
                .get()
                .load(imgObj.url)
                .placeholder(R.drawable.placeholder_picture_of_day)
                .error(R.drawable.ic_broken_image)
                .into(imageView)
        }
    }
}

@BindingAdapter("listdata")
fun bindRecyclerView(recyclerView: RecyclerView, data: ArrayList<Asteroid>?) {
    val adapter = recyclerView.adapter as PhotoLinearAdapter
    adapter.submitList(data)
}

@BindingAdapter("neoApiStatus")
fun bindStatus(statusImageView: ImageView, status: NeoStatus?) {
    when (status) {
        NeoStatus.NOT_STARTED -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        NeoStatus.FAILURE -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        NeoStatus.SUCCESS -> {
            statusImageView.visibility = View.GONE
        }
        else -> {}
    }
}
