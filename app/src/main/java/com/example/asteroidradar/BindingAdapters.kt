package com.example.asteroidradar

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
        text = dateMillis2String(item.date)
    }
}