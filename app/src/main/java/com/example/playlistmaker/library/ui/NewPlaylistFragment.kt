package com.example.playlistmaker.library.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.RootActivity
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.library.domain.models.Playlist
import com.example.playlistmaker.library.ui.view_model.NewPlaylistViewModel
import com.google.android.material.snackbar.Snackbar
import java.io.File

class NewPlaylistFragment : Fragment() {

    companion object {
        fun newInstance() = NewPlaylistFragment()
    }

    private var plImagePath = String()

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewPlaylistViewModel by viewModel()

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            saveImageToInternalStorage(it)
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

        toolBar.setNavigationOnClickListener {
            findNavController().popBackStack()
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

    private fun openPhoto(){
        pickImage.launch("image/*")
    }

    private fun saveImageToInternalStorage(uri: Uri) {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val fileName = "image_${System.currentTimeMillis()}.jpg" // Уникальное имя файла
        val file = File(requireContext().filesDir, fileName)

        inputStream.use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }
        }

        plImagePath = file.absolutePath

        binding.placeholderImage.setImageURI(Uri.fromFile(file))
    }

    private fun savePlaylist() {
        val plDescription = binding.playlistDescriptionEditText.text.toString()
        val plName: String

        if (!binding.playlistNameEditText.text.isNullOrEmpty()) {
            plName = binding.playlistNameEditText.text.toString()
            val playlist = Playlist(0,plName, plDescription, plImagePath)
            viewModel.createPlaylist(playlist)

            showSnackBar(plName)
            findNavController().popBackStack()
        } else {
            checkPlaylistName()
            return
        }
    }

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
        Toast.makeText(requireContext(),"Нужно указать название плейлиста", Toast.LENGTH_LONG).show()
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.pl_dialog_title))
            .setMessage(getString(R.string.pl_dialog_message))
            .setPositiveButton(getString(R.string.pl_dialog_ok)) { dialog, _ ->
                dialog.dismiss()
                findNavController().popBackStack() // Закрыть экран
            }
            .setNegativeButton(getString(R.string.pl_dialog_cancel)) { dialog, _ ->
                dialog.dismiss() // Закрыть диалог
            }
            .show()
    }

    private fun checkForUnsavedChangesAndShowDialog() {
        val isDataChanged = isPlaylistDataChanged()

        if (isDataChanged) {
            showExitConfirmationDialog()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun isPlaylistDataChanged(): Boolean {
        return binding.playlistNameEditText.text?.isNotEmpty() ?: false
    }


}