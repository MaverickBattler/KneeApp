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

//Фрагмент, соответствующий странице со списком тренировок
class TrainingFragment : Fragment() {
    //Экземпляр FragmentTrainingBinding
    private var _binding: FragmentTrainingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    // Адаптер для RecyclerView
    private lateinit var adapter: TrainingAdapter
    // Индекс элемента списка, на который нажал пользователь
    private var selectedItem: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainingBinding.inflate(inflater, container, false)
        //Получение binding.root
        val root: View = binding.root
        // Получение списка упражнений типа List<Training>
        val exercises = Datasource().getExercisesList(root.context)
        //Создание адаптера
        adapter = TrainingAdapter(root.context, exercises) { position ->
            selectedItem = position
            val exercise = exercises[position]
            //Открытие ExerciseActivity по нажатию
            val intent = Intent(context, ExerciseActivity::class.java)
            intent.putExtra("exercise", exercise)
            intent.putExtra("exercise_number", position)
            startActivity(intent)
        }
        //RecyclerView
        val list = binding.exerciseList
        //Установка адаптера
        list.adapter = adapter

        return root
    }

    override fun onResume() {
        super.onResume()
        if (selectedItem != -1) { // при возвращении из ExerciseActivity
            //Получение binding.root
            val root: View = binding.root
            //Список упражнений
            val exercises = Datasource().getExercisesList(root.context)
            adapter.setItems(exercises)
            // обновление списка элементов для изменения отметки о выполнении упражения
            adapter.notifyItemChanged(selectedItem)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}