package com.example.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolBar = binding.settingsToolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(toolBar)
        toolBar.title = getString(R.string.settings_title)

        settingsViewModel.isDarkTheme.observe(viewLifecycleOwner) { checked ->
            binding.themeSwitch.isChecked = checked
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.changeTheme(isChecked)
        }

        binding.shareLayout.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.sendApp))
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_header)))
        }

        binding.supportLayout.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(R.string.user_email))
                putExtra(Intent.EXTRA_SUBJECT, R.string.mail_subject)
                putExtra(Intent.EXTRA_TEXT, R.string.mail_body)
            }
            startActivity(supportIntent)
        }

        binding.userAgreementLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.user_agreement))
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}