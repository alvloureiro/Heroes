package br.eng.alvloureiro.heroes.ui.adapter

import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import br.eng.alvloureiro.heroes.R
import br.eng.alvloureiro.heroes.extensions.inflate
import br.eng.alvloureiro.heroes.extensions.loadHeroImageFromUrl
import br.eng.alvloureiro.heroes.network.data.Character
import kotlinx.android.synthetic.main.hero_listitem.view.*


class HeroListAdapter(listener: (Character, AppCompatImageView) -> Unit): RecyclerView.Adapter<HeroListAdapter.HeroViewHolder>() {
    private var mCharacterList: MutableList<Character> = mutableListOf()
    private val mListener: (Character, AppCompatImageView) -> Unit = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        return HeroViewHolder(parent.inflate(R.layout.hero_listitem))
    }

    override fun getItemCount(): Int = mCharacterList.size

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        holder.bind(mCharacterList.get(position), mListener)
    }

    fun addHeroList(heroes: List<Character>?) {
        var initPosition = 0
        if (mCharacterList.isNotEmpty()) {
            initPosition = mCharacterList.lastIndex.plus(1)
        }

        heroes?.let {
            mCharacterList.plusAssign(it)
        }

        notifyItemRangeChanged(initPosition, mCharacterList.size.plus(1))
    }

    class HeroViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val mHeroImage = itemView.heroImage
        private val mHeroName = itemView.heroName
        private val mFavoriteBtn = itemView.favoriteButton

        fun bind(characterItem: Character, listener: (Character, AppCompatImageView) -> Unit) = with(itemView) {
            mHeroImage.loadHeroImageFromUrl(characterItem.thumbnail?.path)
            mHeroName.text = context.getString(R.string.label_hero_name, characterItem.name)
            ViewCompat.setTransitionName(mHeroImage, characterItem.name)

            setOnClickListener {
                listener(characterItem, mHeroImage)
            }
        }
    }
}
