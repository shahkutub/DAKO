package com.sadi.dako.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sadi.dako.R;
import com.sadi.dako.utils.AppConstant;
import com.sadi.dako.utils.BitmapUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity {

    Context con;
    private ImageView imgCam;
    private CircleImageView imgUser;
    private File file;
    String picture = "";
    private static File dir = null;
    String imageLocal = "";
    public final int imagecaptureid = 0;
    public final int galarytakid = 1;

    private TextView tvSaveBtn;

    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        con = this;

        initialize();
    }

    private void initialize() {
        imgUser = (CircleImageView) findViewById(R.id.imgUser);
        imgCam = (ImageView)findViewById(R.id.imgCam);

        tvSaveBtn = (TextView)findViewById(R.id.tvSaveBtn);

        tvSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(con,RequestActivity.class));
                finish();
            }
        });

        imgCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageCaptureDialogue();
            }
        });
    }


    private void imageCaptureDialogue() {
        dialog = new Dialog(con);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.chang_photo_dialogue);

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        LinearLayout tvUseCam = (LinearLayout) dialog
                .findViewById(R.id.tvUseCam);
        LinearLayout tvRoll = (LinearLayout) dialog
                .findViewById(R.id.tvRoll);
        LinearLayout tvCance = (LinearLayout) dialog
                .findViewById(R.id.tvCance);


        tvRoll.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                AppConstant.isGallery = true;
                if (ActivityCompat.checkSelfPermission(con, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) UserActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppConstant.WRITEEXTERNAL_PERMISSION_RUNTIME);
                    dialog.dismiss();
                } else {
                    final Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), galarytakid);
                    dialog.dismiss();
                }
            }




        });

        tvUseCam.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                if (ActivityCompat.checkSelfPermission(UserActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) UserActivity.this,
                            new String[]{Manifest.permission.CAMERA}, AppConstant.CAMERA_RUNTIME_PERMISSION);
                    dialog.dismiss();
                } else {
                    AppConstant.isGallery = false;
                    if (ActivityCompat.checkSelfPermission(UserActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) UserActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppConstant.WRITEEXTERNAL_PERMISSION_RUNTIME);
                        dialog.dismiss();
                    } else {
                        final Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(i, imagecaptureid);
                        dialog.dismiss();
                    }
                }
            }


//                if (ContextCompat.checkSelfPermission(con,Manifest.permission.CAMERA)
//                        != PackageManager.PERMISSION_GRANTED) {
//
//                    requestPermissions(new String[]{Manifest.permission.CAMERA},
//                            1);
//
//                }else if(ContextCompat.checkSelfPermission(con,Manifest.permission.CAMERA)
//                        == PackageManager.PERMISSION_GRANTED){
//                    final Intent i = new Intent(
//                            "android.media.action.IMAGE_CAPTURE");
//                    startActivityForResult(i, imagecaptureid);
//                    dialog.dismiss();
//                }


        });

        tvCance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        dialog.show();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConstant.CAMERA_RUNTIME_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera

                if (ActivityCompat.checkSelfPermission(UserActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) UserActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, AppConstant.WRITEEXTERNAL_PERMISSION_RUNTIME);
                } else {
                    final Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, imagecaptureid);
                }
            } else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        } else if (requestCode == AppConstant.WRITEEXTERNAL_PERMISSION_RUNTIME) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (AppConstant.isGallery) {
                    final Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), galarytakid);
                } else {
                    final Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, imagecaptureid);
                }
            }
        }
    }


    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("On Activity Result", "On Activity Result");
        if (requestCode == galarytakid && resultCode == Activity.RESULT_OK) {

            Log.e("In gallelrly", "lllll..........");
            try {

                final Uri selectedImageUri = data.getData();

                final Bitmap bitmap = BitmapFactory
                        .decodeStream(UserActivity.this.getContentResolver().openInputStream(
                                selectedImageUri));
                //  final Bitmap d = BitmapFactory.decodeStream(getChildFragmentManager().)


                // final Bundle extras = data.getExtras();
                // final Bitmap b = (Bitmap) extras.get("data");
                final String path = setToImageView(bitmap);
                Log.e("Bitmap >>",
                        "W: " + bitmap.getWidth() + " H: " + bitmap.getHeight());
                Log.e("path", ">>>>>" + path);
                //PersistData.setStringData(con, AppConstant.path, path);
                picture = path;

//                Log.e("path",
//                        ">>>>>"
//                                + PersistData.getStringData(con,
//                                AppConstant.path));
                //Picasso.with(con).load(path).transform(new CircleTransform()).into(imgPicCapture);

                imgUser.setImageBitmap(bitmap);
                // AppConstant.imagebit=bitmap;

                //  AppConstant.imagebit = bitmap;

            } catch (final Exception e) {
                return;
            }

        } else if (requestCode == imagecaptureid
                && resultCode == Activity.RESULT_OK) {

            try {

                final Bundle extras = data.getExtras();
                final Bitmap b = (Bitmap) extras.get("data");

                final String path = setToImageView(b);
                Log.e("Bitmap >>",
                        "W: " + b.getWidth() + " H: " + b.getHeight());
                picture = path;
                Log.e("path", ">>>>>" + path);
//                Log.e("path",
//                        ">>>>>"
//                                + PersistData.getStringData(con,
//                                AppConstant.path));


//                    ImgUserEdit.setImageBitmap(b);
//                    AppConstant.imagebit = b;

                imgUser.setImageBitmap(b);
                //Picasso.with(con).load(path).transform(new CircleTransform()).into(imgPicCapture);
                //AppConstant.imagebit = b;


            } catch (final Exception e) {
                return;
            }

        }

    }

    private String setToImageView(Bitmap bitmap) {

        try {

            // if (isImage) {
            final Bitmap bit = BitmapUtils.getResizedBitmap(bitmap, 300);
            final double time = System.currentTimeMillis();

            imageLocal = saveBitmapIntoSdcard(bit, "luna" + time + ".png");

            Log.e("camera saved URL :  ", " " + imageLocal);


        } catch (final IOException e) {
            e.printStackTrace();

            imageLocal = "";
            Log.e("camera saved URL :  ", e.toString());

        }

        return imageLocal;

    }

    private String saveBitmapIntoSdcard(Bitmap bitmap22, String filename)
            throws IOException {
        /*
         *
		 * check the path and create if needed
		 */
        createBaseDirctory();

        try {

            new Date();

            OutputStream out = null;
            file = new File(this.dir, "/" + filename);

            if (file.exists()) {
                file.delete();
            }

            out = new FileOutputStream(file);

            bitmap22.compress(Bitmap.CompressFormat.PNG, 100, out);

            out.flush();
            out.close();
            // Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
            return file.getAbsolutePath();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void createBaseDirctory() {
        final String extStorageDirectory = Environment
                .getExternalStorageDirectory().toString();
        dir = new File(extStorageDirectory + "/LUNA");

        if (this.dir.mkdir()) {
            System.out.println("Directory created");
        } else {
            System.out.println("Directory is not created or exists");
        }
    }
}
