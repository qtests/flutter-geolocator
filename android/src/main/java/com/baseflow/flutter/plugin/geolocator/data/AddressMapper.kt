package com.baseflow.flutter.plugin.geolocator.data

import android.location.Address
import java.util.*

object AddressMapper {

    fun toHashMapList(addresses: List<Address>): List<Map<String, Any>> {
        val hashMaps = ArrayList<Map<String, Any>>(addresses.size)

        for (address in addresses) {
            val hashMap = AddressMapper.toHashMap(address)
            hashMaps.add(hashMap)
        }

        return hashMaps
    }

    private fun toHashMap(address: Address): Map<String, Any> {
        val placemark = HashMap<String, Any>()

        placemark["name"] = address.featureName
        placemark["isoCountryCode"] = address.countryCode
        placemark["country"] = address.countryName
        placemark["thoroughfare"] = address.thoroughfare
        placemark["subThoroughfare"] = address.subThoroughfare
        placemark["postalCode"] = address.postalCode
        placemark["administrativeArea"] = address.adminArea
        placemark["subAdministrativeArea"] = address.subAdminArea
        placemark["locality"] = address.locality
        placemark["subLocality"] = address.subLocality

        if (address.hasLatitude() && address.hasLongitude()) {
            val locationMap = HashMap<String, Double>()

            locationMap["latitude"] = address.latitude
            locationMap["longitude"] = address.longitude

            placemark["location"] = locationMap
        }

        return placemark
    }
}
