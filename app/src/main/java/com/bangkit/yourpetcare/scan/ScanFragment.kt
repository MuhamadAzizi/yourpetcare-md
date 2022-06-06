package com.bangkit.yourpetcare.scan

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bangkit.yourpetcare.databinding.FragmentScanBinding
import com.bangkit.yourpetcare.ml.Bird
import com.bangkit.yourpetcare.ml.ML
import org.tensorflow.lite.support.image.TensorImage

class ScanFragment : Fragment() {

    private var binding: FragmentScanBinding? = null
    private var REQUEST_GALLERY = 123

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentScanBinding.inflate(layoutInflater, container, false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnTakePhoto?.setOnClickListener {
            accessPhoto()
        }
    }

    private fun accessPhoto() {
        if (ContextCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            takePhotoPreview.launch(null)
        } else {
            requestPermission.launch(android.Manifest.permission.CAMERA)
        }
    }

    //request camera permission
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                takePhotoPreview.launch(null)
            } else {
                Toast.makeText(activity, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }

    //launch camera and take picture
    private val takePhotoPreview =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                binding?.imgPreview?.setImageBitmap(bitmap)
                outputGenerator(bitmap)
            }
        }

    private fun outputGenerator(bitmap: Bitmap){
        val model = ML.newInstance(requireContext())

        val newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)

        // Creates inputs for reference.
        val image = TensorImage.fromBitmap(newBitmap)

        // Runs model inference and gets result.
        val outputs = model.process(image)
            .probabilityAsCategoryList.apply {
                sortByDescending { it.score }
            }

        val highProbabilityOutput = outputs[0]

        //setting ouput text
        binding?.tvResult?.text = highProbabilityOutput.label

    }
}
