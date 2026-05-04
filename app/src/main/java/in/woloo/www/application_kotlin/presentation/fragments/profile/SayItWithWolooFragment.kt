package `in`.woloo.www.application_kotlin.presentation.fragments.profile

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import `in`.woloo.www.R
import `in`.woloo.www.databinding.FragmentSayItWithWolooBinding
import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import com.airbnb.lottie.LottieAnimationView
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.application_kotlin.SayItWithWolooViewModelFactory
import `in`.woloo.www.application_kotlin.repositories.SayItWithWolooRepository
import `in`.woloo.www.application_kotlin.view_models.SayItWithWolooViewModel
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.woloo_host.CreateWolooHostFragment.Companion.TAG
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.CustomProgressView
import `in`.woloo.www.utils.Logger
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.w3c.dom.Text
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class SayItWithWolooFragment : Fragment() {

    lateinit var binding : FragmentSayItWithWolooBinding
    private lateinit var imageUri: Uri
    private lateinit var imageFile: File
    private lateinit var imageFilePath: String
    lateinit var name :String
    lateinit var mobile : String
    private lateinit var progressView: CustomProgressView
    private val repository by lazy { SayItWithWolooRepository() }
    private val sayItWithWolooViewModel: SayItWithWolooViewModel by viewModels {
        SayItWithWolooViewModelFactory(repository)
    }


    @SuppressLint("SetTextI18n")
    private val contactPickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            data?.data?.let { contactUri ->
                val cursor = requireContext().contentResolver.query(
                    contactUri,
                    arrayOf(ContactsContract.Contacts._ID),
                    null, null, null
                )
                cursor?.use {
                    if (it.moveToFirst()) {
                        val contactIdColumnIndex = it.getColumnIndexOrThrow(ContactsContract.Contacts._ID)
                        val contactId = it.getString(contactIdColumnIndex)

                        // Query the phone number
                        val phoneCursor = requireContext().contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                            arrayOf(contactId),
                            null
                        )
                        phoneCursor?.use { pc ->
                            if (pc.moveToFirst()) {
                                val phoneNumberColumnIndex =
                                    pc.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                val phoneNumber = pc.getString(phoneNumberColumnIndex)
                                binding.receiverPhoneNumber.setText(phoneNumber) // Display the phone number in EditText
                            } else {
                                Toast.makeText(requireContext(), "No phone number found", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "No contact found", Toast.LENGTH_SHORT).show()
                    }
                } ?: Toast.makeText(requireContext(), "Failed to fetch contact", Toast.LENGTH_SHORT).show()
            }

            val data1 = result.data
            data1?.data?.let { contactUri ->
                val cursor = requireContext().contentResolver.query(
                    contactUri,
                    null, null, null, null
                )
                cursor?.use {
                    if (it.moveToFirst()) {
                        val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                        val name = it.getString(nameIndex)
                        binding.receiverNameFromContacts.setText("Selected:- $name")
                        binding.receiverNameFromContacts.visibility = View.GONE
                        binding.receiverName.setText(name)
                    }
                }
            }
        }
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val file = uriToFile(it)
                imageFilePath = uriToFilePath(it)
                if (::imageFile.isInitialized) {
                    if (imageFile.exists() && imageFile.length() > 0) {
                        Log.d("FileCheck", "File is not empty: ${imageFile.name}")
                        FileOutputStream(imageFile).use {
                            it.write(ByteArray(0)) // Write an empty byte array to truncate the file
                        }
                        Log.d("FileCheck", "File has been emptied: ${imageFile.name}")
                    } else {
                        Log.d("FileCheck", "File is empty. Initializing a new file...")
                        imageFile = File.createTempFile("captured_image", ".png", requireContext().cacheDir)
                    }
                } else {
                    Log.d("FileCheck", "File is not initialized. Initializing now...")
                    imageFile = File.createTempFile("captured_image", ".png", requireContext().cacheDir)
                }
                imageFile = file
                // Now you can use the file for upload
                Log.d("File Name is " , "$file file" )
                binding.selectImageToSend.text = imageFile.name.toString()
            }
        }


    // Register the ActivityResultLauncher for taking pictures
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                Log.d("Aarati", "Image captured: $imageUri")
                val file = uriToFile(imageUri)
                imageFilePath = uriToFilePath(imageUri)
                if (::imageFile.isInitialized) {
                    if (imageFile.exists() && imageFile.length() > 0) {
                        Log.d("FileCheck", "File is not empty: ${imageFile.name}")
                        FileOutputStream(imageFile).use {
                            it.write(ByteArray(0)) // Write an empty byte array to truncate the file
                        }
                        Log.d("FileCheck", "File has been emptied: ${imageFile.name}")
                    } else {
                        Log.d("FileCheck", "File is empty. Initializing a new file...")
                        imageFile = File.createTempFile("captured_image", ".png", requireContext().cacheDir)
                    }
                } else {
                    Log.d("FileCheck", "File is not initialized. Initializing now...")
                    imageFile = File.createTempFile("captured_image", ".png", requireContext().cacheDir)
                }
                imageFile = file
                val fileName = imageFile.name // Get the file name from the File object
                Log.d("File Name is " , "$fileName file" )
                binding.selectImageToSend.text = imageFile.name.toString()
                // Image successfully captured; you can now use `imageUri`
            } else {
                Log.d("Aarati", "Image capture failed or canceled")
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSayItWithWolooBinding.inflate(inflater, container, false)
        progressView = CustomProgressView(requireActivity())
         name = arguments?.getString(ARG_NAME).toString()
         mobile = arguments?.getString(ARG_MOBILE).toString()
       // sayItWithWolooViewModel.sendMessage(name.toString() , mobile.toString())

        binding.selectContact.setOnClickListener(
            View.OnClickListener {
                binding.receiverNameFromContacts.visibility = View.GONE
                checkPermissionAndOpenContactPicker()
            }
        )

        binding.receiverPhoneNumber.setOnClickListener(
            View.OnClickListener {
                binding.receiverNameFromContacts.visibility = View.GONE
            }
        )

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.selectImageToSend.setOnClickListener(
            View.OnClickListener {
               showImageUploadDialog()
            }
        )

        binding.deleteImage.setOnClickListener(
            View.OnClickListener {
               binding.selectImageToSend.text = ""
                if (::imageFile.isInitialized) {
                    if (imageFile.exists() && imageFile.length() > 0) {
                        Log.d("FileCheck", "File is not empty: ${imageFile.name}")
                        FileOutputStream(imageFile).use {
                            it.write(ByteArray(0)) // Write an empty byte array to truncate the file
                        }
                        Log.d("FileCheck", "File has been emptied: ${imageFile.name}")
                    }
                }
            }
        )

        binding.showImage.setOnClickListener(
            View.OnClickListener {

                if(!binding.selectImageToSend.text.toString().isEmpty())
                {
                    showImagePopup()
                }
                else{
                    Toast.makeText(requireContext() , "No image to show" , Toast.LENGTH_SHORT).show()
                }

            }
        )

        binding.saveBtn.setOnClickListener(
            View.OnClickListener {
                val phone = binding.receiverPhoneNumber.text?.toString()?.trim().orEmpty()
                val message = binding.messageForReceiver.text?.toString()?.trim().orEmpty()

                when {
                    phone.isEmpty() -> {
                        Toast.makeText(requireContext(), "Please add contact", Toast.LENGTH_SHORT).show()
                    }
                    (phone.startsWith("+91") && phone.length == 13) || (!phone.startsWith("+91") && phone.length == 10) -> {
                       if(binding.receiverName.text!!.isNotEmpty()) {
                           if (message.isNotEmpty()) {
                               showFullScreenDialog()
                           } else {
                               Toast.makeText(requireContext(), "Enter message", Toast.LENGTH_SHORT)
                                   .show()
                           }
                       }
                        else{
                            Toast.makeText(requireContext(), "Enter valid name", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else -> {
                        Toast.makeText(requireContext(), "Enter valid contact", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

            sayItWithWolooViewModel.fileUploadResponse.observe(viewLifecycleOwner) { response ->
                if(progressView!= null)
                    progressView.hide()
                if (response == null)
                {
                    Log.d("FileUpload", "Response is null")
                }
                if (response != null) {

                    if (response.isSuccessful) {
                        Log.d("FileUpload", "Success: ${response.body()}")
                        var s3Url = response.body()!!.s3URL  // Extract the URL
                        Log.d("S3URL", "File uploaded successfully: $s3Url")
                        sayItWithWolooViewModel.addMessage(requireContext() ,AppConstants.SAY_IT_WOLOO , name.toString() , mobile.toString() ,
                            binding.receiverName.text.toString() , binding.receiverPhoneNumber.text.toString(),
                            binding.messageForReceiver.text.toString() , AppConstants.SAY_IT_OCCASION ,
                            AppConstants.SAY_IT_CUSTOM_OCCASION , s3Url.toString())
                        Log.d("FileUpload", "Success: ${response.body()}")
                    } else {
                       /* sayItWithWolooViewModel.addMessage(requireContext() ,AppConstants.SAY_IT_WOLOO , name.toString() , mobile.toString() ,
                            binding.receiverName.text.toString() , binding.receiverPhoneNumber.text.toString(),
                            binding.messageForReceiver.text.toString() , AppConstants.SAY_IT_OCCASION ,
                            AppConstants.SAY_IT_CUSTOM_OCCASION , "")
                        Log.d("FileUpload", "Error: ${response.errorBody()?.string()}")*/
                        val errorMsg = response.errorBody()?.string() ?: "Something went wrong"
                      //  CommonUtils.showCustomDialogBackClick(requireContext(), errorMsg)
                        Log.d("FileUpload", "Error: $errorMsg")
                    }
                }
            }

        sayItWithWolooViewModel.messageResponse.observe(viewLifecycleOwner) { response ->
            if(progressView!= null)
            progressView.hide()
            if (response != null) {
                if (response.status == 1) {
                    // Handle success
                    Log.d("Fragment Success", response.qrId.toString())
                    sayItWithWolooViewModel.qrSend(requireContext() , response.qrId.toString())
                    // showSuccessDialog()
                    //  Toast.makeText(requireContext(), "Message added successfully!", Toast.LENGTH_SHORT).show()
                }
            }
          else{
                // Handle failure
                Log.d("Fragment Error", "Unknown error")
                Toast.makeText(requireContext(), "Failed to add message", Toast.LENGTH_SHORT).show()
            }
        }

        sayItWithWolooViewModel.qrsendResponse.observe(viewLifecycleOwner){
            result ->
            if(progressView!= null)
            progressView.hide()
            result.onSuccess { message ->
            // Handle success
            Log.d("Fragment Success", message)


             showSuccessDialog()
            //  Toast.makeText(requireContext(), "Message added successfully!", Toast.LENGTH_SHORT).show()
        }
            result.onFailure { exception ->
                // Handle failure
                Log.d("Fragment Error", exception.message ?: "Unknown error")
                if(progressView!= null)
                progressView.hide()
                Toast.makeText(requireContext(), "Failed to send message", Toast.LENGTH_SHORT).show()
            }

        }

        return binding.root


    }


    private fun checkPermissionAndOpenContactPicker() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSION_REQUEST_READ_CONTACTS
            )
        } else {
            openContactPicker()
        }
    }

    private fun openContactPicker() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        contactPickerLauncher.launch(intent)
    }


    private fun showImageUploadDialog() {
        Logger.i(TAG, "showImageUploadDialog")
        try {
            val alertDialogBuilder = AlertDialog.Builder(
                requireActivity()
            )
            val child: View = layoutInflater.inflate(R.layout.dialog_profile_image, null)
            alertDialogBuilder.setView(child)
            alertDialogBuilder.setCancelable(true)
            val alertDialog = alertDialogBuilder.create()
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val tvSelectGallery = child.findViewById<TextView>(R.id.tvSelectGallery)
            val tvImageCapture = child.findViewById<TextView>(R.id.tvImageCapture)
            val tv_image = child.findViewById<TextView>(R.id.tv_image)
            tv_image.visibility = View.GONE
            tvSelectGallery.setOnClickListener {
                if (checkStoragePermission()) {
                    Log.d("Aarati", "PERMISSION GRANTED")
                    openGallery()
                    alertDialog.dismiss()
                } else {
                    Log.d("Aarati", "PERMISSION Already not GRANTED")
                    requestStoragePermission()
                    alertDialog.dismiss()
                }
            }
            tvImageCapture.setOnClickListener {
                if (checkCameraPermission()) {
                    Log.d("Aarati", "PERMISSION GRANTED")
                    takePictureIntent()
                    alertDialog.dismiss()
                } else {
                    Log.d("Aarati", "PERMISSION Already not GRANTED")
                    requestCameraPermission()
                    alertDialog.dismiss()
                }
            }
            alertDialog.show()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun checkStoragePermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireActivity().applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun openGallery() {
        Log.d("Aarati", "in open gallery")
        pickImageLauncher.launch("image/*")

    }

    private fun requestStoragePermission() {
        Log.d("Aarati", "in request PERMISSION")
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                REQUEST_READ_EXTERNAL_STORAGE
            )
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun checkCameraPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireActivity().applicationContext,
            Manifest.permission.CAMERA
        )
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestCameraPermission() {
        Log.d("Aarati", "in request PERMISSION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_STORAGE_PERMISSION
            )
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_STORAGE_PERMISSION
            )
        }
    }

    private fun takePictureIntent() {
        imageFile = File.createTempFile(
            "captured_image", ".png", requireContext().cacheDir
        ).apply { deleteOnExit() }

        imageUri = FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider", // Replace with your FileProvider authority
            imageFile
        )

        // Launch the camera
        takePictureLauncher.launch(imageUri)

    }

    private fun uriToFile(uri: Uri): File {
        val contentResolver: ContentResolver = requireContext().contentResolver

        // Get the file name
        val fileName = getFileName(uri)

        // Create a temporary file in the cache directory
        val tempFile = File.createTempFile(fileName, null, requireContext().cacheDir).apply {
            deleteOnExit() // Automatically delete the file when the app exits
        }

        // Copy the content of the Uri to the file
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(tempFile)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return tempFile
    }

    // Get the file name from the Uri
    private fun getFileName(uri: Uri): String {
        var fileName = "temp_file"
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst() && nameIndex != -1) {
                fileName = it.getString(nameIndex)
            }
        }
        return fileName
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Logger.d(TAG, "onRequestPermissionsResult")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with selecting an image
                    openGallery()
                } else {
                    // Permission denied, show a message to the user
                    Toast.makeText(
                        requireActivity().applicationContext,
                        "Permission required to access gallery",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with selecting an image
                    takePictureIntent()
                } else {
                    // Permission denied, show a message to the user
                    Toast.makeText(
                        requireActivity().applicationContext,
                        "Permission required to access camera",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            REQUEST_WRITE_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Aarati", "Write external storage permission granted")
                    // Proceed with storage-related functionality
                    checkCameraPermission()
                } else {
                    Log.d("Aarati", "Write external storage permission denied")
                    // Show a message to the user about why the permission is needed
                }
            }
        }
        }

    private fun showImagePopup() {
        // Inflate the popup layout
        val inflater = LayoutInflater.from(requireActivity())
        val popupView = inflater.inflate(R.layout.popup_image, null)

        // Create the PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            true // Focusable, allows clicking outside to dismiss
        )

        // Set the image or load dynamically if needed
        val imageView = popupView.findViewById<ImageView>(R.id.popupImageView)
        showImageInImageView(imageFile, imageView)

        // Show the popup
        popupWindow.showAtLocation(requireView(), android.view.Gravity.CENTER, 0, 0)

        // Dismiss the popup when the background is clicked
        popupView.setOnClickListener {
            popupWindow.dismiss()
        }
    }

    fun showImageInImageView(imageFile: File, imageView: ImageView) {
        if (imageFile.exists()) {
            // Decode the image file to a Bitmap
            val bitmap: Bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

            // Set the Bitmap to the ImageView
            imageView.setImageBitmap(bitmap)
        } else {
            // Handle the case where the file does not exist
            Log.e("ImageView", "File does not exist: ${imageFile.absolutePath}")
        }
    }


    fun prepareFileUpload(filePath: String, qrid: String): Pair<MultipartBody.Part, RequestBody> {
        val file = File(filePath)

        // Create RequestBody for the file
        val requestFile = RequestBody.create("application/octet-stream".toMediaTypeOrNull(), file)

        // Wrap the file into MultipartBody.Part
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

        // Create RequestBody for the QrId
        val qridPart = RequestBody.create("text/plain".toMediaTypeOrNull(), qrid)

        return Pair(filePart, qridPart)
    }

    private fun uriToFilePath(uri: Uri): String {
        val file = uriToFile(uri)
        return file.absolutePath // Return the path of the temporary file
    }

    private fun showFullScreenDialog() {

        val inflater = requireActivity().layoutInflater
        val dialogView: View = inflater.inflate(R.layout.preview_postcard, null)

       /* val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
        val dialog = builder.create()*/

        val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(dialogView)
        dialog.show()

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.show()
        val imageView = dialogView.findViewById<ImageView>(R.id.popupImagePostCard)
       /* imageView.rotation = -5f
        val screenWidth = resources.displayMetrics.widthPixels
        val marginInPx = (20 * resources.displayMetrics.density).toInt()

        val moveToRight = ObjectAnimator.ofFloat(imageView, "translationX", 0f, (screenWidth-marginInPx).toFloat()).apply {
            duration = 2000 // 2 seconds
        }
        val moveToLeft = ObjectAnimator.ofFloat(imageView, "translationX", (screenWidth-marginInPx).toFloat(), 0f).apply {
            duration = 2000 // 2 seconds
        }
        val rotationAnimation = ObjectAnimator.ofFloat(imageView, "rotation", 25f, -5f).apply {
            duration = 2000 // 2 seconds
        }
        AnimatorSet().apply {
            playSequentially(moveToRight,moveToLeft*//*, rotationAnimation*//*)
            start()
        }
        showImageInImageView(imageFile, imageView)*/

        val textView = dialogView.findViewById<TextView>(R.id.popupTextPostcard)
        textView.text = binding.messageForReceiver.text.toString()
        if(binding.selectImageToSend.text.toString().isNotEmpty())
        {
        showImageInImageView(imageFile, imageView)
            }

       /* val fadeIn = ObjectAnimator.ofFloat(textView, "alpha", 0f, 500f).apply {
            duration = 2000 // 1 second
        }
        val slideUp = ObjectAnimator.ofFloat(textView, "translationY", textView.translationY + 250, textView.translationY).apply {
            duration = 2000 // 1 second
        }

        // Play text animations together
        AnimatorSet().apply {
            playTogether(fadeIn, slideUp)
            start()
        }
*/
        val ivBack = dialogView.findViewById<LinearLayout>(R.id.ivBack)

        val editData = dialogView.findViewById<TextView>(R.id.edit_btn)

        val saveData = dialogView.findViewById<TextView>(R.id.save_btn)

        ivBack.setOnClickListener {
            dialog.dismiss()
        }
        editData.setOnClickListener {
            dialog.dismiss()
        }
        saveData.setOnClickListener {
            dialog.dismiss()
            progressView.show()

            if(binding.receiverName.text.toString().isNotEmpty()) {
                if(binding.receiverPhoneNumber.text.toString().isNotEmpty()) {
                    if(binding.selectImageToSend.text.toString().isNotEmpty()) {
                        if(binding.messageForReceiver.text.toString().isNotEmpty()) {
                            val compressedFile = compressImageFile(imageFile, requireContext())
                            val filePath = compressedFile.absolutePath
                            val qrid = AppConstants.SAY_IT_WOLOO
                            val (filePart, qridPart) = prepareFileUpload(filePath, qrid)
                            sayItWithWolooViewModel.fileUpload(requireContext() , filePart, qridPart)
                            //  Log.d("Say it with" , "Button clicked $filePath $qrid $filePart $qridPart")
                            //  showPostcardPreviewPopup()
                        }
                        else
                        {
                            Toast.makeText(activity , " Enter Message" , Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{

                        if(!binding.messageForReceiver.text.toString().isEmpty()) {
                            sayItWithWolooViewModel.addMessage(requireContext() , AppConstants.SAY_IT_WOLOO , name.toString() , mobile.toString() ,
                                binding.receiverName.text.toString() , binding.receiverPhoneNumber.text.toString(),
                                binding.messageForReceiver.text.toString() , AppConstants.SAY_IT_OCCASION ,
                                AppConstants.SAY_IT_CUSTOM_OCCASION , "")
                        }

                    }
                }else
                {
                    Toast.makeText(activity , " Enter Receivers Mobile Number" , Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                Toast.makeText(activity , " Enter Receivers Name" , Toast.LENGTH_SHORT).show()
            }
        }

    }

    @SuppressLint("SetTextI18n")
    fun showSuccessDialog(){
        val inflater = LayoutInflater.from(requireActivity())
        val popupView = inflater.inflate(R.layout.dialog_payment_success, null)

        val displayMetrics = Resources.getSystem().displayMetrics
        val screenWidth = displayMetrics.widthPixels
        // Create the PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            (screenWidth * 0.8).toInt(),
            FrameLayout.LayoutParams.WRAP_CONTENT,
            true // Focusable, allows clicking outside to dismiss
        )

        // Set the image or load dynamically if needed
        val congratsText = popupView.findViewById<TextView>(R.id.tv_congratulations)
        congratsText.visibility = View.GONE


        val successText = popupView.findViewById<TextView>(R.id.tv_paymentsuccess)
        successText.text = "Say it with Woloo has been sent. We will notify the receiver of your Thoughtful Message!"

        val closeBtn = popupView.findViewById<TextView>(R.id.btnCloseDialog)
        closeBtn.setOnClickListener {
            popupWindow.dismiss()
            binding.receiverPhoneNumber.setText("")
            binding.selectImageToSend.text = ""
            imageFile = File("")
            binding.messageForReceiver.setText("")
            binding.receiverName.setText("")
        }


        popupWindow.setOnDismissListener {
            popupWindow.dismiss()
            binding.receiverPhoneNumber.setText("")
            binding.selectImageToSend.text = ""
            imageFile = File("")
            binding.messageForReceiver.setText("")
            binding.receiverName.setText("")
         }

        // Show the popup
        popupWindow.showAtLocation(requireView(), android.view.Gravity.CENTER, 0, 0)

        // Dismiss the popup when the background is clicked
        popupView.setOnClickListener {
            popupWindow.dismiss()
            binding.receiverPhoneNumber.setText("")
            binding.selectImageToSend.text = ""
            imageFile = File("")
            binding.messageForReceiver.setText("")
            binding.receiverName.setText("")
        }
    }

    fun compressImageFile(originalFile: File, context: Context): File {
        // Decode file into Bitmap
        val bitmap = BitmapFactory.decodeFile(originalFile.absolutePath)

        // Create a temp file in cache directory
        val compressedFile = File.createTempFile("compressed_", ".jpg", context.cacheDir)

        // Write compressed bitmap to file
        FileOutputStream(compressedFile).use { out ->
            // 80 = quality (0–100). Adjust as needed
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
        }

        return compressedFile
    }


    companion object {

        private const val REQUEST_READ_EXTERNAL_STORAGE = 111
        private const val PICK_IMAGE_REQUEST = 112
        private const val REQUEST_CAMERA_PERMISSION = 113
        private const val REQUEST_IMAGE_CAPTURE = 114
        private const val REQUEST_WRITE_STORAGE_PERMISSION = 115
        private lateinit var imageFile: File
        private const val PERMISSION_REQUEST_READ_CONTACTS = 101
        private const val ARG_NAME = "name"
        private const val ARG_MOBILE = "mobile"

            fun newInstance(name: String, mobile: String): SayItWithWolooFragment {
                val fragment = SayItWithWolooFragment()
                val args = Bundle()
                args.putString(ARG_NAME, name)
                args.putString(ARG_MOBILE, mobile)
                fragment.arguments = args
                return fragment
            }




    }
}



