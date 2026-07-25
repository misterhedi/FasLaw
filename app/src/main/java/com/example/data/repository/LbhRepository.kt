package com.example.data.repository

import com.example.domain.model.LbhLocation

class LbhRepository {

    private val lbhList = listOf(
        LbhLocation(
            id = "lbh_1",
            name = "LBH Jakarta - Yayasan LBH Indonesia",
            distanceKm = 1.2,
            address = "Jl. Diponegoro No. 74, Menteng, Jakarta Pusat",
            phone = "(021) 3145516",
            operatingHours = "Senin - Jumat (08.30 - 16.30 WIB)",
            latitude = -6.1983,
            longitude = 106.8456,
            isVerifiedPosbakum = true
        ),
        LbhLocation(
            id = "lbh_2",
            name = "Posbakum Pengadilan Negeri Jakarta Pusat",
            distanceKm = 2.8,
            address = "Jl. Bungur Besar Raya No. 24, Kemayoran, Jakarta Pusat",
            phone = "(021) 4244512",
            operatingHours = "Senin - Kamis (09.00 - 15.00 WIB)",
            latitude = -6.1601,
            longitude = 106.8402,
            isVerifiedPosbakum = true
        ),
        LbhLocation(
            id = "lbh_3",
            name = "LBH APik Jakarta (Perlindungan Perempuan & Anak)",
            distanceKm = 4.5,
            address = "Jl. Raya Ciracas No. 19, Ciracas, Jakarta Timur",
            phone = "(021) 87797238",
            operatingHours = "Senin - Jumat (09.00 - 17.00 WIB)",
            latitude = -6.3210,
            longitude = 106.8821,
            isVerifiedPosbakum = true
        ),
        LbhLocation(
            id = "lbh_4",
            name = "LBH Mawar Saron Jakarta",
            distanceKm = 6.1,
            address = "Jl. Sunter Bulak Timur Blok O No. 10, Tanjung Priok",
            phone = "(021) 6511234",
            operatingHours = "Senin - Jumat (08.00 - 16.00 WIB)",
            latitude = -6.1389,
            longitude = 106.8654,
            isVerifiedPosbakum = true
        )
    )

    fun getNearbyLbh(): List<LbhLocation> = lbhList
}
