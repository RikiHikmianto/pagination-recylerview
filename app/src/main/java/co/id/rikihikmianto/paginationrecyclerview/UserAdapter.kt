package co.id.rikihikmianto.paginationrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.id.rikihikmianto.paginationrecyclerview.databinding.ItemUserBinding
import co.id.rikihikmianto.paginationrecyclerview.model.DataItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class UserAdapter(var list: MutableList<DataItem?>?) :
    RecyclerView.Adapter<UserAdapter.Viewholder>() {
    private var binding: ItemUserBinding? = null

    inner class Viewholder(binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        var image = binding.imvAvatar
        fun bind(dataItem: DataItem) {
            with(itemView) {
                binding?.tvEmail!!.text = dataItem.email
                binding?.tvName!!.text = dataItem.firstName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        return Viewholder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        list!![position].let {
            holder.bind(it!!)
        }
        Glide.with(holder.itemView.context).load(list!![position]?.avatar)
            .transform(CenterCrop(), RoundedCorners(25)).diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).into(holder.image)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    fun addList(items: List<DataItem?>?) {
        list?.addAll(items!!)
        notifyDataSetChanged()
    }

    fun clear() {
        list?.clear()
        notifyDataSetChanged()
    }
}
