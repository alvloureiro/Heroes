package br.eng.alvloureiro.heroes.ui.activity

import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import br.eng.alvloureiro.heroes.R
import br.eng.alvloureiro.heroes.extensions.*
import br.eng.alvloureiro.heroes.network.data.Character
import br.eng.alvloureiro.heroes.network.data.ResultData
import br.eng.alvloureiro.heroes.ui.adapter.HeroListAdapter
import br.eng.alvloureiro.heroes.ui.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mViewModel: MainViewModel

    private var mLoadingScrolling = true

    private var mCurrentOffset = 0

    private var mMaxTotal = 0

    private var mDataCount = 0

    private lateinit var mSearchView: SearchView

    private val mHeroListAdapter = HeroListAdapter { hero, sharedView ->
        Log.d("MainActivity", "Launch detail activity")
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedView, ViewCompat.getTransitionName(sharedView))
        launch<DetailActivity>(options = options.toBundle()) {
            putExtra(DetailActivity.MODEL_DATA, hero)
            putExtra(DetailActivity.SHARED_TRANSITION_NAME, ViewCompat.getTransitionName(sharedView))
        }
    }

    private val success: (ResultData<Character>) -> Unit = { data ->
        if (data.results?.isNotEmpty()!!) {
            progressBar.hide()
            mHeroListAdapter.addHeroList(data.results)
            mMaxTotal = data.total ?: -1
            mDataCount = data.count ?: -1

            if (!mLoadingScrolling) {
                mLoadingScrolling = true
            }
        } else {
            progressBar.hide()
            btnRefetch.show()
        }
    }

    private val error: (Throwable) -> Unit = {
        Log.d("MainActivity", it.message)
        progressBar.hide()
        btnRefetch.show()
    }

    private val mLayoutManager: LinearLayoutManager by lazy {
        GridLayoutManager(this, 2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "------------> on create")
        app.component()?.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            .plus(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT

        setSupportActionBar(mainToolbar)
        supportActionBar?.title = getString(R.string.app_name)
        progressBar.show()

        mViewModel.runGetHeroesList(success, error, mCurrentOffset)

        withListView<RecyclerView> {
            layoutManager = mLayoutManager
            adapter = mHeroListAdapter
            addOnScrollListener(ScrollListener())
        }

        btnRefetch?.setOnClickListener {
            progressBar.show()

            mViewModel.runGetHeroesList(success, error, mCurrentOffset)

            it.hide()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mHeroListAdapter.itemCount == 1) {
            mHeroListAdapter.disposeHeroList()
            mHeroListAdapter.notifyDataSetChanged()
            mViewModel.runGetHeroesList(success, error, 0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        mSearchView = menu?.findItem(R.id.search)?.actionView as SearchView
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mHeroListAdapter.disposeHeroList()
                mHeroListAdapter.notifyDataSetChanged()
                progressBar.show()
                mViewModel.searchSuperHero(query!!, success, error)
                mSearchView.clearFocus()
                mSearchView.setQuery("", false)
                mSearchView.setIconifiedByDefault(true)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                Log.d("MainActivity", "back clicked")
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (mSearchView.isIconified) {
            mSearchView.setIconifiedByDefault(true)
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        mViewModel.cancelAllAsync()
        mViewModel.cancelAllCoroutines()
        heroList?.clearOnScrollListeners()
        super.onDestroy()
    }

    inner class ScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            if (dy > 0) {
                val visibleItems = mLayoutManager.childCount
                val totalItems = mLayoutManager.itemCount
                val pastItems = mLayoutManager.findFirstVisibleItemPosition()

                if (mLoadingScrolling) {
                    if ((visibleItems + pastItems) >= totalItems) {
                        mLoadingScrolling = false

                        if (mCurrentOffset < mMaxTotal) {
                            mCurrentOffset += mDataCount
                            progressBar.show()
                            mViewModel.runGetHeroesList(success, error, mCurrentOffset)
                        } else {
                            Toast.makeText(
                                recyclerView?.context,
                                recyclerView?.context?.getString(R.string.label_toast_reach_end_list_text),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }
}
