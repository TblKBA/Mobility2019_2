package com.example.lab_2_3

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_rating.*

class Rating : AppCompatActivity(), ItemClick {

    //Передача клика по пользователю
    override fun onItemClick(pos: Int) {
        val userName = ratings[pos].login
        val intent = Intent(this, DetalRating::class.java)
        intent.putExtra("user", userName)
        startActivity(intent)
    }

    var ratings: ArrayList<Ratingg> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)
        addRating()
    }

    //Вывод всех результатов из базы рейтинга
    fun addRating() {

        val af = Observable.fromCallable {

            val db = TestDatabase.getAppDataBase(context = this)
            val ratingDao = db?.readoutDAO()
            ratingDao!!.getallResult

        }.subscribeOn(Schedulers.io())

            .observeOn(AndroidSchedulers.mainThread())

            .subscribe {

                if (it != null) {

                    for (item in it) {
                        ratings.add(Ratingg(item.login.toString(), item.result.toString()))
                    }
                    ratingRecyclerView.layoutManager = LinearLayoutManager(this)
                    ratingRecyclerView.adapter = RatingAdapter(ratings, this, this)
                }
            }
    }

}
