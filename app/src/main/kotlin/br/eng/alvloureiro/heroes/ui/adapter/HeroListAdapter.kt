package br.eng.alvloureiro.heroes.ui.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import br.eng.alvloureiro.heroes.R
import br.eng.alvloureiro.heroes.extensions.inflate
import br.eng.alvloureiro.heroes.extensions.loadHeroImageFromUrl
import br.eng.alvloureiro.heroes.network.data.Character
import kotlinx.android.synthetic.main.hero_listitem.view.*


class HeroListAdapter(listener: (Character) -> Unit): RecyclerView.Adapter<HeroListAdapter.HeroViewHolder>() {
    private var mHeroList: MutableList<Character> = mutableListOf()
    private val mListener: (Character) -> Unit = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        return HeroViewHolder(parent.inflate(R.layout.hero_listitem))
    }

    override fun getItemCount(): Int = mHeroList.size

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        holder.bind(mHeroList.get(position), mListener)
    }

    fun addHeroList(heroes: List<Character>) {
        var initPosition = 0
        if (mHeroList.isNotEmpty()) {
            initPosition = mHeroList.lastIndex.plus(1)
        }

        mHeroList.plusAssign(heroes)
        notifyItemRangeChanged(initPosition, mHeroList.size.plus(1))
    }

    class HeroViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val mHeroImage = itemView.heroImage
        private val mHeroName = itemView.heroName
        private val mFavoriteBtn = itemView.favoriteButton

        fun bind(item: Character, listener: (Character) -> Unit) = with(itemView) {
            mHeroImage.loadHeroImageFromUrl(item.thumbnail?.path)
            mHeroName.text = context.getString(R.string.label_hero_name, item.name)

            setOnClickListener {
                listener(item)
            }
        }
    }
}
