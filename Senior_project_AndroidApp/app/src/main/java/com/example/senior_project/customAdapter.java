package com.example.senior_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class customAdapter extends BaseAdapter {
    Context con;
    JSONArray data;
    LayoutInflater inflater;
    byte[] imageString;


    public customAdapter(Context context, JSONArray data){
        this.con=context;
        this.data=data;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    public class holder{
TextView textView;
ImageView imageView;
    }

    @Override
    public int getCount() {
        return data.length();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final holder holder = new holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.row,null);

        holder.imageView=rowView.findViewById(R.id.RowImage);
        holder.textView=rowView.findViewById(R.id.textView2);
        JSONObject obj=data.optJSONObject(i);
        try
        {
            //Toast.makeText(con,"hey from adapter",Toast.LENGTH_SHORT).show();
            String date=obj.getString("date");
            holder.textView.setText(date);

            String image=obj.getString("image");
            imageString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte= BitmapFactory.decodeByteArray(imageString,0,imageString.length);
            holder.imageView.setImageBitmap(decodedByte);


        }
        catch (Exception e){
            Toast.makeText(con,"hey from adapter",Toast.LENGTH_SHORT).show();

        }
        return rowView;
    }
}
