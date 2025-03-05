package com.example.aistablediffusionprojectapp

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            AIStableDiffusionProjectAppTheme {
//                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            AIStableDiffusionProjectAppTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    MainScreen()
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MainScreen() {
//    var prompt by remember { mutableStateOf("") }
//    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
//    var isLoading by remember { mutableStateOf(false) }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(
//            text = "AI Image Generator",
//            style = MaterialTheme.typography.headlineLarge,
//            textAlign = TextAlign.Center,
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(32.dp))
//        OutlinedTextField(
//            value = prompt,
//            onValueChange = { prompt = it },
//            label = { Text("Enter Prompt") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(
//            onClick = {
//                errorMessage = null
//                imageBitmap = null
//                isLoading = true
//                val request = ImageRequest(prompt)
//                RetrofitInstance.api.generateImage(request)
//                    .enqueue(object : Callback<ImageResponse> {
//                        override fun onResponse(
//                            call: Call<ImageResponse>,
//                            response: Response<ImageResponse>
//                        ) {
//                            isLoading = false
//                            if (response.isSuccessful) {
//                                val imageResponse = response.body()
//                                val base64Image = imageResponse?.image
//                                if (base64Image != null) {
//                                    val decodedString: ByteArray =
//                                        Base64.decode(base64Image, Base64.DEFAULT)
//                                    val decodedByte = BitmapFactory.decodeByteArray(
//                                        decodedString,
//                                        0,
//                                        decodedString.size
//                                    )
//                                    imageBitmap = decodedByte
//                                }
//                            } else {
//                                errorMessage = "Error: ${response.errorBody()?.string()}"
//                            }
//                        }
//
//                        override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
//                            isLoading = false
//                            errorMessage = "Network error: ${t.message}"
//                        }
//                    })
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Generate Image")
//        }
//        Spacer(modifier = Modifier.height(32.dp))
//        if (isLoading) {
//            CircularProgressIndicator(modifier = Modifier.size(48.dp))
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        if (errorMessage != null) {
//            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        if (imageBitmap != null) {
//            Image(
//                bitmap = imageBitmap!!.asImageBitmap(),
//                contentDescription = " "
//            )
//        }
//    }}

//fun sendImageToServer(bitmap: Bitmap, callback: (String) -> Unit) {
//    val context =
//    val file = bitmapToFile(bitmap, context)
//    val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
//    val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
//    val description = RequestBody.create("text/plain".toMediaTypeOrNull(), "Image Description")
//
//    RetrofitInstance.api.sendImage(imagePart, description).enqueue(object : Callback<String> {
//        override fun onResponse(call: Call<String>, response: Response<String>) {
//            if (response.isSuccessful) {
//                val responseBody = response.body()
//                callback(responseBody ?: "No response")
//            } else {
//                callback("Error: ${response.errorBody()?.string()}")
//            }
//        }
//
//        override fun onFailure(call: Call<String>, t: Throwable) {
//            callback("Network Error: ${t.message}")
//        }
//    })
//}

//fun Context.sendImageToServer(bitmap: Bitmap, authority: String, callback: (Result<String>) -> Unit){
//    // Use a more descriptive name for the file
//    val imageFile: File
//    try {
//        imageFile = bitmap.toTempFile(this)
//    } catch (e: IOException) {
//        callback(Result.failure(e))
//        return
//    }
//
//    // Use a more descriptive name for the request body
//    val imageRequestBody = imageFile.asRequestBody("image/jpeg".toMediaType())
//    val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)
//
//    // Use a more descriptive name for the description
//    // val descriptionRequestBody = "Image Description".toRequestBody("text/plain".toMediaType())
//
//    // Use a more descriptive name for the API call
//    RetrofitInstance.api.sendImage(imagePart).enqueue(object : Callback<ResponseBody> {
//        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//            if (response.isSuccessful) {
//                response.body()?.string()?.let { responseString ->
//                    val jsonObject = JSONObject(responseString)
//                    val extractedText = jsonObject.getString("text") // Extracts the "text" field
//                    callback(Result.success(extractedText ?: "No response"))
//                }
////                val responseBody = response.body()
////                callback(Result.success(responseBody ?: "No response"))
//            } else {
//                val errorBody = response.errorBody()?.string() ?: "Unknown error"
//                callback(Result.failure(RuntimeException("Error: $errorBody")))
//            }
//            // Delete the temporary file after the request is completed
//            imageFile.delete()
//        }
//
//        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//            callback(Result.failure(t))
//            // Delete the temporary file if the request fails
//            imageFile.delete()
//        }
//
//    })
//}

//    call.enqueue(object : Callback<ResponseBody> {
//        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//            if (response.isSuccessful) {
//                val responseBody = response.body()
//                val textResponse = responseBody?.string()
//                val responseImage = responseBody?.string()
//                if (responseBody != null) {
//                    when (selectedModel) {
//                        "Blip - Image to Text" -> {
//                            val jsonObject1 = textResponse?.let { JSONObject(it) }
//                            val extractedText = jsonObject1?.getString("text")
//                            callback(Result.success(extractedText ?: "No response"))
//                        }
//
//                        "Image to Image" -> {
//                            val jsonObject2 = responseImage?.let { JSONObject(it) }
//                            val base64Image = jsonObject2?.getString("image")
//
//                            // **2. Decode the Base64 String**
//                            val decodedString: ByteArray =
//                                Base64.decode(base64Image, Base64.DEFAULT)
//
//                            // **3. Decode the Byte Array to a Bitmap**
//                            val decodedByte =
//                                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
////                            val bitmap = BitmapFactory.decodeByteArray(responseBody.toByteArray(), 0, responseBody.toByteArray().size)
//                            callback(Result.success(decodedByte))
//                        }
//                    }
//                } else {
//                    callback(Result.failure(Exception("Empty response body")))
//                }
//            } else {
//                callback(Result.failure(Exception("Request failed with code: ${response.code()}")))
//            }
//        }
//
//
//        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//            callback(Result.failure(t))
//        }
//    })