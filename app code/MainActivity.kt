package com.example.aistablediffusionprojectapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.aistablediffusionprojectapp.ui.theme.AIStableDiffusionProjectAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Slider
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import androidx.compose.material3.Switch
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.Divider
import androidx.compose.ui.text.font.FontFamily
import com.example.aistablediffusionprojectapp.ui.theme.AppTheme
import com.example.aistablediffusionprojectapp.ui.theme.AppTypography
import com.example.aistablediffusionprojectapp.ui.theme.itModernaFontFamily
import com.example.aistablediffusionprojectapp.ui.theme.montserratFontFamily
import androidx.compose.material.icons.filled.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.content.FileProvider
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID
import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.rotate
//import androidx.compose.ui.graphics.Matrix
import androidx.core.app.ActivityCompat
import java.util.concurrent.Executors
import androidx.camera.core.ImageAnalysis
import java.nio.ByteBuffer
import androidx.camera.core.AspectRatio
import java.io.ByteArrayOutputStream
import android.graphics.Matrix
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Check
import okhttp3.ResponseBody
import org.json.JSONObject

// Data class for bottom navigation items
data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)

class MainActivity : ComponentActivity() {
//    var darkModeEnabled by mutableStateOf(false)
//    var fontSize by mutableStateOf(16)
//    var useCustomFont by rememberSaveable { mutableStateOf(false) }
//    var selectedFont by rememberSaveable { mutableStateOf("Default") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var darkModeEnabled by rememberSaveable { mutableStateOf(false) }
            var fontSize by rememberSaveable { mutableStateOf(16) }
            var useCustomFont by rememberSaveable { mutableStateOf(false) }
            var selectedFont by rememberSaveable { mutableStateOf("Default") }
            // Step 3: Create the `currentTypography` Variable (HERE)
            val currentTypography = AppTypography(
                defaultFontFamily = when {
                    useCustomFont && selectedFont == "Font 1" -> montserratFontFamily
                    useCustomFont && selectedFont == "Font 2" -> itModernaFontFamily
                    else -> FontFamily.Default
                },
                fontSize = fontSize
            )

            AppTheme(darkModeEnabled = darkModeEnabled,
                useCustomFont = useCustomFont,
                selectedFont = selectedFont,
                appTypography = currentTypography) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        darkModeEnabled = darkModeEnabled,
                        onDarkModeEnabledChange = { darkModeEnabled = it },
                        fontSize = fontSize,
                        onFontSizeChange = { fontSize = it },
                        useCustomFont = useCustomFont,
                        onUseCustomFontChange = { useCustomFont = it },
                        selectedFont = selectedFont,
                        onSelectedFontChange = { selectedFont = it }
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    darkModeEnabled: Boolean,
    onDarkModeEnabledChange: (Boolean) -> Unit,
    fontSize: Int,
    onFontSizeChange: (Int) -> Unit,
    useCustomFont: Boolean,
    onUseCustomFontChange: (Boolean) -> Unit,
    selectedFont: String,
    onSelectedFontChange: (String) -> Unit
) {
    val navController = rememberNavController()
    val bottomNavItems = listOf(
        BottomNavItem("Main", "main", Icons.Filled.Home),
        BottomNavItem("Gallery", "gallery", Icons.Filled.Image),
        BottomNavItem("Settings", "settings", Icons.Filled.Settings),
        BottomNavItem("Extra Models", "extra_models", Icons.Rounded.AutoAwesome),
    )
    var selectedModel by remember { mutableStateOf("Model 1") } // Default model
    val generatedImages = remember { mutableStateListOf<Bitmap>() }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.name) },
                        label = { Text(item.name) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("main") {
                MainContent(selectedModel = selectedModel, onModelSelected = { selectedModel = it }, onImageGenerated = { generatedImages.add(it) })
            }
            composable("gallery") { GalleryContent(generatedImages) }
            composable("extra_models") {
                ModelsContent(generatedImages = generatedImages) }
            composable("settings") {
                SettingsContent(
                    darkModeEnabled = darkModeEnabled,
                    onDarkModeEnabledChange = onDarkModeEnabledChange,
                    fontSize = fontSize,
                    onFontSizeChange = onFontSizeChange,
                    useCustomFont = useCustomFont,
                    onUseCustomFontChange = onUseCustomFontChange,
                    selectedFont = selectedFont,
                    onSelectedFontChange = onSelectedFontChange
                )
            }
        }
    }
}

data class ModelSelectionRequest(
    val mainModel: String?,
    val narutoModel: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(selectedModel: String, onModelSelected: (String) -> Unit, onImageGenerated: (Bitmap) -> Unit) {
    var prompt by remember { mutableStateOf("") }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedMainModel by remember { mutableStateOf<String?>("v1-5 Model") } // Default to FN Naruto
    var selectedNarutoModel by remember { mutableStateOf<String?>("FN Naruto - 1 epoch") } // Default Naruto model

    fun sendModelSelectionToServer(mainModel: String?, narutoModel: String?) {
        val request = ModelSelectionRequest(mainModel, narutoModel)
        RetrofitInstance.api.sendModelSelection(request)
            .enqueue(object : Callback<Void> { // Assuming your server doesn't send back data
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("ModelSelection", "Model selection sent successfully")
                    } else {
                        Log.e("ModelSelection", "Error sending model selection: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("ModelSelection", "Network error sending model selection: ${t.message}")
                }
            })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "AI Image Generator",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))
        // Model Selection Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { selectedMainModel = "FN Naruto" },
                colors = if (selectedMainModel == "FN Naruto") {
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF8800),
                        contentColor = Color.Black
                    )
                } else {
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD3D3D3), // Light Gray
                        contentColor = Color.Black
                    )
                }
            ) {
                Text("FN Naruto")
            }
            Button(
                onClick = {
                    selectedMainModel = "v1-5 Model"
                    selectedNarutoModel = null
                },
                colors = if (selectedMainModel == "v1-5 Model") {
                    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                } else {
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD3D3D3), // Light Gray
                        contentColor = Color.Black
                    )
                }
            ) {
                Text("v1-5 Model")
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        // Naruto Model Selection Buttons
        if (selectedMainModel == "FN Naruto") {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val radioOptions = listOf(
                    "FN Naruto - 1 epoch",
                    "FN Naruto - 20 epoch",
                    "FN Naruto - 50 epoch"
                )
                radioOptions.forEach { text ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (text == selectedNarutoModel),
                                onClick = {
                                    selectedNarutoModel = text
                                    onModelSelected(text)
                                },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedNarutoModel),
                            onClick = {
                                selectedNarutoModel = text
                                onModelSelected(text)
                            },
                            enabled = true,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.Yellow, // Inner circle when selected
                                unselectedColor = Color(0xFFD3D3D3), // Outer circle when unselected
                            ),
                            modifier = Modifier.padding(3.dp)
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 1.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            label = { Text("Enter Prompt") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                errorMessage = null
                imageBitmap = null
                isLoading = true
                val request = ImageRequest(prompt)
                sendModelSelectionToServer(selectedMainModel, selectedNarutoModel)
                RetrofitInstance.api.generateImage(request)
                    .enqueue(object : Callback<ImageResponse> {
                        override fun onResponse(
                            call: Call<ImageResponse>,
                            response: Response<ImageResponse>
                        ) {
                            isLoading = false
                            if (response.isSuccessful) {
                                val imageResponse = response.body()
                                val base64Image = imageResponse?.image
                                if (base64Image != null) {
                                    val decodedString: ByteArray =
                                        Base64.decode(base64Image, Base64.DEFAULT)
                                    val decodedByte = BitmapFactory.decodeByteArray(
                                        decodedString,
                                        0,
                                        decodedString.size
                                    )
                                    imageBitmap = decodedByte
                                    decodedByte?.let { onImageGenerated(it) }
                                }
                            } else {
                                errorMessage = "Error: ${response.errorBody()?.string()}"
                            }
                        }

                        override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                            isLoading = false
                            errorMessage = "Network error: ${t.message}"
                        }
                    })

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate Image")
        }
        Spacer(modifier = Modifier.height(32.dp))
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (errorMessage != null) {
            Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!.asImageBitmap(),
                contentDescription = " "
            )
        }
    }
}

@Composable
fun GalleryContent(generatedImages: List<Bitmap>) {
    var showImages by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Gallery",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
        Button(onClick = { showImages = !showImages }) {
            Text(if (showImages) "Hide Images" else "Show Images")
        }
        if (showImages) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3), // 3 columns
                contentPadding = PaddingValues(8.dp) // Add some padding around the grid
            ) {
                items(generatedImages) { image ->
                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                    ) {
                        AsyncImage(
                            model = image,
                            contentDescription = "Generated Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
//            LazyColumn(
//                modifier = Modifier.fillMaxSize(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                items(generatedImages) { image ->
//                    Image(
//                        bitmap = image.asImageBitmap(),
//                        contentDescription = "Generated Image",
//                        modifier = Modifier
//                            .size(200.dp)
//                            .padding(8.dp)
//                    )
//                }
//            }
        }
    }
}

@Composable
fun SettingsContent(darkModeEnabled: Boolean,
                    onDarkModeEnabledChange: (Boolean) -> Unit,
                    fontSize: Int,
                    onFontSizeChange: (Int) -> Unit,
                    useCustomFont: Boolean,
                    onUseCustomFontChange: (Boolean) -> Unit,
                    selectedFont: String,
                    onSelectedFontChange: (String) -> Unit
) {
    var doUseCustomFont by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Interface Customization",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Dark Mode")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = darkModeEnabled,
                    onCheckedChange = onDarkModeEnabledChange
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Font Size")
                Spacer(modifier = Modifier.weight(1f))
                Slider(
                    value = fontSize.toFloat(),
                    onValueChange = { onFontSizeChange(it.toInt()) },
                    valueRange = 12f..24f,
                    steps = 11
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Custom Font")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = useCustomFont,
                    onCheckedChange = {isChecked ->
                        if (isChecked) {
                            onUseCustomFontChange(true)
                        } else {
                            onSelectedFontChange("Default")
                            onUseCustomFontChange(false)
                        }
                    }
                )
            }
            DropdownMenu(
                expanded = useCustomFont,
                onDismissRequest = { onUseCustomFontChange(false) }
            ) {
                DropdownMenuItem(
                    text = { Text("Default") },
                    onClick = {
                        onSelectedFontChange("Default")
                        onUseCustomFontChange(false)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Font 1") },
                    onClick = {
                        onSelectedFontChange("Font 1")
                        onUseCustomFontChange(false)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Font 2") },
                    onClick = {
                        onSelectedFontChange("Font 2")
                        onUseCustomFontChange(false)
                    }
                )
            }
        }
    }
}

@Composable
fun ModelsContent(generatedImages: List<Bitmap>) {
    var showCamera by remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf<Bitmap?>(null) }
    var serverResponse by remember { mutableStateOf<Any?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var selectedModel by remember { mutableStateOf("Blip - Image to Text") }
    val context = LocalContext.current
    val cameraPermission = Manifest.permission.CAMERA
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                showCamera = true
            } else {
                // Handle permission denial
            }
        }
    )
    var showGeneratedImages by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Extra Models",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Image Source Selection
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = selectedModel == "Blip - Image to Text",
                onClick = { selectedModel = "Blip - Image to Text" },
                label = { Text("Blip - Image to Text") },
                leadingIcon = {
                    if (selectedModel == "Blip - Image to Text") {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Selected",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                }
            )
            FilterChip(
                selected = selectedModel == "Image to Image",
                onClick = { selectedModel = "Image to Image" },
                label = { Text("Kandinsky - Image to Image") },
                leadingIcon = {
                    if (selectedModel == "Image to Image") {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Selected",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                if (ContextCompat.checkSelfPermission(context, cameraPermission) == PackageManager.PERMISSION_GRANTED) {
                showCamera = true
                } else {
                    launcher.launch(cameraPermission)
                }
                Log.d("ExtraModelsContent", "Camera button clicked, showCamera = $showCamera")
            }) {
                Icon(Icons.Filled.Camera, contentDescription = "Camera")
                Text("Camera")
            }
            Button(onClick = {
                selectedImage = null
                serverResponse = null
                showGeneratedImages = !showGeneratedImages }) {
                Icon(Icons.Filled.Image, contentDescription = "Generated Images")
                Text("Generated Images")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Camera Preview
        if (showCamera) {
            Log.d("ExtraModelsContent", "Entering showCamera block")
            CameraPreview(
                onImageCaptured = { bitmap ->
                    selectedImage = bitmap
                    showCamera = false
                }
            )
        }

        // Generated Images
        if (showGeneratedImages && generatedImages.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f)

            ) {
                items(generatedImages) { image ->
                    Image(
                        bitmap = image.asImageBitmap(),
                        contentDescription = "Generated Image",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                selectedImage = image
                                showGeneratedImages = false
                            },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        // Selected Image Display
        selectedImage?.let { image ->
            Image(
                bitmap = image.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                isLoading = true
                context.sendImageToServer(image, "com.example.aistablediffusionprojectapp.fileprovider",selectedModel) { result ->
                    isLoading = false
                    result.onSuccess { response ->
                        serverResponse = response
                    }.onFailure { exception ->
                        serverResponse = "Error: ${exception.message}"
                    }
                }
            }) {
                Text("Send Image")
            }
        }
        if (isLoading) {
            CircularProgressIndicator()
        }
        // Server Response
        serverResponse?.let { response ->
            when (response) {
                is String -> Text(text = "Server Response: $response")
                is Bitmap -> Image(
                    bitmap = response.asImageBitmap(),
                    contentDescription = "Server Response Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Fit
                )
                else -> Text(text = "Server Response: Unknown data type")
            }
        }
    }

}
fun Context.sendImageToServer(fileImage: Bitmap, fileProviderAuthority: String, selectedModel: String, callback: (Result<Any>) -> Unit) {
    val file = File(cacheDir, "temp_image.jpg")
    try {
        val stream = FileOutputStream(file)
        fileImage.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
    } catch (e: IOException) {
        callback(Result.failure(e))
        return
    }

    val uri = FileProvider.getUriForFile(this, fileProviderAuthority, file)
    val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
    val body = MultipartBody.Part.createFormData("image", file.name, requestFile)


    val call: Call<ResponseBody> = when (selectedModel) {
        "Blip - Image to Text" -> RetrofitInstance.api.sendImageToBlip(body)
        "Image to Image" -> RetrofitInstance.api.sendImageToImageToImage(body)
        else -> {
            callback(Result.failure(IllegalArgumentException("Invalid model selected")))
            return
        }
    }

    when (selectedModel) {
        "Blip - Image to Text" -> {
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            response.body()?.string()?.let { responseString ->
                            val jsonObject = JSONObject(responseString)
                            val extractedText = jsonObject.getString("text") // Extracts the "text" field
                            callback(Result.success(extractedText ?: "No response"))
                            }
                        } else {
                            callback(Result.failure(Exception("Empty response body")))
                        }
                    } else {
                        callback(Result.failure(Exception("Request failed with code: ${response.code()}")))
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback(Result.failure(t))
                }
            })
        }
        "Image to Image" -> {
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
    //                    val textResponse = responseBody?.string()
    //                    val responseImage = responseBody?.string()
                        if (responseBody != null) {
                            response.body()?.string()?.let { responseString ->
                                val jsonObject = JSONObject(responseString)
                                val base64Image = jsonObject.getString("image")

                                // **2. Decode the Base64 String**
                                val decodedString: ByteArray =
                                    Base64.decode(base64Image, Base64.DEFAULT)

                                // **3. Decode the Byte Array to a Bitmap**
                                val decodedByte = BitmapFactory.decodeByteArray(
                                    decodedString,
                                    0,
                                    decodedString.size
                                )
                                // val bitmap = BitmapFactory.decodeByteArray(responseBody.toByteArray(), 0, responseBody.toByteArray().size)
                                callback(Result.success(decodedByte))
                            }
                        } else {
                            callback(Result.failure(Exception("Empty response body")))
                        }
                    } else {
                        callback(Result.failure(Exception("Request failed with code: ${response.code()}")))
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback(Result.failure(t))
                }
            })
        }
    }
}


@Throws(IOException::class)
fun Bitmap.toTempFile(context: Context, fileName: String = "temp_image_${UUID.randomUUID()}.jpg"): File {
    val file = File(context.cacheDir, fileName)
    FileOutputStream(file).use { outputStream ->
        this.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
    }
    return file
}

fun bitmapToFile(bitmap: Bitmap, context: Context): File {
    val file = File(context.cacheDir, "temp_image.jpg")
    file.createNewFile()
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.flush()
    outputStream.close()
    return file
}

fun imageProxyToBitmap(image: ImageProxy): Bitmap {
    val buffer = image.planes[0].buffer
    buffer.rewind()
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

@Composable
fun CameraPreview(onImageCaptured: (Bitmap) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var preview by remember { mutableStateOf<Preview?>(null) }
    val executor = ContextCompat.getMainExecutor(context)
    var imageCapture: ImageCapture? = null

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                imageCapture = ImageCapture.Builder().build()
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
                preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            }, executor)
            previewView
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            imageCapture?.let { capture ->
                capture.takePicture(
                    executor,
                    object : ImageCapture.OnImageCapturedCallback() {
                        override fun onCaptureSuccess(image: ImageProxy) {
                            val bitmap = imageProxyToBitmap(image).rotate(image.imageInfo.rotationDegrees.toFloat())
                            onImageCaptured(bitmap)
                            image.close()
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e("CameraPreview", "Error capturing image", exception)
                        }
                    }
                )
            }
        }) {
            Text("Capture")
        }
    }
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}