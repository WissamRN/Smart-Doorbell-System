package com.example.senior_project;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.namespace.QName;

public class ServiceNotification extends IntentService {
    String Response;
    String image;
    byte [] decodedString;
    int y;
    int x;
    final int[] integer = {0};
    public ServiceNotification() {
        super("myService");

    }



    public static boolean ServiceIsRun =false;



    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ServiceNotification(String name) {
        super(name);
    }
    int id =0;


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SharedPreferences sharepref= this.getSharedPreferences("myfile",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharepref.edit();
        String id =sharepref.getString("key","");
        x= Integer.parseInt(id);






        while (ServiceIsRun){

            String url = "http://10.0.2.2:80/images/getItem.php";
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            StringRequest request= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Response = jsonObject.getString("response");
                        image =jsonObject.getString("image");
                        decodedString = Base64.decode(image,Base64.DEFAULT);
                        //Toast.makeText(getApplicationContext(),decodedString.toString(),Toast.LENGTH_LONG).show();
                        y =Integer.parseInt(Response);

                    }
                    catch (JSONException e){
                        //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                    }
                    if (y >x) {




                        Intent i = new Intent(getApplicationContext(), RecentVisits.class);
                        i.putExtra("image",decodedString);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);


                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);
                        NotificationCompat.Builder NA = new NotificationCompat.Builder(getApplicationContext(), "sam");
                        NA.setSmallIcon(R.drawable.ic_launcher_foreground);
                        NA.setContentTitle("new notification");
                        NA.setContentText("New Activity Nearby" +String.valueOf(y));
                        NA.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        NA.setContentIntent(pendingIntent);
                        NA.setAutoCancel(true);
                        NotificationManager notificationManager = null;
                        NotificationChannel channel = new NotificationChannel("sam", "myChannel", NotificationManager.IMPORTANCE_LOW);
                        notificationManager = (NotificationManager) getApplicationContext().getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(channel);
                        notificationManager.notify(0, NA.build());
                        editor.putString("key",String.valueOf(y));
                        x= y;


                    }

        /*
                    //Toast.makeText(getApplicationContext(),response+"service",Toast.LENGTH_SHORT).show();

                    //create new intent
                    Intent intent= new Intent();
                    //set the action that will recieve our broadcast
                    intent.setAction("com.example.Broadcast");
                    //send the id
                    intent.putExtra("image",image);
                    //send the data to the broadcast
                    sendBroadcast(intent);
                    //create notification here



                    //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show(); */
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(getApplicationContext(),error+"",Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(request);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

}