package com.example.senior_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button open, recent, emergency;
    String Response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ServiceNotification.ServiceIsRun==false){
            ServiceNotification.ServiceIsRun = true ;

            Intent intent =new Intent(this,ServiceNotification.class);
            startService(intent);
        }

        recent=findViewById(R.id.btnRecent);
        open=findViewById(R.id.btnOpen);



        String url = "http://10.0.2.2:80/images/getItem.php";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                   /* image=jsonObject.getString("image");
                    imageString= Base64.decode(image,Base64.DEFAULT);
                    Bitmap decodedByte= BitmapFactory.decodeByteArray(imageString,0,imageString.length);
                    //imageView.setImageBitmap(decodedByte)*/

                    int i =Integer.parseInt(Response);

                    SharedPreferences sharedPreferences=MainActivity.this.getSharedPreferences("myfile",MODE_PRIVATE);
                    SharedPreferences.Editor editor =sharedPreferences.edit();
                    editor.putString("key",Response);
                    editor.commit();
                    //Toast.makeText(MainActivity.this,Response,Toast.LENGTH_SHORT).show();

                }
                catch (JSONException exception) {

                    //Toast.makeText(MainActivity.this, exception.toString(), Toast.LENGTH_LONG).show();
                }
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

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder Dialog= new AlertDialog.Builder(MainActivity.this);
                Dialog.setMessage("are you sure you want to open door?");
                Dialog.setCancelable(true);
                Dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String urlOpen= "http://10.0.2.2:80/images/open.php";
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
                Dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                   dialog.cancel();
                    }
                });
                AlertDialog alertDialog=Dialog.create();
                alertDialog.show();




            }

        });





        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Addguest.class);
                startActivity(intent);
            }
        });
        recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,RecentVisits.class);
                startActivity(i);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_alarm) {




                String urlOpen= "http://10.0.2.2:80/images/alarm.php";
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


            return true;
        }
        if (id==R.id.action_reset){
                String urlOpen= "http://10.0.2.2:80/images/reset.php";
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

        return super.onOptionsItemSelected(item);
    }
}