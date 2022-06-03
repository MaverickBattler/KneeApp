package ru.leti.kneeapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.leti.kneeapp.R
import ru.leti.kneeapp.data.Training

//Адаптер для RecyclerView, показывающего список тренировок
class TrainingAdapter (context: Context,
                       //Список тренировок
                       private var trainings: List<Training>,
                       //OnClickListener по элементу RecyclerView на определенной position
                       private val clickListener: (position: Int) -> Unit) :
    RecyclerView.Adapter<TrainingAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    //Переопределение необходимых методов
    override fun getItemCount(): Int = trainings.size

    private fun getItem(position: Int): Training = trainings[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_training, parent, false), clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    //Установка нового спика тренировок в RecyclerView
    fun setItems(trainings: List<Training>) {
        this.trainings = trainings
    }
    //Класс, хранящий ссылки на все View внутри элемента списка
    class ViewHolder(
        //View ViewHolder'а
        itemView: View,
        //Listener (слушатель)
        listener: (position: Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        //Картинка с упражнением
        private val image: ImageView = itemView.findViewById(R.id.preview_image)
        //Название упражнения
        private val title: TextView = itemView.findViewById(R.id.exercise_name)
        //Отметка о прохождении упражнения
        private val passMark: TextView = itemView.findViewById(R.id.pass_mark)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    //Активация определенного Listener
                    listener(position)
                }
            }
        }
        // Установка значений всем View внутри элемента списка
        fun bind(
            //Тренировка
            training:Training
        ) {
            //Получение контекста
            val appContext = itemView.context
            image.setImageResource(training.trainingPreview)
            title.text = appContext.getString(training.nameStringResourceId)
            passMark.text = training.passMark
            when (training.passMark) {
                appContext.getString(R.string.completed) -> {
                    //Упражнение выполнено сегодня
                    passMark.setTextColor(
                        ContextCompat.getColor(
                            appContext,
                            R.color.green_completed
                        )
                    )
                }
                itemView.context.getString(R.string.not_allowed_to) -> {
                    //Если сначала нужно заполнить анкету OKS
                    passMark.setTextColor(
                        ContextCompat.getColor(
                            appContext,
                            R.color.light_gray
                        )
                    )
                }
                else -> {
                    //Упражнение нужно выполнить
                    passMark.setTextColor(
                        ContextCompat.getColor(
                            appContext,
                            R.color.black
                        )
                    )
                }
            }
        }
    }
}