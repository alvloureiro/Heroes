package br.eng.alvloureiro.heroes.ui.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import br.eng.alvloureiro.heroes.R
import br.eng.alvloureiro.heroes.extensions.inflate
import br.eng.alvloureiro.heroes.network.data.ModelCommonData
import kotlinx.android.synthetic.main.entity_listitem.view.*


class EntityListAdapter: RecyclerView.Adapter<EntityListAdapter.EntityViewHolder>() {
    private var mEntityList: MutableList<ModelCommonData> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        return EntityViewHolder(parent.inflate(R.layout.entity_listitem))
    }

    override fun getItemCount(): Int = mEntityList.size

    override fun onBindViewHolder(holder:EntityViewHolder , position: Int) {
        holder.bind(mEntityList[position])
    }

    fun addEntityList(entities: MutableList<ModelCommonData>) {
        var initPosition = 0
        if (mEntityList.isNotEmpty()) {
            initPosition = mEntityList.lastIndex.plus(1)
        }
        mEntityList.plusAssign(entities)
        notifyItemRangeChanged(initPosition, mEntityList.size)
    }

    class EntityViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val mEntityTitle = itemView.entityTitle
        private  val mEntityDescription = itemView.entityDescription

        fun bind(data: ModelCommonData?) = with(itemView) {
            data?.title?.let {
                Log.d("EntityListAdapter title", it)
                mEntityTitle.text = resources.getString(R.string.label_entity_title, data?.title)
            }

            data?.description?.let {
                Log.d("EntityListAdapter desc", it)

                mEntityDescription.text = resources.getString(R.string.label_entity_description, it)
            }
        }
    }
}
