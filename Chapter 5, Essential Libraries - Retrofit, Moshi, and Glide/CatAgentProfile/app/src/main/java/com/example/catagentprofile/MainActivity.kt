package com.example.catagentprofile

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.catagentprofile.api.TheCatApiService
import com.example.catagentprofile.model.ImageResultData
import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(
                MoshiConverterFactory.create()
            ).build()
    }

    private  val theCatApiService by lazy {
        retrofit.create(TheCatApiService::class.java)
    }

    private val agentBreedView: TextView by lazy {
        findViewById(R.id.main_agent_breed_value) }

    private val profileImageView: ImageView by lazy {
        findViewById(R.id.main_profile_image) }

    private val imageLoader: ImageLoader by lazy {
        GlideImageLoader(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getCatImageResponse()
    }

    private fun getCatImageResponse() {
        val call = theCatApiService.searchImages(1, "full")
        call.enqueue(object : Callback<List<ImageResultData>> {

            override fun onFailure(call: Call<List<ImageResultData>>, t: Throwable) {
                Log.e("MainActivity",
                    "Failed to get search results", t)
            }

            override fun onResponse(call:
                Call<List<ImageResultData>>,
                response: Response<List<ImageResultData>>
            ) {
                if (response.isSuccessful) {
                    val imageResults = response.body()
                    val firstImageUrl = imageResults
                        ?.firstOrNull()?.imageUrl.orEmpty()

                    if (firstImageUrl.isNotBlank()) {
                        imageLoader.loadImage(
                            firstImageUrl, profileImageView)
                    } else {
                        Log.d("MainActivity", "Missing image URL")
                    }

                    agentBreedView.text = imageResults
                        ?.firstOrNull()?.breeds?.firstOrNull()
                        ?.name ?: "Unknown"
                } else {
                    Log.e(
                        "MainActivity",
                        "Failed to get search results\n${
                            response.errorBody()?.string().orEmpty()
                        }"
                    )
                }
            }
        })
    }
}