package io.justdevit.telegram.flow.model

/**
 * Represents geographical coordinates, including the address, latitude, and longitude.
 *
 * @property address The location's address as a string.
 * @property latitude The latitude of the location, represented as a double.
 * @property longitude The longitude of the location, represented as a double.
 */
data class GeoCoordinates(
    val address: String,
    val latitude: Double,
    val longitude: Double,
)
