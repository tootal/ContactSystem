package xyz.tootal.contactsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

public class NewActivity extends AppCompatActivity {
    private static final String TAG = "NewActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Button new_confirm_button=findViewById(R.id.new_confirm_button);
        Button new_cancle_button=findViewById(R.id.new_cancle_button);
        final EditText new_name_edittext=findViewById(R.id.new_name_edittext);
        final EditText new_number_edittext=findViewById(R.id.new_number_edittext);
        Log.d(TAG, "onCreate: start");
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
        final ImageButton new_avatar_imagebutton=findViewById(R.id.new_avatar_imagebutton);
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
                        Toast.makeText(NewActivity.this, "正在启动相机", Toast.LENGTH_SHORT).show();
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
}
