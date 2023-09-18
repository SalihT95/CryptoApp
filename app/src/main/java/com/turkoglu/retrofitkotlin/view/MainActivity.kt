package com.turkoglu.retrofitkotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.turkoglu.retrofitkotlin.R
import com.turkoglu.retrofitkotlin.adapter.RecyclerViewAdapter
import com.turkoglu.retrofitkotlin.databinding.ActivityMainBinding
import com.turkoglu.retrofitkotlin.model.CryptoModel
import com.turkoglu.retrofitkotlin.service.CryptoAPI
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.disposables.ArrayCompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val BASE_URL = "https://raw.githubusercontent.com/"
    private var cryptoModels : ArrayList<CryptoModel>?=null
    private var recyclerViewAdapter: RecyclerViewAdapter?=null


    //private var compositeDisposable: CompositeDisposable?=null
    private var job:Job?=null

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Error: ${throwable.localizedMessage}")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view= binding.root
        setContentView(view)

        //https://github.com/atilsamancioglu/K21-JSONDataSet/blob/master/crypto.json
        //https://raw.githubusercontent.com/atilsamancioglu/K21-JSONDataSet/master/crypto.json

        //compositeDisposable= CompositeDisposable()

        // RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        loadData()

    }
    private fun loadData(){
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RXJava
            .build().create(CryptoAPI::class.java)

        job= CoroutineScope(Dispatchers.IO).launch {
            val response = retrofit.getData()

            withContext(Dispatchers.Main + exceptionHandler){
                if (response.isSuccessful){

                    response.body()?.let {
                        cryptoModels = ArrayList(it)
                        cryptoModels?.let {
                            recyclerViewAdapter = RecyclerViewAdapter(it)
                            binding.recyclerView.adapter = recyclerViewAdapter
                        }
                    }

                }


            }

        }



        /*RXJava
        compositeDisposable?.add(retrofit.getData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::hendleResponse))


         */

        /*
        val service = retrofit.create(CryptoAPI::class.java)
        val call = service.getData()

        call.enqueue(object : Callback<List<CryptoModel>>{
            override fun onResponse(
                call: Call<List<CryptoModel>>,
                response: Response<List<CryptoModel>>
            ) {
                if (response.isSuccessful){
                    response.body()?.let {

                        cryptoModels = ArrayList(it)
                        recyclerViewAdapter= RecyclerViewAdapter(cryptoModels!!)
                        binding.recyclerView.adapter = recyclerViewAdapter

                        /*
                        for (cryptoModel : CryptoModel in cryptoModels!!){
                            println(cryptoModel.currency)
                            println(cryptoModel.price)

                        }

                         */

                    }
                }
            }

            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })
            */
    }
/*
    private fun hendleResponse(cryptoList: List<CryptoModel>){
        cryptoModels = ArrayList(cryptoList)

        cryptoModels?.let {
            recyclerViewAdapter = RecyclerViewAdapter(it,this@MainActivity)
            binding.recyclerView.adapter = recyclerViewAdapter
        }

    }

 */

    override fun onDestroy() {
        super.onDestroy()
        //compositeDisposable?.clear()
        job?.cancel()
    }

}