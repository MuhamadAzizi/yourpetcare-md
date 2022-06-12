package com.bangkit.yourpetcare.scan

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bangkit.yourpetcare.PermissionUtility
import com.bangkit.yourpetcare.databinding.FragmentScanBinding
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ScanFragment : Fragment(), UploadRequestBody.UploadCallback {

    private var binding: FragmentScanBinding? = null
    private val TAG = ScanFragment::class.java.simpleName
    private val PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var permissionUtility: PermissionUtility? = null

    //    private lateinit var presenter: ScanPresenter
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentScanBinding.inflate(layoutInflater, container, false)

        binding?.btnTakePhoto?.setOnClickListener {
            uploadPhoto()
        }
        permissionUtility = PermissionUtility(requireActivity(), PERMISSIONS)

        binding?.imgPreview?.setOnClickListener {
            if (permissionUtility!!.arePermissionsEnabled()) {
                openImage()
            }else{
                permissionUtility!!.requestMultiplePermissions()
            }

        }
        return binding?.root
    }


    private fun openImage() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!permissionUtility!!.onRequestPermissionsResult(
                requestCode,
                permissions.toMutableList(),
                grantResults
            )
        ) {
            permissionUtility!!.requestMultiplePermissions()
        } else {
            openImage()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {

                    selectedImageUri = data?.data
                    binding?.imgPreview?.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun uploadPhoto() {
        selectedImageUri?.let {
            context?.let { mcontext ->
                binding?.progressbarScan?.visibility= View.VISIBLE
                Log.d("daa uploadPhoto", "uri iamge  $selectedImageUri")
                val file = File(getRealPathFromURI(mcontext, it))
                val progressCallback = UploadRequestBody(file, "image/*", this)
                val body = MultipartBody.Part.createFormData("file", file.name, progressCallback)
                ApiService.invoke().predict(body).enqueue(object : Callback<ScanResponse> {
                    override fun onResponse(
                        call: Call<ScanResponse>,
                        response: Response<ScanResponse>
                    ) {
                        binding?.progressbarScan?.visibility= View.GONE
                        Log.d("daa uploadPhoto", "uri iamge  aresponse")
                        if (response.isSuccessful) {
                            if (response.body()?.message.equals("success")) {
                                binding?.tvResult?.text = response.body()?.predicted
                                binding?.tvResultConfidence?.text = response.body()?.confidence.toString()
                            }
                        } else {
                            Toast.makeText(
                                mcontext,
                                response.message() ?: "Failed Response",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

                    override fun onFailure(call: Call<ScanResponse>, t: Throwable) {
                        Log.d("daa uploadPhoto", "uri iamge  failed "+t.message)
                        binding?.progressbarScan?.visibility= View.GONE

                        Toast.makeText(mcontext, t.message, Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }

    }

    fun getRealPathFromURI(
        context: Context,
        contentUri: Uri
    ): String? {
        var cursor: Cursor? = null
        return try {
            val proj =
                arrayOf(MediaStore.Video.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            ""
        } finally {
            cursor?.close()
        }
    }

    override fun onProgressUpdate(percentage: Int) {
        binding?.progressbarScan?.progress = percentage
    }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
    }

}