package ru.zemlyanaya.getonbus.trip

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.bus_card.view.*
import ru.zemlyanaya.getonbus.R

class TripBusRecyclerAdapter constructor(private val onCardClickListener: (String) -> Unit):
    RecyclerView.Adapter<TripBusRecyclerAdapter.RouteCardViewHolder>() {

        var routes = emptyList<String>()

        inner class RouteCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val route: TextView = itemView.textNum
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteCardViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.bus_card, parent, false)
            return RouteCardViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: RouteCardViewHolder, position: Int) {
            val current = routes[position]
            holder.itemView.setOnClickListener { onCardClickListener.invoke(current) }
            holder.route.text = current
        }

        internal fun setData(routes: List<String>) {
            this.routes = routes
            notifyDataSetChanged()
        }

        fun getData(): List<String> {
            return routes
        }

        override fun getItemCount() = routes.size

    }