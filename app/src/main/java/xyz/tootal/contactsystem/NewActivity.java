package xyz.tootal.contactsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class NewActivity extends AppCompatActivity {
    private static final String TAG = "NewActivity";
    private Uri imageUri;
    private ImageButton new_avatar_imagebutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Button new_confirm_button=findViewById(R.id.new_confirm_button);
        Button new_cancle_button=findViewById(R.id.new_cancle_button);
        final EditText new_name_edittext=findViewById(R.id.new_name_edittext);
        final EditText new_number_edittext=findViewById(R.id.new_number_edittext);
//        Log.d(TAG, "onCreate: start");
        new_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(NewActivity.this, "ok", Toast.LENGTH_SHORT).show();
                Person person=new Person(new_name_edittext.getText().toString(),new_number_edittext.getText().toString());
                Intent intent=new Intent();
                intent.putExtra("new_person",person);
//                Log.d(TAG, "onClick: add new person");
                setResult(RESULT_OK,intent);
                NewActivity.this.finish();
            }
        });
        new_cancle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(NewActivity.this, "cancle", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        new_avatar_imagebutton=findViewById(R.id.new_avatar_imagebutton);
        registerForContextMenu(new_avatar_imagebutton);
        new_avatar_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvatarMenu(new_avatar_imagebutton);
            }
        });
    }
    private void showAvatarMenu(View v){
        PopupMenu popupMenu=new PopupMenu(this,v);
        getMenuInflater().inflate(R.menu.new_avatar,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.new_avatar_menu_camera:
//                        Toast.makeText(NewActivity.this, "正在启动相机", Toast.LENGTH_SHORT).show();
                        takePhoto();
                        break;
                    case R.id.new_avatar_menu_default:
                        Toast.makeText(NewActivity.this, "正在加载预置图片", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.new_avatar_menu_select:
                        Toast.makeText(NewActivity.this, "正在打开图库", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    private void takePhoto(){
        File outputImage=new File(getExternalCacheDir(),"output_image.jpg");
        try{
            if(outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT>=24){
            imageUri= FileProvider.getUriForFile(NewActivity.this,"xyz.tootal.contactsystem.fileprovider",outputImage);
        }else{
            imageUri=Uri.fromFile(outputImage);
        }
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1://take photo
                try{
                    Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    new_avatar_imagebutton.setImageBitmap(bitmap);
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                }
                break;
        }
    }
}
