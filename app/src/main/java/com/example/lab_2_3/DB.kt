package com.example.lab_2_3

import android.content.Context
import androidx.annotation.NonNull
import androidx.room.*
import androidx.room.RoomDatabase.Callback
import androidx.sqlite.db.SupportSQLiteDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_reg.*

//Обьявление дата класов для таблиц БД
@Entity
data class UsersTable(
    @ColumnInfo var name: String?,
    @ColumnInfo var surname: String?,
    @ColumnInfo var pat: String?,
    @ColumnInfo var login: String?,
    @ColumnInfo var pass: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

@Entity
data class TestTable(
    @ColumnInfo var ques: String?,
    @ColumnInfo var var1: String?,
    @ColumnInfo var var2: String?,
    @ColumnInfo var var3: String?,
    @ColumnInfo var approve: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

@Entity
data class ResultTable(
    @ColumnInfo var login: String?,
    @ColumnInfo var result: String?,
    @ColumnInfo var name: String?,
    @ColumnInfo var surname: String?,
    @ColumnInfo var pat: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

@Entity
data class AnswerTable(
    @ColumnInfo var login: String?,
    @ColumnInfo var ques: String?,
    @ColumnInfo var answ: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

@Dao
interface ReadoutModelDao {
    // методы для юзеров
    @get:Query("select * from UsersTable")            // "get:" означает применение аннотации "Query" к геттеру (функцию геттера для переменной allReadoutItems вручную не пишем)
    val allReadoutItems: List<UsersTable>             // Обёртываем возвращаемое значение LiveData<...> чтобы отслеживать изменения в базе. При изменении данных будут рассылаться уведомления

    @Query("select * from UsersTable where id = :id")
    fun getReadoutById(id: Long): UsersTable

    @Query("select * from UsersTable where login = :login")
    fun getReadoutByLogin(login: String): List<UsersTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(readoutModel: UsersTable)

    @Update
    fun updateReadout(readoutModel: UsersTable)

    @Delete
    fun deleteReadout(readoutModel: UsersTable)

    //методы для теста
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTest(readoutModel: TestTable)

    @get:Query("select * from TestTable")
    val getallTest: List<TestTable>

    @Query("select * from TestTable where id = :id")
    fun getTestById(id: Long): TestTable

    @Query("DELETE FROM TestTable")
    fun cleantable()

    //Методы для Результатов
    @Query("select * from ResultTable where login = :login")
    fun getResultByLogin(login: String): List<ResultTable>

    @get:Query("select * from ResultTable")
    val getallResult: List<ResultTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addResult(readoutModel: ResultTable)

    //Методы для таблицы Ответов
    @Query("select * from AnswerTable where login = :login")
    fun getAnswerByLogin(login: String): List<AnswerTable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAnswer(readoutModel: AnswerTable)
}

/*
 * Описание базы данных.
 */
@Database(entities = [UsersTable::class, TestTable::class, ResultTable::class, AnswerTable::class], version = 1)
abstract class TestDatabase : RoomDatabase() {

    abstract fun readoutDAO(): ReadoutModelDao           // Описываем абстрактные методы для получения объектов интерфейса BorrowModelDao

    companion object {
        var INSTANCE: TestDatabase? = null

        fun getAppDataBase(context: Context): TestDatabase? {


            if (INSTANCE == null) {
                synchronized(TestDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, TestDatabase::class.java, "TestDB")
                        .fallbackToDestructiveMigration()
                        .addCallback(onCreateCallBack)
                        .build()
                }
            }
            return INSTANCE
        }

        //Записываем в БД при создании наш список вопросов теста
        var onCreateCallBack: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                Observable.fromCallable {

                    val base = TestDatabase.getAppDataBase(MyApp.appContext)

                    if (base != null) {
                        val myDao = base.readoutDAO()
                        myDao.addTest(
                            TestTable(
                                "Какую первую программу обычно пишут программисты?",
                                "Для взлома аккаунта «ВКонтакте». Такая программа есть у каждого программиста",
                                "Hello world",
                                "Сортировку «пузырьком»",
                                "Hello world"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Представим гипотетическую ситуацию, в которой программа скомпилировалась с первого раза. Как вы поступите?",
                                "Выключу комп и спокойно пойду спать — дело сделано",
                                "Порадуюсь за себя и продолжу писать код",
                                "Буду искать ошибку в компиляторе, где-то она должна быть",
                                "Буду искать ошибку в компиляторе, где-то она должна быть"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Допустим, вы пишете проект, и заказчик утвердил документ, в котором чётко написано, что он хочет получить в результате. Назовём его ТЗ. Изменятся ли требования в процессе работы над проектом?",
                                "Изменятся, конечно",
                                "Не изменятся. Вы же сами сказали, что всё чётко зафиксировано",
                                "Ну немножко",
                                "Изменятся, конечно"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Какой правильный ответ на вопрос про рекурсию?",
                                "Да",
                                "42",
                                "Какой правильный ответ на вопрос про рекурсию?",
                                "Какой правильный ответ на вопрос про рекурсию?"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Представьте, что вы пишете программу и при попытке её сборки компилятор выдал вам одну ошибку. Вы исправили её и пробуете собрать проект ещё раз. Сколько теперь будет ошибок?",
                                "Была одна, теперь ошибок не будет",
                                "Неизвестно",
                                "Ещё одна ошибка",
                                "Неизвестно"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Вы пришли на проект, над которым раньше работал другой программист. Что можно сказать о его коде?",
                                "Его код просто ужасен, ну кто так пишет!",
                                "Надо сначала детально изучить проект, чтобы понять это",
                                "В его коде есть чему поучиться",
                                "Его код просто ужасен, ну кто так пишет!"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Перед вами три дерева. На том, что посередине, сидит кот. На дереве с каким индексом сидит кот?",
                                "2",
                                "1",
                                "0",
                                "1"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Теперь чуть сложнее. Что такое Пик Балмера?",
                                "Гора в Северной Америке",
                                "Феномен о том, что при определённой концентрации алкоголя в крови программистские способности резко возрастают",
                                "Яхта Стива Балмера — бывшего генерального директора Microsoft",
                                "Феномен о том, что при определённой концентрации алкоголя в крови программистские способности резко возрастают"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Что такое стринги?",
                                "Разновидность мини-трусиков",
                                "«Верёвки» на английском",
                                "Несколько переменных типа «строка»",
                                "Несколько переменных типа «строка»"
                            )
                        )
                        myDao.addTest(
                            TestTable(
                                "Какое максимальное число из перечисленных можно показать пальцами одной руки?",
                                "5",
                                "31",
                                "32",
                                "31"
                            )
                        )
                    }


                }.subscribeOn(Schedulers.io())

                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
            }
        }
    }
}
