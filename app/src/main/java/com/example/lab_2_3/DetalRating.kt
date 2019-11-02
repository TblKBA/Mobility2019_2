package com.example.lab_2_3

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detal_rating.*

class DetalRating : AppCompatActivity(), ItemClick {

    //Событие клика пока не актуально всвязи с текущим ТЗ
    override fun onItemClick(pos: Int) {
    }

    //Заполняем имя фамилию отчество и результат из БД результатов
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detal_rating)

        val ab = Observable.fromCallable {

            val user = intent.getStringExtra("user")

            val db = TestDatabase.getAppDataBase(context = this)
            val resultDao = db?.readoutDAO()
            resultDao!!.getResultByLogin(user)

        }.subscribeOn(Schedulers.io())

            .observeOn(AndroidSchedulers.mainThread())

            .subscribe {

                if (it != null)

                    for (item in it) {

                        Fio.setText(it[0].surname.toString() + " " + it[0].name.toString() + " " + it[0].pat.toString())
                        Rating.setText(it[0].result.toString())
                        Answ()
                    }

            }


    }

    //Заполняем вопросы и ответы пользователя из БД ответов
    @SuppressLint("CheckResult")
    fun Answ() {
        var answers: ArrayList<Answerss> = ArrayList()
        Observable.fromCallable {

            val user = intent.getStringExtra("user")

            val db = TestDatabase.getAppDataBase(context = this)
            val answDao = db?.readoutDAO()
            answDao!!.getAnswerByLogin(user)

        }.subscribeOn(Schedulers.io())

            .observeOn(AndroidSchedulers.mainThread())

            .subscribe {

                if (it != null) {

                    for (item in it) {
                        answers.add(Answerss(item.ques.toString(), item.answ.toString()))
                    }

                    DetalratingRecyclerView.layoutManager = LinearLayoutManager(this)
                    DetalratingRecyclerView.adapter = DetalRatingAdapter(answers, this, this)
                }
            }
    }
}

