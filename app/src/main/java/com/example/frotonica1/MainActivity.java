package com.example.frotonica1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;

import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;



import com.bumptech.glide.Glide;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    RelativeLayout l;
    ImageView e1;
    ImageView e2;
    ImageView e3;

    int element =1;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l = findViewById(R.id.top);
        e1 = findViewById(R.id.element1);
        e2 = findViewById(R.id.element2);
        e3 = findViewById(R.id.top1);
        Glide.with(this).load(R.mipmap.imageelement1).into(e1);
        Glide.with(this).load(R.mipmap.imageelement2).into(e2);
        Glide.with(this).load(R.mipmap.baselayout).into(e3);
        e1.setOnTouchListener(new t());
        e2.setOnTouchListener(new t());

    }

    public void c(View view) {
        l.setDrawingCacheEnabled(true);
        l.buildDrawingCache();
        Bitmap bm = l.getDrawingCache();
        Bitmap b = Bitmap.createScaledBitmap(bm,3600,2400, false);
        File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //context.getExternalFilesDir(null);

        File file = new File(storageLoc, "base" + ".jpg");

        try {
            FileOutputStream fos = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();

            scanFile(this, Uri.fromFile(file));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            l.destroyDrawingCache();
        }

    }

    private static void scanFile(Context context, Uri imageUri) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        context.sendBroadcast(scanIntent);

    }

    public void b(View view) {
        element=1;

        pickImage();
    }
    @SuppressLint("IntentReset")
    public void pickImage() {
        @SuppressLint("IntentReset") Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap bmp = extras.getParcelable("data");
                        if (element == 1) {
                            ImageView i = findViewById(R.id.element1);

                            i.setImageBitmap(bmp);
                        }
                        else if (element == 2){
                            ImageView i = findViewById(R.id.element2);

                            i.setImageBitmap(bmp);
                        }
            }



        }
    }


    PointF DownPT = new PointF();
    PointF StartPT = new PointF();

    public void d(View view) {
        element =2;
        pickImage();


    }

    public void q(View view) {

        l.setDrawingCacheEnabled(true);
        l.buildDrawingCache();

        Bitmap bm = l.getDrawingCache();
        Bitmap b = Bitmap.createScaledBitmap(bm,3600,2400, false);

        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(),
                b, "Design", null);

        Uri uri = Uri.parse(path);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TEXT, "Created by Fotonicia1 App!");
        this.startActivity(Intent.createChooser(share, "Share Your Design!"));
    }


    private class t implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch( View v,  MotionEvent event) {

            ImageView image = findViewById(v.getId());
            int eid = event.getAction();
            switch (eid)
            {
                case MotionEvent.ACTION_MOVE :
                    PointF mv = new PointF( event.getX() - DownPT.x, event.getY() - DownPT.y);
                    image.setX((int)(StartPT.x+mv.x));
                    image.setY((int)(StartPT.y+mv.y));
                    StartPT = new PointF( image.getX(), image.getY() );
                    break;
                case MotionEvent.ACTION_DOWN :
                    DownPT.x = event.getX();
                    DownPT.y = event.getY();
                    StartPT = new PointF( image.getX(), image.getY() );
                    break;
                case MotionEvent.ACTION_UP :
                    break;
                default :
                    break;
            }
            return true;

        }
    }
}

