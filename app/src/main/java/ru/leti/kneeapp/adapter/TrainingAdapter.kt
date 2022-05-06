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

class TrainingAdapter (context: Context,
                       private var trainings: List<Training>,
                       private val clickListener: (position: Int) -> Unit) :
    RecyclerView.Adapter<TrainingAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemCount(): Int = trainings.size

    private fun getItem(position: Int): Training = trainings[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_training, parent, false), clickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setItems(trainings: List<Training>) {
        this.trainings = trainings
    }

    class ViewHolder(itemView: View,
                     listener: (position: Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.preview_image)
        private val title: TextView = itemView.findViewById(R.id.exercise_name)
        private val passMark: TextView = itemView.findViewById(R.id.pass_mark)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener(position)
                }
            }
        }

        fun bind(training:Training) {
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