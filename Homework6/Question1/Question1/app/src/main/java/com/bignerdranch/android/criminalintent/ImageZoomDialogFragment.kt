package com.bignerdranch.android.criminalintent

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import java.io.File

class ImageZoomDialogFragment : DialogFragment() {

    private var imagePath: String? = null

    companion object {
        private const val ARG_IMAGE_PATH = "imagePath"

        fun newInstance(imagePath: String): ImageZoomDialogFragment {
            val args = Bundle().apply {
                putString(ARG_IMAGE_PATH, imagePath)
            }
            return ImageZoomDialogFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imagePath = arguments?.getString(ARG_IMAGE_PATH)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_image_zoom_dialog, container, false)
        val imageView = view.findViewById<ImageView>(R.id.zoomedImageView)

        // Load the scaled image from the path
        imagePath?.let {
            val imageFile = File(requireContext().filesDir, it)
            if (imageFile.exists()) {
                val size = Point()
                requireActivity().windowManager.defaultDisplay.getSize(size)
                val scaledBitmap = getScaledBitmap(imageFile.absolutePath, size.x, size.y)
                imageView.setImageBitmap(scaledBitmap)
            } else {
                Log.e("ImageZoomDialogFragment", "File not found: $it")
            }
        }


        return view
    }
}
