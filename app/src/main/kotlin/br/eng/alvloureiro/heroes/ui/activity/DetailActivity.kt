package br.eng.alvloureiro.heroes.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.ViewTreeObserver
import br.eng.alvloureiro.heroes.R
import br.eng.alvloureiro.heroes.extensions.app
import br.eng.alvloureiro.heroes.extensions.getParam
import br.eng.alvloureiro.heroes.extensions.loadHeroImageFromUrl
import br.eng.alvloureiro.heroes.network.data.Character
import br.eng.alvloureiro.heroes.ui.fragment.DetailFragment
import kotlinx.android.synthetic.main.detail_activity.*


class DetailActivity: AppCompatActivity() {

    private val mCharacter by lazy {
        getParam<Character>(MODEL_DATA)
    }

    private val mShareTransitionName by lazy {
        getParam<String>(SHARED_TRANSITION_NAME)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        app.component()?.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        postponeEnterTransition()
        heroDetailName.text = getString(R.string.label_hero_name, mCharacter.name)
        heroImage.transitionName = mShareTransitionName
        heroImage.loadHeroImageFromUrl(mCharacter.thumbnail?.path)
        heroImage.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                heroImage.viewTreeObserver.removeOnPreDrawListener(this)
                startPostponedEnterTransition()
                return true
            }
        })

        setViewPager()
        tabResource.setupWithViewPager(heroDetailViewPager)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                finishAfterTransition()
                onBackPressed()
                return true
            }
        }
        return true
    }

    override fun onPause() {
        finishAfterTransition()
        super.onPause()
    }

    private fun setViewPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(
            createDetailFragment {
                putSerializable(MODEL_DATA, mCharacter)
                putString(DetailFragment.FRAGMENT_TYPE, DetailFragment.COMICS)
            }, DetailFragment.COMICS)
        adapter.addFragment(
            createDetailFragment {
                putSerializable(MODEL_DATA, mCharacter)
                putString(DetailFragment.FRAGMENT_TYPE, DetailFragment.EVENTS)
            }, DetailFragment.EVENTS)
        adapter.addFragment(
            createDetailFragment {
                putSerializable(MODEL_DATA, mCharacter)
                putString(DetailFragment.FRAGMENT_TYPE, DetailFragment.STORIES)
            }, DetailFragment.STORIES)
        adapter.addFragment(
            createDetailFragment {
                putSerializable(MODEL_DATA, mCharacter)
                putString(DetailFragment.FRAGMENT_TYPE, DetailFragment.SERIES)
            }, DetailFragment.SERIES)

        heroDetailViewPager.adapter = adapter
    }

    private fun createDetailFragment(init: Bundle.() -> Unit): Fragment {
        val bundle = Bundle()
        bundle.init()
        val fragment = DetailFragment()
        fragment.arguments = bundle
        return fragment
    }

    inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList: MutableList<Fragment> = mutableListOf()
        private val mFragmentListTitle: MutableList<String> = mutableListOf()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentListTitle[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentListTitle.add(title)
        }
    }

    companion object {
        const val MODEL_DATA = "MODEL_DATA"
        const val SHARED_TRANSITION_NAME = "SHARED_TRANSITION_NAME"
    }
}
