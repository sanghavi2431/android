package `in`.woloo.www.services.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import `in`.woloo.www.databinding.ActivityServicesDetailsBinding

class ServicesDetailsActivity : AppCompatActivity() {
    lateinit var binding : ActivityServicesDetailsBinding

            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                binding = ActivityServicesDetailsBinding.inflate(layoutInflater)
                setContentView(binding.root)
    }
}