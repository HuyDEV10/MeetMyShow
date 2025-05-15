package com.materialsouk.meetmyshow

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.materialsouk.meetmyshow.adapters.CinemaAdapter
import com.materialsouk.meetmyshow.databinding.ActivityCinemaBinding
import com.materialsouk.meetmyshow.models.CinemaModel

class CinemaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityCinemaBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_cinema)
        setSupportActionBar(binding.toolbar)

        val cinemaList: ArrayList<CinemaModel> = arrayListOf(
            // CGV
            CinemaModel("CGV Crescent Mall", "TP. Hồ Chí Minh", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/cgv.png"),
            CinemaModel("CGV Vincom Center Landmark 81", "TP. Hồ Chí Minh", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/cgv.png"),
            CinemaModel("CGV Aeon Mall Bình Tân", "TP. Hồ Chí Minh", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/cgv.png"),
            CinemaModel("CGV Aeon Mall Long Biên", "Hà Nội", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/cgv.png"),
            CinemaModel("CGV Vincom Bà Triệu", "Hà Nội", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/cgv.png"),
            CinemaModel("CGV Vincom Nguyễn Chí Thanh", "Hà Nội", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/cgv.png"),
            CinemaModel("CGV Vincom Đà Nẵng", "Đà Nẵng", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/cgv.png"),
            CinemaModel("CGV Vincom Trà Vinh", "Trà Vinh", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/cgv.png"),
            CinemaModel("CGV Vincom Rạch Giá", "Kiên Giang", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/cgv.png"),
            CinemaModel("CGV Vincom Vĩnh Long", "Vĩnh Long", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/cgv.png"),
            CinemaModel("CGV Aeon Mall Tân Phú", "TP. Hồ Chí Minh", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/cgv.png"),
            CinemaModel("CGV GigaMall", "TP. Hồ Chí Minh", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/cgv.png"),
            CinemaModel("CGV Parkson Hùng Vương", "TP. Hồ Chí Minh", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/cgv.png"),
            CinemaModel("CGV Vincom Metropolis", "Hà Nội", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/cgv.png"),
            CinemaModel("CGV Hồ Gươm Plaza", "Hà Nội", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/cgv.png"),
            CinemaModel("CGV Vincom Ocean Park", "Hà Nội", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/cgv.png"),

            // Lotte
            CinemaModel("Lotte Cinema Cantavil", "TP. Hồ Chí Minh", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/lotte.jpg"),
            CinemaModel("Lotte Cinema Gò Vấp", "TP. Hồ Chí Minh", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/lotte.jpg"),
            CinemaModel("Lotte Cinema Cần Thơ", "Cần Thơ", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/lotte.jpg"),
            CinemaModel("Lotte Cinema Nha Trang", "Khánh Hòa", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/lotte.jpg"),
            CinemaModel("Lotte Cinema Hà Đông", "Hà Nội", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/lotte.jpg"),
            CinemaModel("Lotte Cinema Nam Sài Gòn", "TP. Hồ Chí Minh", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/lotte.jpg"),

            // Beta
            CinemaModel("Beta Mỹ Đình", "Hà Nội", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/beta.jpg"),
            CinemaModel("Beta Thanh Xuân", "Hà Nội", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/beta.jpg"),
            CinemaModel("Beta Long Biên", "Hà Nội", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/beta.jpg"),
            CinemaModel("Beta Biên Hòa", "Đồng Nai", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/beta.jpg"),
            CinemaModel("Beta Thái Nguyên", "Thái Nguyên", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/beta.jpg"),
            CinemaModel("Beta Bắc Giang", "Bắc Giang", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/beta.jpg"),
            CinemaModel("Beta Thanh Hóa", "Thanh Hóa", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/beta.jpg"),
            CinemaModel("Beta Nha Trang", "Khánh Hòa", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/beta.jpg"),
            CinemaModel("Beta Huế", "Thừa Thiên Huế", "https://pub-cd617de5e74b498ab4b882710c47f9b0.r2.dev/beta.jpg")
        )

        // Lấy dữ liệu từ Intent
        val movie_name = intent.getStringExtra("movie_name")
        val banner_image_url = intent.getStringExtra("banner_image_url")
        val movieId = intent.getStringExtra("movieId")

        // Kiểm tra dữ liệu từ Intent
        if (movie_name == null || banner_image_url == null || movieId == null) {
            Toast.makeText(this, "Lỗi: Dữ liệu không đầy đủ từ Intent", Toast.LENGTH_SHORT).show()
            finish() // Kết thúc Activity nếu thiếu dữ liệu
            return
        }

        // Lấy dữ liệu vị trí từ SharedPreferences
        val sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        val selectedCityList: ArrayList<CinemaModel> = ArrayList()
        val locationStr = sharedPreferences.getString("location_str", null)

        if (!locationStr.isNullOrEmpty()) {
            val lstValues: List<String> = locationStr.split(",").map { it.trim() }
            lstValues.forEach { city ->
                cinemaList.forEach { element ->
                    if (element.cinemaLocation == city) {
                        selectedCityList.add(CinemaModel(element.cinemaName, element.cinemaLocation, element.cinemaDrawable))
                    }
                }
            }
        }

        // Nếu có danh sách thành phố đã chọn, dùng danh sách đó, nếu không thì dùng toàn bộ danh sách
        val cinemaAdapter = CinemaAdapter(
            this,
            if (selectedCityList.isNotEmpty()) selectedCityList else cinemaList,
            movie_name,
            banner_image_url,
            movieId
        )
        binding.movieRecyclerView.adapter = cinemaAdapter
    }
}
