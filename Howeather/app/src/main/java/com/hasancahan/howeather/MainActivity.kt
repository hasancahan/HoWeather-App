package com.hasancahan.howeather
import android.app.DownloadManager
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import im.delight.android.location.SimpleLocation
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {


    var tvSehir2:TextView? =null
    var location:SimpleLocation?= null
    var latitude:String?=null
    var longitude:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//Oluşturulan Spinner üzerinden seçilen şehir ve işlemleri

        var spinnerAdapter=ArrayAdapter.createFromResource(this,R.array.sehirler,android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnSehirler.setTitle("Şehir Seçin")
        spnSehirler.setPositiveButton("SEÇ")
        spnSehirler.adapter=spinnerAdapter
        spnSehirler.setOnItemSelectedListener(this)
        location= SimpleLocation(this)
        spnSehirler.setSelection(7)
        verileriGetir("Ankara")



    }


    private fun oankiSehriGetir(lat: String?,longt:String?):String? {



    //API üzerinden alınan veri ------ Sisteme bağlama

    var sehirAdi:String?=null

    val url="http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+longt+"&appid=ad7f0287623a21f0c79a89f0155c37fc&lang=tr&units=metric"
    val havaDurumuObjeRequest2 = JsonObjectRequest(Request.Method.GET, url,null,object : Response.Listener<JSONObject>{


        override fun onResponse(response: JSONObject?) {
            //Toast.makeText(this@MainActivity,response.toString(),Toast.LENGTH_LONG).show() ---Kontrol için...

//  API Üzerinden Sıcaklık vb. değerleri çekme

            var main = response?.getJSONObject("main")

            var sicaklik = main?.getInt("temp")
            tvSicaklik.text=sicaklik.toString()

            var sehirAdi = response?.getString("name")
            tvSehir.text=sehirAdi!!.toUpperCase()
            tvSehir?.setText(sehirAdi)

            var weather = response?.getJSONArray("weather")

            var aciklama = weather?.getJSONObject(0)?.getString("description")
            tvAciklama.text= aciklama!!.toUpperCase()

            var icon = weather?.getJSONObject(0)?.getString("icon")

// Gece Gündüz İlişkisi

            if(icon?.last() == 'd'){
                rootLayout.background=getDrawable(R.drawable.after_noon)
                tvAciklama.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                tvSicaklik.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                tvSehir.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                tvTarih.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                tvSicaklik.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                textView4.setTextColor(resources.getColor(R.color.colorPrimaryDark))

            }else{
                rootLayout.background=getDrawable(R.drawable.night)
                tvAciklama.setTextColor(resources.getColor(R.color.colorAccent))
                tvSicaklik.setTextColor(resources.getColor(R.color.colorAccent))
                tvSehir.setTextColor(resources.getColor(R.color.colorAccent))
                tvTarih.setTextColor(resources.getColor(R.color.colorAccent))
                tvSicaklik.setTextColor(resources.getColor(R.color.colorAccent))
                textView4.setTextColor(resources.getColor(R.color.colorAccent))

            }


// Değerleri tasarım üzerinde aktifleştirme...

            var resimDosyaAdi = resources.getIdentifier("d" + icon?.sonKarakteriSil(), "drawable", packageName) //R.drawable.icon_50n
            imgHavaDurumu.setImageResource(resimDosyaAdi)

            tvTarih.text=tarihyazdır()


        }
    }, object : Response.ErrorListener{

        override fun onErrorResponse(error: VolleyError?) {

        }


    })


//Null değer oluşması durumunda veya veri çekiminde hata olursa görünecek...

    MySingleton.getInstance(this)?.addToRequestQueue(havaDurumuObjeRequest2)
    if (sehirAdi !=null){
        return sehirAdi
    }
    else {
        return "N \\ A"

    }


}

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        //Konum kontrol ve İzin İşlemleri

        tvSehir2= view as TextView

        if (position ==0)
        {

            location= SimpleLocation(this)

            if (!location!!.hasLocationEnabled())
            {
                Toast.makeText(this,"Lütfen GPS Açınız",Toast.LENGTH_LONG).show()
                SimpleLocation.openSettings(this)
            }
            else
            {
                if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 60)
                }
                else
                {
                    location= SimpleLocation(this)
                    latitude= String.format("%.6f",location?.latitude)
                    longitude= String.format("%.6f",location?.longitude)
                    oankiSehriGetir(latitude,longitude)

                }
            }
        }
        else
        {
            var secilenSehir=parent?.getItemAtPosition(position).toString()
            tvSehir2= view as TextView
            verileriGetir(secilenSehir)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode==60)
        {
            if (grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                location= SimpleLocation(this)
                latitude= String.format("%.6f",location?.latitude)
                longitude= String.format("%.6f",location?.longitude)
                oankiSehriGetir(latitude,longitude)
            }
            else
            {
                spnSehirler.setSelection(7)
                Toast.makeText(this,"Lütfen İzin Veriniz",Toast.LENGTH_LONG).show()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {

        //Konum değişimine göre konum algılama ve takip etme...
        super.onResume()

        // make the device update its location
        location?.beginUpdates()

        // ...
    }

    override fun onPause() {
        // stop location updates (saves battery)
        location?.endUpdates()

        // ...

        super.onPause()
    }

    fun verileriGetir(sehir:String){

        //API üzerinden alınan veri ------ Sisteme bağlama

        val url="http://api.openweathermap.org/data/2.5/weather?q="+sehir+"&appid=ad7f0287623a21f0c79a89f0155c37fc&lang=tr&units=metric"
        val havaDurumuObjeRequest = JsonObjectRequest(Request.Method.GET, url,null,object : Response.Listener<JSONObject>{


            override fun onResponse(response: JSONObject?) {
                //Toast.makeText(this@MainActivity,response.toString(),Toast.LENGTH_LONG).show() ---Kontrol için...

//  API Üzerinden Sıcaklık vb. değerleri çekme

                var main = response?.getJSONObject("main")

                var sicaklik = main?.getInt("temp")
                tvSicaklik.text=sicaklik.toString()

                var sehirAdi = response?.getString("name")
                tvSehir.text=sehirAdi!!.toUpperCase()

                var weather = response?.getJSONArray("weather")

                var aciklama = weather?.getJSONObject(0)?.getString("description")
                tvAciklama.text= aciklama!!.toUpperCase()



                var icon = weather?.getJSONObject(0)?.getString("icon")
// Gece Gündüz İlişkisi

                if(icon?.last() == 'd'){
                    rootLayout.background=getDrawable(R.drawable.after_noon)
                    tvAciklama.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                    tvSicaklik.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                    tvSehir.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                    tvTarih.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                    tvSicaklik.setTextColor(resources.getColor(R.color.colorPrimaryDark))
                    textView4.setTextColor(resources.getColor(R.color.colorPrimaryDark))

                }else{
                    rootLayout.background=getDrawable(R.drawable.night)
                    tvAciklama.setTextColor(resources.getColor(R.color.colorAccent))
                    tvSicaklik.setTextColor(resources.getColor(R.color.colorAccent))
                    tvSehir.setTextColor(resources.getColor(R.color.colorAccent))
                    tvTarih.setTextColor(resources.getColor(R.color.colorAccent))
                    tvSicaklik.setTextColor(resources.getColor(R.color.colorAccent))
                    textView4.setTextColor(resources.getColor(R.color.colorAccent))

                }


// Değerleri tasarım üzerinde aktifleştirme...

                var resimDosyaAdi = resources.getIdentifier("d" + icon?.sonKarakteriSil(), "drawable", packageName) //R.drawable.icon_50n
                imgHavaDurumu.setImageResource(resimDosyaAdi)

                tvTarih.text=tarihyazdır()




            }


        }, object : Response.ErrorListener{

            override fun onErrorResponse(error: VolleyError?) {

            }


        })

        MySingleton.getInstance(this)?.addToRequestQueue(havaDurumuObjeRequest)






    }

    fun tarihyazdır():String{

//Tarih Yazdırma işlemi

        var tavkim=Calendar.getInstance().time
        var formatlayici=SimpleDateFormat("EEEE,MMMM yyyy",Locale("tr"))
        var tarih=formatlayici.format(tavkim)
        return tarih


    }
    }

private fun String.sonKarakteriSil(): String {

     return this.substring(0,this.length-1)

}

