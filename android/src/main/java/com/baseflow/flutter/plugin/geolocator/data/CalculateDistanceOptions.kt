package com.baseflow.flutter.plugin.geolocator.data

class CalculateDistanceOptions(val sourceCoordinates: Coordinate, val destinationCoordinates: Coordinate) {
    companion object {


        fun parseArguments(arguments: Any?): CalculateDistanceOptions {
            val sourceCoordinate: Coordinate
            val destinationCoordinate: Coordinate

            if (arguments == null) {
                throw IllegalArgumentException("No coordinates supplied to calculate distance between.")
            }

            @Suppress("UNCHECKED_CAST")
            val coordinates = arguments as? Map<String, Double>
                ?: throw IllegalArgumentException("No coordinates supplied to calculate distance between.")

            sourceCoordinate = Coordinate(
                coordinates["startLatitude"]!!,
                coordinates["startLongitude"]!!)
            destinationCoordinate = Coordinate(
                coordinates["endLatitude"]!!,
                coordinates["endLongitude"]!!)

            return CalculateDistanceOptions(sourceCoordinate, destinationCoordinate)
        }
    }
}
