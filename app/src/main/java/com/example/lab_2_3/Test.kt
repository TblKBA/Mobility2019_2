package com.example.lab_2_3

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.example.lab_2_3.R.layout.activity_test
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_reg.*
import kotlinx.android.synthetic.main.activity_test.*
import java.util.ArrayList

class Test : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_test)

        //Счетчики для подсчета правильных тестов
        var num_test = 0
        var truetest = 0

        //Загружаем из бд при открытии первый вопрос
        val ab = Observable.fromCallable {

            val db = TestDatabase.getAppDataBase(context = this)
            val userDao = db?.readoutDAO()

            userDao!!.getallTest

        }.subscribeOn(Schedulers.io())

            .observeOn(AndroidSchedulers.mainThread())

            .subscribe {

                if (it != null)

                    for (item in it) {
                        Num.setText("1 из 10")
                        Ques.setText(it[0].ques)
                        Var1.setText(it[0].var1)
                        Var2.setText(it[0].var2)
                        Var3.setText(it[0].var3)
                        Accept.setText(it[0].approve.toString())
                    }

            }

//При нажатии на кнопку записываем ответ в БД ответов, вместе с вопросом и заполняем новым тестом
        Button.setOnClickListener {
            var id: Int = radio_group.checkedRadioButtonId
            if (id != -1) {
                num_test += 1
                val radio: RadioButton = findViewById(radio_group.checkedRadioButtonId)
                if (radio.text.toString() == Accept.text.toString()) {
                    truetest += 1
                }
                if (num_test != 10) {

                    val ac = Observable.fromCallable {

                        val user = pref.getString("user", "")

                        val db = TestDatabase.getAppDataBase(context = this)
                        val AnswerDao = db?.readoutDAO()
                        AnswerDao!!.addAnswer(AnswerTable(user, Ques.text.toString(), radio.text.toString()))
                    }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {}

                    edit_test(num_test)
                } else {
                    open_result(truetest)
                }
            } else {
                Toast.makeText(
                    applicationContext, "Ничего не выбрано",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun radio_button_click(view: View) {
        val radio: RadioButton = findViewById(radio_group.checkedRadioButtonId)
    }

    //Перезаполнение текущего теста после нажатия на кнопку
    @SuppressLint("CheckResult")
    fun edit_test(id: Int) {
        Observable.fromCallable {

            val db = TestDatabase.getAppDataBase(context = this)
            val userDao = db?.readoutDAO()
            userDao!!.getallTest
        }.subscribeOn(Schedulers.io())

            .observeOn(AndroidSchedulers.mainThread())

            .subscribe {

                if (it != null)

                    for (item in it) {
                        Num.setText("${id + 1} из 10")
                        Ques.setText(it[id].ques)
                        Var1.setText(it[id].var1)
                        Var2.setText(it[id].var2)
                        Var3.setText(it[id].var3)
                        Accept.setText(it[id].approve.toString())
                    }

            }
    }

    //Открытие результатов после ответа на последний тест
    fun open_result(truetest: Int) {

        val getuser = pref.getString("user", "")

        val ac = Observable.fromCallable {

            val db = TestDatabase.getAppDataBase(context = this)
            val userDao = db?.readoutDAO()
            userDao!!.getReadoutByLogin(getuser)

        }.subscribeOn(Schedulers.io())

            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                Observable.fromCallable {
                    for (item in it) {
                        val percent = "${truetest * 10}%"
                        val db = TestDatabase.getAppDataBase(context = this)
                        val resultDao = db?.readoutDAO()
                        resultDao!!.addResult(ResultTable(it[0].login, percent, it[0].name, it[0].surname, it[0].pat))
                    }
                }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
            .subscribe {
                val intent = Intent(this, Result::class.java)
                intent.putExtra("truetest", truetest)
                startActivity(intent)
            }
    }
}
