package com.example.asteroidradar.network_database.network

import com.example.asteroidradar.network_database.database.AsteroidData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import timber.log.Timber

@JsonClass(generateAdapter = true)
data class NeoFeed(
    @Json(name = "links") val links: Links,
    @Json(name = "element_count") val elementCount: Int,
    @Json(name = "near_earth_objects") val nearEarthObjects: Map<String, List<NearEarthObject>>
)

@JsonClass(generateAdapter = true)
data class Links(
    @Json(name = "next") val next: String,
    @Json(name = "previous") val previous: String,
    @Json(name = "self") val self: String
)

@JsonClass(generateAdapter = true)
data class NearEarthObject(
    @Json(name = "links") val links: NeoLinks,
    @Json(name = "id") val id: Long,
    @Json(name = "neo_reference_id") val neoReferenceId: Long,
    @Json(name = "name") val name: String,
    @Json(name = "nasa_jpl_url") val nasaJplUrl: String,
    @Json(name = "absolute_magnitude_h") val absoluteMagnitudeH: Double,
    @Json(name = "estimated_diameter") val estimatedDiameter: EstimatedDiameter,
    @Json(name = "is_potentially_hazardous_asteroid") val isPotentiallyHazardousAsteroid: Boolean,
    @Json(name = "close_approach_data") val closeApproachData: List<CloseApproachData>,
    @Json(name = "is_sentry_object") val isSentryObject: Boolean
)

@JsonClass(generateAdapter = true)
data class NeoLinks(
    @Json(name = "self") val self: String
)

@JsonClass(generateAdapter = true)
data class EstimatedDiameter(
    @Json(name = "kilometers") val kilometers: DiameterRange,
    @Json(name = "meters") val meters: DiameterRange,
    @Json(name = "miles") val miles: DiameterRange,
    @Json(name = "feet") val feet: DiameterRange
)

@JsonClass(generateAdapter = true)
data class DiameterRange(
    @Json(name = "estimated_diameter_min") val estimatedDiameterMin: Double,
    @Json(name = "estimated_diameter_max") val estimatedDiameterMax: Double
)

@JsonClass(generateAdapter = true)
data class CloseApproachData(
    @Json(name = "close_approach_date") val closeApproachDate: String,
    @Json(name = "close_approach_date_full") val closeApproachDateFull: String,
    @Json(name = "epoch_date_close_approach") val epochDateCloseApproach: Long,
    @Json(name = "relative_velocity") val relativeVelocity: RelativeVelocity,
    @Json(name = "miss_distance") val missDistance: MissDistance,
    @Json(name = "orbiting_body") val orbitingBody: String
)

@JsonClass(generateAdapter = true)
data class RelativeVelocity(
    @Json(name = "kilometers_per_second") val kilometersPerSecond: Double,
    @Json(name = "kilometers_per_hour") val kilometersPerHour: Double,
    @Json(name = "miles_per_hour") val milesPerHour: Double
)

@JsonClass(generateAdapter = true)
data class MissDistance(
    @Json(name = "astronomical") val astronomical: Double,
    @Json(name = "lunar") val lunar: Double,
    @Json(name = "kilometers") val kilometers: Double,
    @Json(name = "miles") val miles: Double
)

fun NeoFeed.asteroidsDataList(): Array<AsteroidData> =
    nearEarthObjects.values.flatten().map { asteroid ->
        val approachData = asteroid.closeApproachData.first()
        Timber.d("Approach data: $approachData")
        AsteroidData(
            id = asteroid.id,
            name = asteroid.name,
            date = approachData.epochDateCloseApproach,
            maxMagnitude = asteroid.absoluteMagnitudeH,
            diameter = asteroid.estimatedDiameter.kilometers.estimatedDiameterMax,
            velocity = approachData.relativeVelocity.kilometersPerSecond,
            distance = approachData.missDistance.astronomical,
            hazardous = asteroid.isPotentiallyHazardousAsteroid
        )
    }.toTypedArray()