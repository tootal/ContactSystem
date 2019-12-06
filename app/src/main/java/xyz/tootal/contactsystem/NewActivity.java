package xyz.tootal.contactsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    }
}
