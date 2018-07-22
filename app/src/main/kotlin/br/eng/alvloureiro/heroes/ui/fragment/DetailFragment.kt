package br.eng.alvloureiro.heroes.ui.fragment

import android.graphics.ColorSpace
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.eng.alvloureiro.heroes.R
import br.eng.alvloureiro.heroes.extensions.app
import br.eng.alvloureiro.heroes.extensions.hide
import br.eng.alvloureiro.heroes.extensions.show
import br.eng.alvloureiro.heroes.network.data.Character
import br.eng.alvloureiro.heroes.network.data.ModelCommonData
import br.eng.alvloureiro.heroes.network.data.ResultData
import br.eng.alvloureiro.heroes.ui.activity.DetailActivity
import br.eng.alvloureiro.heroes.ui.adapter.EntityListAdapter
import br.eng.alvloureiro.heroes.ui.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.detail_fragment.*
import javax.inject.Inject


class DetailFragment: Fragment() {

    @Inject
    lateinit var mViewModel: DetailViewModel

    private val mLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    private val mCharacter by lazy {
        arguments?.getSerializable(DetailActivity.MODEL_DATA) as Character
    }

    private val mFragmentType by lazy {
        arguments?.getString(FRAGMENT_TYPE) as String
    }

    private val mEntityListAdapter = EntityListAdapter()

    private val fail: (Throwable) -> Unit = {
        Log.d("Detail Fragment", it.message)
        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
        progressBarDetail.hide()
        entityNoData.show()
    }

    private val success: (ResultData<ModelCommonData>?) -> Unit = { data ->
        if(data?.results != null) {
            if (data.results?.isNotEmpty()!! && data.results?.size!! > 3) {
                mEntityListAdapter.addEntityList(data.results?.subList(0, 2)!!)
            } else if (data.results?.isNotEmpty()!! && data.results?.size in 1..2) {
                mEntityListAdapter.addEntityList(data.results!!)
            }
        } else {
            entityNoData.show()
        }

        progressBarDetail.hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        activity?.app?.component()?.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        entities.layoutManager = mLayoutManager
        entities.adapter = mEntityListAdapter

        progressBarDetail.show()

        when(mFragmentType) {
            COMICS -> mViewModel.runGetCharacterComics(mCharacter, success, fail)
            EVENTS -> mViewModel.runGetCharacterEvents(mCharacter, success, fail)
            STORIES -> mViewModel.runGetCharacterStories(mCharacter, success, fail)
            SERIES -> mViewModel.runGetCharacterSeries(mCharacter, success, fail)
        }
    }

    override fun onDestroyView() {
        entities.layoutManager = null
        entities.adapter = null
        mViewModel.cancelAllAsync()
        mViewModel.cancelAllCoroutines()
        super.onDestroyView()
    }

    companion object DetailType {
        const val COMICS = "comics"
        const val EVENTS = "events"
        const val STORIES = "stories"
        const val SERIES = "series"
        const val FRAGMENT_TYPE = "FRAGMENT_TYPE"
    }

}
