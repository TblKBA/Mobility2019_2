package com.example.lab_2_3

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_reg.*
import kotlinx.android.synthetic.main.activity_reg.loginText
import kotlinx.android.synthetic.main.activity_reg.passText

lateinit var pref: SharedPreferences

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        pref = getSharedPreferences("Settings", MODE_PRIVATE)
    }

    //Получаем из БД пользователя согластно введеному нику и проверяем пороль на сходство
    @SuppressLint("CheckResult")
    fun login(view: View) {

        Observable.fromCallable {

            val user = loginText.text.toString()

            val db = TestDatabase.getAppDataBase(context = this)
            val userDao = db?.readoutDAO()
            userDao!!.getReadoutByLogin(user)

        }.subscribeOn(Schedulers.io())

            .observeOn(AndroidSchedulers.mainThread())

            .subscribe {

                if (it != null)

                    for (item in it) {

                        val pass = passText.text.toString()

                        if (pass == it[0].pass) {
                            //Если пароль совпал и юзер зашел, записываем текущего юзера в КЭШ
                            val editor = pref.edit()
                            editor.putString("user", loginText.text.toString())
                            editor.apply()

                            val intent = Intent(this, HomeScreen::class.java)
                            startActivity(intent)
                        }

                    }

            }
    }
}
