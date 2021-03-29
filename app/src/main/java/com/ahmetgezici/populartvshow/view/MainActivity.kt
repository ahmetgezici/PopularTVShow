package com.ahmetgezici.populartvshow.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ahmetgezici.populartvshow.R
import com.ahmetgezici.populartvshow.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    var manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ////////////////////////////////////////////////////////////////////////////////////////////

        val popularTVFragment = PopularTVFragment()

        manager.beginTransaction()
            .setCustomAnimations(R.anim.fadein, R.anim.fadeout)
            .replace(binding.fragmentLayout.id, popularTVFragment, popularTVFragment.tag)
            .commit()

    }
}