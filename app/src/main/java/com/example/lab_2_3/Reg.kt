package com.example.lab_2_3

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_reg.*

class Reg : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)
    }

    @SuppressLint("CheckResult")
    fun reg(view: View) {

        //Создаем массив данных забитых пользователем и записываем в БД о пользователях
        val users = UsersTable(
            nameText.text.toString(),
            surnameText.text.toString(),
            patText.text.toString(),
            loginText.text.toString(),
            passText.text.toString()
        )

        Observable.fromCallable {

            val db = TestDatabase.getAppDataBase(context = this)
            val userDao = db?.readoutDAO()
            userDao!!.addUser(users)

        }.subscribeOn(Schedulers.io())

            .observeOn(AndroidSchedulers.mainThread())

            .subscribe {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }


    }

}
