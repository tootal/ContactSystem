
package xyz.tootal.contactsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ModifyDetailActivity extends AppCompatActivity{

    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private EditText person_name_editText;
    private  EditText person_number_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_detail);

        dbHelper=new MyDatabaseHelper(this,"Contact.db",null,1);
        db=dbHelper.getWritableDatabase();

        person_name_editText=(EditText) findViewById(R.id.person_name_editext);
        person_number_editText=(EditText) findViewById(R.id.person_number_editext);
        Intent intent=getIntent();
        final Person person=(Person)intent.getSerializableExtra("person");
        person_name_editText.setText(person.getName());
        person_number_editText.setText(person.getNumber());
        Button button =(Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.button:
                        String name=person_name_editText.getText().toString();
                        String number=person_number_editText.getText().toString();
                        db.execSQL("update person set name=?,number=? where id=?",new String[]{name,number,String.valueOf(person.getId())});
                }
                Toast.makeText(ModifyDetailActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
            }
        });
        Button back =(Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ModifyDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
