package com.ratnadirawidyasari.pola;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ImageView;

import java.io.FileInputStream;

public class ZoomImage extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.zoomimage);
        imageView = (ImageView) this.findViewById(R.id.player_imageview);
        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            imageView.setImageBitmap(bmp);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


//        onBackPressed();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
