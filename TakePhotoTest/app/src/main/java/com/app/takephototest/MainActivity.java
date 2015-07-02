package com.app.takephototest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends Activity {

    public static final int TAKE_PHOTO = 1;

    public static final int CROP_PHOTO = 2;

    private Button takePhoto;

    private ImageView picture;

    private Uri imageUri;

    private Button choosePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        takePhoto = (Button) findViewById(R.id.take_photo);
        picture = (ImageView) findViewById(R.id.picture);
        choosePicture = (Button) findViewById(R.id.choose_pic);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage = new File(Environment.getExternalStorageDirectory(), "tempImage.jpg");
                if(outputImage.exists()) {
                    outputImage.delete();
                }
                try {
                    outputImage.createNewFile();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        choosePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File outputImage = new File(Environment.getExternalStorageDirectory(), "output_iamge.jpg");
                if(outputImage.exists()) {
                    outputImage.delete();
                }
                try {
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                intent.putExtra("crop", true);
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, CROP_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, CROP_PHOTO);
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        InputStream is = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        //Bug:  BitmapFactory.decodeStream(is) return null


                        //InputStream is = getContentResolver().openInputStream(imageUri);
                        //byte[] img = readStream(is);
                        //Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                        if(bitmap == null) {
                            Toast.makeText(this, "bitmap is empty", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "set  bitmap succeed ", Toast.LENGTH_SHORT).show();
                        }

                        //picture.setImageBitmap(bitmap);
                        picture.setImageResource(R.drawable.img01);

                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static byte[] readStream(InputStream is) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        return outputStream.toByteArray();
    }

//    public class PatchInputStream extends FilterInputStream {
//        public PatchInputStream(InputStream in) {
//            super(in);
//        }
//        public long skip(long n) throws IOException {
//            long m = 0L;
//            while (m < n) {
//                long _m = in.skip(n-m);
//                if (_m == 0L) break;
//                m += _m;
//            }
//            return m;
//        }
//    }
}
