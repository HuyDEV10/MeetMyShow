package com.materialsouk.meetmyshow

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import com.materialsouk.meetmyshow.adapters.LocationAdapter
import com.materialsouk.meetmyshow.databinding.ActivityLocationBinding
import com.materialsouk.meetmyshow.models.CitiesModel
import java.util.*


class LocationActivity : AppCompatActivity() {
    lateinit var courseList: ArrayList<CitiesModel>
    lateinit var locationAdapter: LocationAdapter
    lateinit var binding: ActivityLocationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_location)
        setSupportActionBar(binding.toolbar)
        val sharedPreferences = getSharedPreferences(
            getString(R.string.app_name),
            MODE_PRIVATE
        )
        courseList = arrayListOf(
            CitiesModel("TP. Hồ Chí Minh"),
            CitiesModel("Hà Nội"),
            CitiesModel("Đà Nẵng"),
            CitiesModel("Trà Vinh"),
            CitiesModel("Kiên Giang"),
            CitiesModel("Vĩnh Long"),
            CitiesModel("Cần Thơ"),
            CitiesModel("Khánh Hòa"),
            CitiesModel("Đồng Nai"),
            CitiesModel("Thái Nguyên"),
            CitiesModel("Bắc Giang"),
            CitiesModel("Thanh Hóa"),
            CitiesModel("Thừa Thiên Huế")

            )
        val locationStr = sharedPreferences.getString("location_str", null)
        if (locationStr != null) {
            if (locationStr.isNotEmpty()) {
                val lstValues: List<String> = locationStr.split(",").map { it.trim() }
                lstValues.forEach {
                    courseList.forEachIndexed { i, element ->
                        if (element.city == it) {
                            courseList[i].isChecked = true
                        }
                    }
                }
            }
        }
        locationAdapter = LocationAdapter(
            this, courseList
        )
        binding.locationRecyclerView.adapter = locationAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val search = menu.findItem(R.id.app_bar_search)

        val searchView = search.actionView as SearchView
        searchView.maxWidth = android.R.attr.width
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {
                val checkItemModelArrayList: ArrayList<CitiesModel> = ArrayList()

                for (i in courseList) {
                    if (i.city.lowercase(Locale.getDefault())
                            .contains(newText!!.lowercase(Locale.getDefault()))
                    ) {
                        checkItemModelArrayList.add(i)
                    }
                }
                locationAdapter = LocationAdapter(
                    binding.root.context, checkItemModelArrayList
                )
                binding.locationRecyclerView.adapter = locationAdapter
                locationAdapter.notifyDataSetChanged()
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }
}