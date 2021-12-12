package com.example.kedditbysteps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kedditbysteps.common.adapters.NewsLoadingAdapter
import com.example.kedditbysteps.common.inflate
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.news_fragment.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class NewsFragment: RxBaseFragment() {
    private val newsManager by lazy { NewsManager() }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.news_fragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        news_list?.setHasFixedSize(true)
        news_list?.layoutManager = LinearLayoutManager(context)
        initAdapter()

        if (savedInstanceState == null) {
            requestNews()
//            (news_list.adapter as NewsLoadingAdapter).addNews(news)
        }
    }

    private fun initAdapter() {
        if (news_list.adapter == null) {
            news_list.adapter = NewsLoadingAdapter()
        }
    }

    private fun requestNews() {
        val subscription = newsManager.getNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            retrievedNews -> (news_list.adapter as NewsLoadingAdapter).addNews(retrievedNews)
                        },
                        {
                            e -> Snackbar.make(news_list, e.message ?: "", Snackbar.LENGTH_LONG).show()
                        }
                )
        subscriptions.add(subscription)
    }
}