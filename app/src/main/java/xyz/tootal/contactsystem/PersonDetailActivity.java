package xyz.tootal.contactsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PersonDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        TextView person_name_textview=findViewById(R.id.person_name_textview);
        TextView person_number_textview=findViewById(R.id.person_number_textview);
        Intent intent=getIntent();
        Person person=(Person)intent.getSerializableExtra("person");
        person_name_textview.setText(person.getName());
        person_number_textview.setText(person.getNumber());
    }
}
