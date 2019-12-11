package xyz.tootal.contactsystem;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class ModifyDetailActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private EditText person_name_modify_edittext;
    private EditText person_number_modify_edittext;
    private EditText person_note_modify_edittext;
    private ImageView person_image_modify_imageview;
    private Uri imageUri;
    long curTime;
    private Person person;
    public static final int TAKE_PHOTO = 1;

    public static final int CHOOSE_PHOTO = 2;
    private static final String TAG = "ModifyDetailActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        Intent intent=getIntent();
        person=(Person)intent.getSerializableExtra("person");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_modify);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_modify);
        person_image_modify_imageview=(ImageView) findViewById(R.id.person_image_modify_imageview);
        person_name_modify_edittext=(EditText)findViewById(R.id.person_name_modify_edittext);
        person_number_modify_edittext=(EditText)findViewById(R.id.person_number_modify_edittext);
        person_note_modify_edittext=(EditText)findViewById(R.id.person_note_modify_edittext);
        person_image_modify_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAvatarMenu(person_image_modify_imageview);
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_cancle);
        }
        person_name_modify_edittext.setText(person.getName());
        person_number_modify_edittext.setText(person.getNumber());
        person_note_modify_edittext.setText(person.getNote());
        Glide.with(this).load(person.getImageUri()).into(person_image_modify_imageview);

    }

    private void showAvatarMenu(View v){
        PopupMenu popupMenu=new PopupMenu(this,v);
        getMenuInflater().inflate(R.menu.modify_avatar,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.modify_avatar_menu_camera:
//                        Toast.makeText(NewActivity.this, "正在启动相机", Toast.LENGTH_SHORT).show();
                        takePhoto();
                        break;
                    case R.id.modify_avatar_menu_select:
//                        Toast.makeText(NewActivity.this, "正在打开图库", Toast.LENGTH_SHORT).show();
                        if (ContextCompat.checkSelfPermission(ModifyDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ModifyDetailActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
                        } else {
                            openAlbum();
                        }
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }


    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }

    private void takePhoto(){
        curTime=System.currentTimeMillis();
        File outputImage = new File(getExternalCacheDir(), String.valueOf(curTime)+".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(outputImage);
        } else {
            imageUri = FileProvider.getUriForFile(ModifyDetailActivity.this, "xyz.tootal.contactsystem.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
//                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                        person_image_new_imageview.setImageBitmap(bitmap);
                        Glide.with(this).load(imageUri).into(person_image_modify_imageview);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            imageUri=Uri.parse(imagePath);
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            person_image_modify_imageview.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_modify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent();
                setResult(RESULT_CANCELED,intent);
                finish();
                break;
            case R.id.modify_done:
//                Person person1= DataSupport.where("id=?",String.valueOf(person.getId())).find(Person.class).get(0);
                Person person1=new Person();
                person1.setName(person_name_modify_edittext.getText().toString());
                person1.setNumber(person_number_modify_edittext.getText().toString());
                person1.setNote(person_note_modify_edittext.getText().toString());
                if(imageUri!=null){
                    person1.setImageUri(imageUri.toString());
                }
//                Toast.makeText(ModifyDetailActivity.this, String.valueOf(person.getId()), Toast.LENGTH_SHORT).show();
                person1.updateAll("id=?",String.valueOf(person.getId()));
//                person1.save();
                Toast.makeText(ModifyDetailActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
        }
        return true;
    }

}
