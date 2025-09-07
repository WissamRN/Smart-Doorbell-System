package com.example.senior_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Addguest extends AppCompatActivity {
    byte [] image;
    Bitmap bitmap;
    Button select, upload;
    ImageView imageView;
    EditText editText;
    ProgressDialog progressDialog;
    String EditTextImageName;
    String ServerUrl ="http://10.0.2.2:80/images/upload.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addguest);
        select=findViewById(R.id.btnSelect);
        upload=findViewById(R.id.btnAdd);
        imageView=findViewById(R.id.imageView);
        editText =findViewById(R.id.editTextName);
        SharedPreferences sharedPreferences=Addguest.this.getSharedPreferences("myfile",MODE_PRIVATE);
        String x =sharedPreferences.getString("key", " ");
        int y = Integer.parseInt(x);
        Toast.makeText(Addguest.this,x ,Toast.LENGTH_LONG).show();
        Intent intent=getIntent();

        try {
            image = intent.getByteArrayExtra("image");
            if (image!=null) {

                Bitmap decodedByte = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageView.setImageBitmap(decodedByte);
            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();

        }


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Select_image();


            }
        });
    }
    public void Select_image(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select image from gallery"),1);

    }
    public void uploadImage() {

        if (TextUtils.isEmpty(editText.getText().toString())) {
            Toast.makeText(Addguest.this, "please enter a name", Toast.LENGTH_SHORT).show();
        } else {
            RequestQueue queue = Volley.newRequestQueue(getBaseContext());
            String s = ImageToString(bitmap);
            String Url = "http://10.0.2.2:80/images/upload.php?image=" + s + "&name=" + editText.getText().toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String Response = jsonObject.getString("response");
                        Toast.makeText(Addguest.this, Response, Toast.LENGTH_SHORT).show();
                    } catch (JSONException exception) {

                        Toast.makeText(Addguest.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(Addguest.this, s, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Addguest.this, error.toString(), Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", editText.getText().toString());
                    params.put("image", ImageToString(bitmap));
                    return params;

                }
            };
            queue.add(stringRequest);
            //MySingleton.getInstance(Addguest.this).addToRequestQueue(stringRequest);
        }
    }
    //-------------------------------------------------------------------------------------------//
    public String ImageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream;
        byteArrayOutputStream =new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte [] byteArrayVar=byteArrayOutputStream.toByteArray();
        final String  convertImage = Base64.encodeToString(byteArrayVar,Base64.DEFAULT);
        return convertImage;
    }
    //-------------------------------------------------------------------------------------------//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 && resultCode==RESULT_OK && data!=null && data!=null){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException exception){

            }
        }
    }

}