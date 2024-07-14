package com.example.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.asteroidradar.network_database.database.AsteroidData

@BindingAdapter("asteroidName")
fun TextView.setAsteroidName(item: AsteroidData?) {
    item?.let {
        text = item.name
    }
}

@BindingAdapter("asteroidDate")
fun TextView.setAsteroidDate(item: AsteroidData?) {
    item?.let {
        text = dateMillis2String(item.date, withTime = true)
    }
}

@BindingAdapter("closeApproachDate")
fun TextView.setCloseApproachDate(item: AsteroidData?) {
    item?.let {
        text = dateMillis2String(item.date, withTime = true)
    }
}

@BindingAdapter("absoluteMagnitude")
fun TextView.setAbsoluteMagnitude(item: AsteroidData?) {
    item?.let {
        text = context.resources.getString(R.string.au_unit, item.maxMagnitude)
    }
}

@BindingAdapter("estimatedDiameter")
fun TextView.setEstimatedDiameter(item: AsteroidData?) {
    item?.let {
        text = context.resources.getString(R.string.km_unit, item.diameter)
    }
}

@BindingAdapter("relativeVelocity")
fun TextView.setRelativeVelocity(item: AsteroidData?) {
    item?.let {
        text = context.resources.getString(R.string.kmps_unit, item.velocity)
    }
}

@BindingAdapter("distanceFromEarth")
fun TextView.setDistanceFromEarth(item: AsteroidData?) {
    item?.let {
        text = context.resources.getString(R.string.au_unit, item.distance)
    }
}

@BindingAdapter("hazardousIcon")
fun ImageView.setHazardousIcon(item: AsteroidData?) {
    item?.let {
        setImageResource(when(item.hazardous) {
            true -> R.drawable.ic_status_potentially_hazardous
            else -> R.drawable.ic_status_normal
        })
        contentDescription = when(item.hazardous) {
            true -> context.getString(R.string.hazardous_desc)
            else -> context.getString(R.string.not_hazardous_desc)
        }
    }
}

@BindingAdapter("hazardousImage")
fun ImageView.setHazardousImage(item: AsteroidData?) {
    item?.let {
        setImageResource(when(item.hazardous) {
            true -> R.drawable.asteroid_hazardous
            else -> R.drawable.asteroid_safe
        })
    }
}

@BindingAdapter("apodTitle")
fun ImageView.setApodTitle(item: String?) {
    item?.let {
        contentDescription = if (item != "") {
            item
        } else {
            context.getString(R.string.image_of_the_day)
        }
    }
}

@BindingAdapter("hazardousContentDescription")
fun ImageView.setHazardousContentDescription(item: AsteroidData?) {
    item?.let {
        contentDescription = when(item.hazardous) {
            true -> context.getString(R.string.hazardous_desc)
            else -> context.getString(R.string.not_hazardous_desc)
        }
    }
}