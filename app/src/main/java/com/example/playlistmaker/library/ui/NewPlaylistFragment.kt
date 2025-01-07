package com.example.playlistmaker.library.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.playlistmaker.R
import com.example.playlistmaker.RootActivity
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.library.ui.view_model.NewPlaylistViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewPlaylistViewModel by viewModel()

    private var coverIsSaved: Boolean = false

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                viewModel.saveImage(it.toString())
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createButton.isEnabled = false

        val toolBar = binding.toolBar
        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(toolBar)
            supportActionBar?.apply {
                title = getString(R.string.new_pl_toolbar_title)
                setDisplayHomeAsUpEnabled(true)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.imageUri.collect { imageUri ->
                    coverIsSaved = imageUri.toString().isNotEmpty()
                    binding.placeholderImage.setImageURI(imageUri)
                    binding.placeholderImage.scaleType = ImageView.ScaleType.CENTER_CROP
                }
            }
        }

        val coverFrame = binding.placeholderImage
        coverFrame.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val width = coverFrame.width
                coverFrame.layoutParams.height = width
                coverFrame.requestLayout()
                coverFrame.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })


        toolBar.setNavigationOnClickListener {
            checkForUnsavedChangesAndShowDialog()
        }

        (activity as? RootActivity)?.hideBottomNavigationView()

        binding.placeholderImage.setOnClickListener {
            openPhoto()
        }

        binding.createButton.setOnClickListener {
            savePlaylist()
        }

        binding.playlistNameEditText.addTextChangedListener(
            afterTextChanged = { text: CharSequence? ->
                binding.createButton.isEnabled = !text.isNullOrEmpty()
            },

            onTextChanged = { text, _, _, _ ->
                if (!text.isNullOrEmpty()) binding.createButton.isEnabled = true
            }
        )

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            checkForUnsavedChangesAndShowDialog()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        (activity as? RootActivity)?.showBottomNavigationView()
    }

    private fun openPhoto() {
        pickImage.launch("image/*")
    }

    private fun savePlaylist() {
        val description = binding.playlistDescriptionEditText.text.toString()
        val name = binding.playlistNameEditText.text.toString()

        if (name.isNotEmpty()) {
            viewModel.createPlaylist(name, description)

            showSnackBar(name)
            parentFragmentManager.popBackStack()
        } else {
            checkPlaylistName()
            return
        }
    }

    @SuppressLint("InflateParams")
    private fun showSnackBar(plName: String) {
        val snackBar = Snackbar.make(requireView(), "", Snackbar.LENGTH_LONG)
        val customView = LayoutInflater.from(requireContext())
            .inflate(R.layout.custom_toast_view, null)
        val textView = customView.findViewById<TextView>(R.id.custom_toast_text)
        textView.text = getString(R.string.pl_toast_text).format(plName)
        val snackBarView = snackBar.view as FrameLayout
        snackBarView.addView(customView, 0)
        snackBar.show()
    }

    private fun checkPlaylistName() {
        Toast.makeText(requireContext(), getString(R.string.checkPLName_message), Toast.LENGTH_LONG)
            .show()
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.pl_dialog_title))
            .setMessage(getString(R.string.pl_dialog_message))
            .setPositiveButton(getString(R.string.pl_dialog_ok)) { dialog, _ ->
                dialog.dismiss()
                parentFragmentManager.popBackStack() // Закрыть экран
            }
            .setNegativeButton(getString(R.string.pl_dialog_cancel)) { dialog, _ ->
                dialog.dismiss() // Закрыть диалог
            }
            .show()
    }

    private fun checkForUnsavedChangesAndShowDialog() {
        val isDataChanged = isPlaylistDataChanged()

        if (!isDataChanged) {
            showExitConfirmationDialog()
        } else {
            parentFragmentManager.popBackStack()
        }
    }

    private fun isPlaylistDataChanged(): Boolean {
        return binding.playlistNameEditText.text?.isNotEmpty() == true ||
                binding.playlistDescriptionEditText.text?.isNotEmpty() == true || coverIsSaved
    }
}