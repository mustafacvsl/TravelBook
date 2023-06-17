package com.example.travelbookproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.travelbookproject.databinding.ActivityNextBinding;
import com.example.travelbookproject.databinding.ActivityUcuncuactivityBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

public class ucuncuactivity extends AppCompatActivity {
    private ActivityUcuncuactivityBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    SQLiteDatabase database;

    Bitmap selectedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityUcuncuactivityBinding.inflate(getLayoutInflater());
        View view= binding.getRoot();

        setContentView(view);
        registerLauncher();
        database=this.openOrCreateDatabase("Arts",MODE_PRIVATE,null);
        Intent intent= getIntent();
        String info=intent.getStringExtra("info");

        if(info.equals("new")){
            binding.nameText.setText("");
            binding.surnameText
                    .setText("");

            binding.yearText.setText("");
            binding.saveButton.setVisibility(View.VISIBLE);
            binding.imageView.setImageResource(R.drawable.istanbul);



        }else {
            int artId= intent.getIntExtra("artId", 1);
            binding.saveButton.setVisibility(View.VISIBLE);


            try {

                Cursor cursor= database.rawQuery("SELECT * FROM arts WHERE id = ?",new String[] {String.valueOf(artId)});
                int ArtNameIx = cursor.getColumnIndex("artname");
                int painterName = cursor.getColumnIndex("paintername");
                int yearIx = cursor.getColumnIndex("year");

                int imageIx= cursor.getColumnIndex("image");
                while (cursor.moveToNext()){
                    binding.nameText.setText(cursor.getString(ArtNameIx));
                    binding.surnameText.setText(cursor.getString(painterName));
                    binding.yearText.setText(cursor.getString(yearIx));
                    byte[] bytes= cursor.getBlob(imageIx);
                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    binding.imageView.setImageBitmap(bitmap);
                }
                cursor.close();


            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }
        public void save(View view){
        String name= binding.nameText.getText().toString();
        String name2=binding.surnameText.getText().toString();
        String year=binding.yearText.getText().toString();

        Bitmap smallImage= makeSmallerImage(selectedImage,300);
            ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
            smallImage.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream);
            byte[] byteArray= byteArrayOutputStream.toByteArray();

            try {

                database.execSQL("CREATE TABLE IF NOT EXISTS arts (id INTEGER PRIMARY KEY,artname VARCHAR, paintername VARCHAR, year VARCHAR, images BLOB)");

                String sqlSorgu="INSERT INTO arts (artname,paintername, year, images) VALUES(?, ?, ?, ?)";
                SQLiteStatement sqLiteStatement= database.compileStatement(sqlSorgu);
                sqLiteStatement.bindString(1,name);
                sqLiteStatement.bindString(2,name2);
                sqLiteStatement.bindString(3,year);
                sqLiteStatement.bindBlob(4,byteArray);
                sqLiteStatement.execute();


            }catch (Exception e){
                e.printStackTrace();
            }

                Intent intent2= new Intent(ucuncuactivity.this,nextActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent2);



        }


        public Bitmap makeSmallerImage(Bitmap image , int maximumSize){
        int width= image.getWidth();
        int height=image.getHeight();
        float bitmapRadio=(float) width/ (float) height;
        if(bitmapRadio>1){
            width=maximumSize;
            height=(int) (width/bitmapRadio);


        }else {
            height=maximumSize;
            width= (int) (height * bitmapRadio);


        }



        return image.createScaledBitmap(image,width,height,true);


        }
        public void ımage(View view){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if ((ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE))){
                Snackbar.make(view,"Galeri için izin gerekiyor",Snackbar.LENGTH_INDEFINITE).setAction("İzin ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);


                    }
                }).show();

            }else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);


            }

        }else {
            Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);


        }

        }

        public void registerLauncher(){
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()==RESULT_OK){
                    Intent intentFromResult = result.getData();
                    if(intentFromResult != null){
                    Uri imageData= intentFromResult.getData();

                    try {
                        if(Build.VERSION.SDK_INT>=28){
                            ImageDecoder.Source source=ImageDecoder.createSource(getContentResolver(),imageData);
                            selectedImage= ImageDecoder.decodeBitmap(source);
                            binding.imageView.setImageBitmap(selectedImage);

                        }




                    }catch (Exception e){
                        e.printStackTrace();
                    }






                    }

                }

            }
        });




        permissionLauncher= registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intent);




                }else {
                    Toast.makeText(ucuncuactivity.this,"İzin gerekiyor!!!",Toast.LENGTH_LONG).show();
                }

            }
        });
        }
}