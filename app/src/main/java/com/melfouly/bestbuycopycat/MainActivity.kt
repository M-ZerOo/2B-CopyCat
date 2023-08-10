package com.melfouly.bestbuycopycat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.melfouly.bestbuycopycat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocaleHelper.setLanguage(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adapter Setup.
        adapter = ViewPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = adapter

        // BottomNavigationBar onItemSelect.
        binding.bottomNavigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    binding.viewPager.currentItem = 0
                    binding.bottomNavigationBar.menu.getItem(0).isChecked = true
                }

                R.id.categoriesFragment -> {
                    binding.viewPager.currentItem = 1
                    binding.bottomNavigationBar.menu.getItem(1).isChecked = true
                }

                R.id.dealsFragment -> {
                    binding.viewPager.currentItem = 2
                    binding.bottomNavigationBar.menu.getItem(2).isChecked = true
                }

                R.id.notificationsFragment -> {
                    binding.viewPager.currentItem = 3
                    binding.bottomNavigationBar.menu.getItem(3).isChecked = true
                }

                R.id.accountFragment -> {
                    binding.viewPager.currentItem = 4
                    binding.bottomNavigationBar.menu.getItem(4).isChecked = true
                }
            }
            return@setOnItemSelectedListener false
        }

    }


}