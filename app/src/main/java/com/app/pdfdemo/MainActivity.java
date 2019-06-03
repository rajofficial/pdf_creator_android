package com.app.pdfdemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.pdfdemo.databinding.ActivityMainBinding;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private final int RESULT_LOAD_IMAGE = 100;
    private final int RESULT_LOAD_IMAGE_PROFILE = 102;
    private final int SELECT_PICTURE = 101;
    ImageModelClass modelClass = new ImageModelClass();
    private List<String> uriList = new ArrayList<>();
    private ImagesAdapter adapter;
    private File pdfFile;


    @Override
    protected void initView() {
        binding = (ActivityMainBinding) viewDataBinding;
        binding.rcvImages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new ImagesAdapter(this, uriList);
        binding.rcvImages.setAdapter(adapter);
        binding.ivAddImages.setOnClickListener(this);
        binding.ivProfile.setOnClickListener(this);
        binding.btnPdf.setOnClickListener(this);

    }

    @Override
    protected int getLayoutById() {
        return R.layout.activity_main;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivAddImages:
                addImagesFromGallery("gallery");
                break;
            case R.id.ivProfile:
                addImagesFromGallery("profile");
                break;

            case R.id.btnPdf:
                if (checkPermission()) {

                    createPdf();
                } else {
                    requestPermission(200);
                }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createPdf() {

       Intent  intent = new Intent(this,PdfActivity.class);
       intent.putExtra("invoiceDate",binding.ivDate.getText().toString());
       intent.putExtra("dueDate",binding.dDate.getText().toString());
       intent.putExtra("senderInfo",binding.sInfo.getText().toString());
       intent.putExtra("recipientName",binding.rName.getText().toString());
       intent.putExtra("paymentMethod",binding.payMethod.getText().toString());
       intent.putExtra("total",binding.total.getText().toString());
       intent.putExtra("totalAmount",binding.totalAmount.getText().toString());
       intent.putExtra("imageUrl","");
       startActivity(intent);
    }

    private static void addImage(Document document, byte[] byteArray)  {
        Image image = null;
        try
        {
            image = Image.getInstance(byteArray);
        }
        catch (BadElementException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // image.scaleAbsolute(150f, 150f);
        try
        {
            document.add(image);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    private void addImagesFromGallery(String check) {
        if (check.equalsIgnoreCase("gallery")) {
            if (checkPermission()) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, RESULT_LOAD_IMAGE);
            } else {
                requestPermission(RESULT_LOAD_IMAGE);
            }


        } else if (check.equalsIgnoreCase("profile")) {
            if (checkPermission()) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, RESULT_LOAD_IMAGE_PROFILE);
            }
            requestPermission(RESULT_LOAD_IMAGE_PROFILE);

        }

    }

    private void requestPermission(int requestcode) {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                requestcode);

    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                uriList.add(String.valueOf(data.getData()));
                Objects.requireNonNull(data.getData()).getPath();
                if (adapter != null)
                    adapter.notifyDataSetChanged();
            }

        }
        if (requestCode == RESULT_LOAD_IMAGE_PROFILE && resultCode == RESULT_OK) {
            if (data != null) {
                generateBitmap(data.getData());

            }

        }


    }

    private void generateBitmap(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            binding.ivProfile.setImageBitmap(bitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "Please grant the permission", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

   /* private void viewPdf() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(pdfDir,  "pdfFileName"));
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }*/
}
