package ru.zemlyanaya.getonbus.routing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fav_route_card.view.*
import ru.zemlyanaya.getonbus.R
import ru.zemlyanaya.getonbus.database.FavRoute

class FavRoutesRecyclerViewAdapter internal constructor():
    RecyclerView.Adapter<FavRoutesRecyclerViewAdapter.RouteCardViewHolder>() {

    var favRoutes = ArrayList<FavRoute>()
    //private lateinit var listener: View.OnClickListener


    inner class RouteCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val routeName: TextView = itemView.routeName
        val routeDestination: TextView = itemView.routeDestination
        val routeIcon: ImageButton = itemView.routeIcon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteCardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.fav_route_card, parent, false)
        return RouteCardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RouteCardViewHolder, position: Int) {
        val current = favRoutes[position]
        holder.routeName.text = current.name
        holder.routeDestination.text = current.destination
        holder.routeIcon.setImageResource(parseIcon(current.icon))
    }

//    internal fun addOnClickListener(listener: View.OnClickListener){
//        this.listener = listener
//    }

    private fun parseIcon(iconName: String?): Int{
        return when(iconName){
            "home" -> R.drawable.ic_home
            "work" -> R.drawable.ic_work
            else -> R.drawable.ic_heart
        }
    }

    internal fun setData(persons: ArrayList<FavRoute>) {
        this.favRoutes = persons
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        favRoutes.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(item: FavRoute, position: Int) {
        favRoutes.add(position, item)
        notifyItemInserted(position)
    }

    fun getData(): ArrayList<FavRoute> {
        return favRoutes
    }

    override fun getItemCount() = favRoutes.size
}