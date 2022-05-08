package ru.leti.kneeapp.activity.main_activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.leti.kneeapp.activity.ExerciseActivity
import ru.leti.kneeapp.adapter.TrainingAdapter
import ru.leti.kneeapp.data.Datasource
import ru.leti.kneeapp.databinding.FragmentTrainingBinding


class TrainingFragment : Fragment() {

    private var _binding: FragmentTrainingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: TrainingAdapter

    private var selectedItem: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTrainingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val exercises = Datasource().getExercisesList(root.context)
        adapter = TrainingAdapter(root.context, exercises) { position ->
            selectedItem = position
            val exercise = exercises[position]
            val intent = Intent(context, ExerciseActivity::class.java)
            intent.putExtra("exercise", exercise)
            intent.putExtra("exercise_number", position)
            startActivity(intent)
        }
        val list = binding.exerciseList
        list.adapter = adapter

        return root
    }

    override fun onResume() {
        super.onResume()
        if (selectedItem != -1) { //возвращение из элемента recyclerview
            val root: View = binding.root
            val exercises = Datasource().getExercisesList(root.context)
            adapter.setItems(exercises)
            adapter.notifyItemChanged(selectedItem)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}