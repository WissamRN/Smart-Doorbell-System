package com.example.senior_project;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecentVisits extends AppCompatActivity {
byte[] imageString;
    String image;
ImageView imageView;
JSONArray array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_visits);
        //Toast.makeText(RecentVisits.this,"hi man",Toast.LENGTH_SHORT).show();

        //imageView=findViewById(R.id.imageView2);
        Button capture=findViewById(R.id.capture);
        // textView=findViewById(R.id.tvDate);
        ListView listView=findViewById(R.id.ListView);


        String urlAll="http://10.0.2.2:80/images/getallimages.php";
        RequestQueue queue1=Volley.newRequestQueue(RecentVisits.this);
        JsonArrayRequest arrayRequest=new JsonArrayRequest(Request.Method.GET, urlAll, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                array=response;
                customAdapter customAdapter=new customAdapter(RecentVisits.this,array);
                listView.setAdapter(customAdapter);
                //Toast.makeText(RecentVisits.this,"helloooo from success",Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            //Toast.makeText(RecentVisits.this,"helloooo",Toast.LENGTH_SHORT).show();
            }
        });
        queue1.add(arrayRequest);












        String url = "http://10.0.2.2:80/images/getItem.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
/*
                try {


                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    image=jsonObject.getString("image");
                    imageString= Base64.decode(image,Base64.DEFAULT);
                    String date=jsonObject.getString("date");
                    textView.setText(date);

                    Bitmap decodedByte= BitmapFactory.decodeByteArray(imageString,0,imageString.length);
                    imageView.setImageBitmap(decodedByte);

                    int i =Integer.parseInt(Response);

                    SharedPreferences sharedPreferences=RecentVisits.this.getSharedPreferences("myfile",MODE_PRIVATE);
                    SharedPreferences.Editor editor =sharedPreferences.edit();
                    editor.putString("key",Response);
                    editor.commit();
                    //Toast.makeText(MainActivity.this,Response,Toast.LENGTH_SHORT).show();

                }
                catch (JSONException exception) {

                    //Toast.makeText(MainActivity.this, exception.toString(), Toast.LENGTH_LONG).show();
                }*/
                //String value= response.toString();
                //int id = Integer.parseInt(value);
                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();






                //Toast.makeText(getApplicationContext(),"error parsing",Toast.LENGTH_SHORT).show();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error+"",Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
       capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                String urlOpen= "http://10.0.2.2:80/images/capture.php";
                RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
                StringRequest request1= new StringRequest(Request.Method.GET, urlOpen, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error+"",Toast.LENGTH_SHORT).show();
                    }
                }
                );
                requestQueue.add(request1);




            }
        });

        if (ServiceNotification.ServiceIsRun==false){
            ServiceNotification.ServiceIsRun = true ;

            Intent intent =new Intent(this,ServiceNotification.class);
            startService(intent);
        }
        ;
    }
}