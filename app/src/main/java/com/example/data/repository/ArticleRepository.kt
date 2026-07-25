package com.example.data.repository

import com.example.data.local.dao.BookmarkDao
import com.example.data.local.entity.BookmarkEntity
import com.example.domain.model.Article
import com.example.domain.model.LawPasal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ArticleRepository(private val bookmarkDao: BookmarkDao) {

    private val articlesList = listOf(
        Article(
            id = "art_1",
            title = "Cara Melaporkan Penipuan Online & Membekukan Rekening Pelaku",
            category = "Pidana",
            date = "24 Juli 2026",
            readTimeMinutes = 4,
            summary = "Panduan lengkap prosedur pengaduan ke pihak kepolisian, pembuatan Laporan Polisi, dan pemblokiran akun bank penipu.",
            contentHtml = """
                <h3>1. Kumpulkan Seluruh Bukti Transaksi</h3>
                <p>Simpan bukti transfer bank, tangkapan layar (screenshot) percakapan WhatsApp/Sosmed, akun rekening penipu, serta tautan profil toko online terkait.</p>
                
                <h3>2. Laporkan Rekening ke CekRekening.id</h3>
                <p>Kunjungi situs resmi Kemenkominfo (cekrekening.id) untuk mendaftarkan aduan penipuan rekening bank agar sistem menandai rekening penipu.</p>
                
                <h3>3. Buat Laporan Polisi di Kantor Polres Terdekat</h3>
                <p>Datangi unit SPKT (Sentra Pelayanan Kepolisian Terpadu). Jelaskan kronologi kejadian dan minta diterbitkan <b>Surat Tanda Penerimaan Laporan (STPL)</b>.</p>
                
                <h3>4. Permohonan Pembekuan ke Bank</h3>
                <p>Bawa STPL dari kepolisian beserta KTP ke cabang bank penerbit rekening Anda untuk mengajukan surat permohonan pemblokiran rekening tujuan penipu.</p>
            """.trimIndent()
        ),
        Article(
            id = "art_2",
            title = "Hak Pekerja Kontrak (PKWT) Menurut UU Cipta Kerja & PP 35/2021",
            category = "Ketenagakerjaan",
            date = "22 Juli 2026",
            readTimeMinutes = 5,
            summary = "Penjelasan mengenai perhitungan uang kompensasi berakhirnya kontrak kerja dan hak-hak dasar karyawan.",
            contentHtml = """
                <h3>Perhitungan Uang Kompensasi PKWT</h3>
                <p>Setiap pekerja kontrak yang telah memiliki masa kerja minimal 1 bulan secara terus menerus berhak mendapatkan Uang Kompensasi saat masa kontrak berakhir.</p>
                <p><b>Rumus Kompensasi:</b> (Masa Kerja dalam Bulan / 12) x 1 Bulan Upah Pokok.</p>
            """.trimIndent()
        ),
        Article(
            id = "art_3",
            title = "Panduan Prosedur Perceraian di Pengadilan Agama & Pengadilan Negeri",
            category = "Keluarga",
            date = "18 Juli 2026",
            readTimeMinutes = 6,
            summary = "Syarat administratif, tahapan mediasi wajib, hingga penetapan hak asuh anak serta pembagian harta bersama.",
            contentHtml = """
                <h3>Persyaratan Berkas</h3>
                <ul>
                    <li>Buku Nikah / Akta Perkawinan Asli</li>
                    <li>KTP Penggugat/Pemohon</li>
                    <li>Akta Kelahiran Anak (jika menuntut hak asuh)</li>
                    <li>Surat Gugatan/Permohonan Tertulis</li>
                </ul>
            """.trimIndent()
        ),
        Article(
            id = "art_4",
            title = "Memahami Asas Perjanjian & Wanprestasi dalam Hukum Perdata",
            category = "Perdata",
            date = "15 Juli 2026",
            readTimeMinutes = 5,
            summary = "Apa yang dimaksud dengan ingkar janji (wanprestasi), Somasi pertama & kedua, serta tuntutan ganti rugi.",
            contentHtml = """
                <h3>Bentuk-bentuk Wanprestasi (Pasal 1243 KUHPerdata)</h3>
                <p>1. Tidak melaksanakan apa yang diperjanjikan.</p>
                <p>2. Melaksanakan yang diperjanjikan tetapi tidak sebagaimana mestinya.</p>
                <p>3. Melaksanakan yang diperjanjikan tetapi terlambat.</p>
                <p>4. Melakukan sesuatu yang menurut perjanjian tidak boleh dilakukan.</p>
            """.trimIndent()
        )
    )

    private val lawsList = listOf(
        LawPasal(
            id = "law_362",
            lawName = "KUHP (Kitab Undang-Undang Hukum Pidana)",
            pasalNumber = "Pasal 362",
            title = "Pencurian",
            content = "Barang siapa mengambil barang sesuatu, yang seluruhnya atau sebagian kepunyaan orang lain, dengan maksud untuk dimiliki secara melawan hukum, diancam karena pencurian, dengan pidana penjara paling lama lima tahun atau pidana denda paling banyak sembilan ratus rupiah.",
            penaltyDescription = "Pidana Penjara Maks. 5 Tahun"
        ),
        LawPasal(
            id = "law_378",
            lawName = "KUHP (Kitab Undang-Undang Hukum Pidana)",
            pasalNumber = "Pasal 378",
            title = "Penipuan",
            content = "Barang siapa dengan maksud untuk menguntungkan diri sendiri atau orang lain secara melawan hukum, dengan memakai nama palsu atau martabat palsu, dengan tipu muslihat, ataupun rangkaian kebohongan, menggerakkan orang lain untuk menyerahkan barang sesuatu kepadanya, diancam karena penipuan dengan pidana penjara paling lama empat tahun.",
            penaltyDescription = "Pidana Penjara Maks. 4 Tahun"
        ),
        LawPasal(
            id = "law_1320",
            lawName = "KUHPerdata (Kitab Undang-Undang Hukum Perdata)",
            pasalNumber = "Pasal 1320",
            title = "Syarat Sah Perjanjian",
            content = "Supaya terjadi persetujuan yang sah, diperlukan empat syarat:\n1. Kesepakatan mereka yang mengikatkan dirinya;\n2. Kecakapan untuk membuat suatu perikatan;\n3. Suatu pokok persoalan tertentu;\n4. Suatu sebab yang tidak terlarang.",
            penaltyDescription = "Perjanjian Batal / Dapat Dibatalkan demi hukum"
        ),
        LawPasal(
            id = "law_156",
            lawName = "UU No. 13/2003 jo. PP 35/2021 Ketenagakerjaan",
            pasalNumber = "Pasal 156",
            title = "Uang Pesangon & Uang Penghargaan Masa Kerja",
            content = "Dalam hal pengusaha melakukan pemutusan hubungan kerja (PHK), pengusaha diwajibkan membayar uang pesangon dan/atau uang penghargaan masa kerja dan uang penggantian hak yang seharusnya diterima.",
            penaltyDescription = "Wajib dibayarkan oleh Pengusaha"
        )
    )

    fun getArticles(category: String? = null, searchQuery: String? = null): List<Article> {
        return articlesList.filter { article ->
            val matchCategory = category.isNullOrBlank() || category == "Semua" || article.category.equals(category, ignoreCase = true)
            val matchQuery = searchQuery.isNullOrBlank() || article.title.contains(searchQuery, ignoreCase = true) || article.summary.contains(searchQuery, ignoreCase = true)
            matchCategory && matchQuery
        }
    }

    fun getArticleById(id: String): Article? {
        return articlesList.find { it.id == id }
    }

    fun getLaws(searchQuery: String? = null): List<LawPasal> {
        if (searchQuery.isNullOrBlank()) return lawsList
        return lawsList.filter { law ->
            law.pasalNumber.contains(searchQuery, ignoreCase = true) ||
                    law.title.contains(searchQuery, ignoreCase = true) ||
                    law.lawName.contains(searchQuery, ignoreCase = true) ||
                    law.content.contains(searchQuery, ignoreCase = true)
        }
    }

    fun isBookmarked(articleId: String): Flow<Boolean> = bookmarkDao.isBookmarked(articleId)

    suspend fun toggleBookmark(article: Article) {
        val bookmark = BookmarkEntity(
            articleId = article.id,
            title = article.title,
            category = article.category
        )
        bookmarkDao.insertBookmark(bookmark)
    }

    suspend fun removeBookmark(articleId: String) {
        bookmarkDao.deleteBookmarkById(articleId)
    }

    fun getBookmarkedArticles(): Flow<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks()
}
