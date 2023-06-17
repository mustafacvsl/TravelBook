package com.example.travelbookproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.travelbookproject.databinding.ActivityNextBinding;

import java.util.ArrayList;

public class nextActivity extends AppCompatActivity {
    public ActivityNextBinding binding;
    ArrayList<Art> artArrayList;
    ArtAdaper artAdaper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNextBinding.inflate(getLayoutInflater());
        View view= binding.getRoot();
        setContentView(view);


        artArrayList= new ArrayList<>();
       binding.recylerView.setLayoutManager(new LinearLayoutManager(this));
        artAdaper= new ArtAdaper(artArrayList);

        binding.recylerView.setAdapter(artAdaper);

        listele();




        



    }

    private void listele(){
        SQLiteDatabase database= this.openOrCreateDatabase("Arts", MODE_PRIVATE,null);
        Cursor cursor= database.rawQuery("SELECT * FROM arts",null);
        int nameIX=cursor.getColumnIndex("artname");
        int Idx= cursor.getColumnIndex("id");
        while (cursor.moveToNext()){
            String name= cursor.getString(nameIX);
            int id=cursor.getInt(Idx);
            Art art= new Art(name,id);
            artArrayList.add(art);

        }
        artAdaper.notifyDataSetChanged();
        cursor.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.add_art,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.add_art){
            Intent intent= new Intent(nextActivity.this,ucuncuactivity.class);
            intent.putExtra("info","new");
            startActivity(intent);


        }
        return super.onOptionsItemSelected(item);
    }
}