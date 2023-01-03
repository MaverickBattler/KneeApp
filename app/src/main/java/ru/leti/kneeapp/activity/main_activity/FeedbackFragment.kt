package ru.leti.kneeapp.activity.main_activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ru.leti.kneeapp.activity.ContactDoctorActivity
import ru.leti.kneeapp.activity.RegistrationActivity
import ru.leti.kneeapp.databinding.FragmentFeedbackBinding

class FeedbackFragment : Fragment() {

    private var _binding: FragmentFeedbackBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val button: Button = binding.informDoctorOfEmergencyButton
        button.setOnClickListener {
            openContactDoctorActivity()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openContactDoctorActivity() {
        val intent = Intent(activity, ContactDoctorActivity::class.java)
        startActivity(intent)
    }
}