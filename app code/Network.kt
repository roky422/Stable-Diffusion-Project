package com.example.aistablediffusionprojectapp // Replace with your package name


import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Part
import okhttp3.OkHttpClient
import okhttp3.MultipartBody
import java.util.concurrent.TimeUnit
import okhttp3.RequestBody
import okhttp3.ResponseBody


// Data classes for request and response
data class ImageRequest(
    @SerializedName("prompt") val prompt: String
)

data class ImageResponse(
    @SerializedName("image") val image: String
)

// API interface
interface ImageApi {
    @POST("/select_model")
    fun sendModelSelection(@Body request: ModelSelectionRequest): Call<Void>
    @POST("/generate_image")
    fun generateImage(@Body request: ImageRequest): Call<ImageResponse>
    @Multipart
    @POST("/send_text") // Replace with your actual endpoint
    fun sendImageToBlip(@Part image: MultipartBody.Part): Call<ResponseBody>
    @Multipart
    @POST("/send_image") // Replace with your actual endpoint
    fun sendImageToImageToImage(@Part image: MultipartBody.Part): Call<ResponseBody>
}

// Retrofit instance
object RetrofitInstance {
    val api: ImageApi by lazy {
        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS) // Increase connect timeout to 60 seconds
            .readTimeout(180, TimeUnit.SECONDS)    // Increase read timeout to 60 seconds
            .writeTimeout(180, TimeUnit.SECONDS)   // Increase write timeout to 60 seconds
            .build()
        Retrofit.Builder()
            .baseUrl("https://champion-man-active.ngrok-free.app") // Replace with your PC's IP address
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ImageApi::class.java)
    }
}

