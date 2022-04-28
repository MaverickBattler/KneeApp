package ru.leti.kneeapp.activity.main_activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import ru.leti.kneeapp.activity.TestPageActivity
import ru.leti.kneeapp.databinding.FragmentOksBinding

class OksFragment : Fragment() {

    private var _binding: FragmentOksBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val startOksButton: Button = binding.buttonRunOKS

        startOksButton.setOnClickListener {
            openOksTestActivity()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openOksTestActivity() {
        val intent = Intent(activity, TestPageActivity::class.java)
        startActivity(intent)
    }
}