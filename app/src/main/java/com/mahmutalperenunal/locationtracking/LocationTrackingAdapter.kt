package com.mahmutalperenunal.locationtracking

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class LocationTrackingAdapter(private val context: Context): RecyclerView.Adapter<LocationTrackingAdapter.MyViewHolder>(),
    Filterable {

    private var userList = emptyList<Users>()
    private var userListFiltered = emptyList<Users>()


    /**
     * item click
     */
    private lateinit var userListener: OnItemClickListener

    interface OnItemClickListener { fun onItemClick(position: Int) }

    fun setOnItemClickListener(listener: OnItemClickListener) { userListener = listener }


    inner class MyViewHolder(itemView: View, listener: OnItemClickListener): RecyclerView.ViewHolder(itemView) {
        val firstName: TextView = itemView.findViewById(R.id.userList_userName_textView)
        val lastName: TextView = itemView.findViewById(R.id.userList_userSurname_textView)
        val cardView: MaterialCardView = itemView.findViewById(R.id.userList_cardView)

        //navigate gradeActivity
        init { itemView.setOnClickListener { listener.onItemClick(adapterPosition) } }
    }


    override fun onCreateViewHolder(student: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(student.context).inflate(R.layout.user_list, student, false), userListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //set animation
        holder.cardView.animation = AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation)

        holder.firstName.text = userList[position].name
        holder.lastName.text = userList[position].surname

    }

    override fun getItemCount(): Int {
        return userList.size
    }


    //set data
    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<Users>){
        userList = newList
        userListFiltered = newList
        notifyDataSetChanged()
    }


    override fun getFilter(): Filter {

        val filter = object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {

                val filterResults = FilterResults()
                if (p0 == null || p0.isEmpty()) {
                    filterResults.values = userListFiltered
                    filterResults.count = userListFiltered.size
                } else {
                    val searchChar = p0.toString().toLowerCase()
                    val filteredResults = ArrayList<Users>()

                    for (student in userListFiltered) {
                        if (student.name.toLowerCase().contains(searchChar)
                            || student.surname.toLowerCase().contains(searchChar)
                            || student.name.plus(" ").plus(student.surname).toLowerCase().contains(searchChar)) {
                            filteredResults.add(student)
                        }
                    }

                    filterResults.values = filteredResults
                    filterResults.count = filteredResults.size

                }

                return filterResults

            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                userList = p1!!.values as List<Users>
                notifyDataSetChanged()
            }

        }

        return filter

    }


}