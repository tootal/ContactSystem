package xyz.tootal.contactsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private final List<Person> personList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        initPeople();
        PersonAdapter adapter=new PersonAdapter(MainActivity.this,R.layout.person_item,personList);
        ListView listView=findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Person person=personList.get(position);
                Toast.makeText(MainActivity.this, String.valueOf(person.getNumber()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initPeople() {
        Person[] persons=new Person[]{
            new Person("建设银行",95533),
            new Person("招商银行",95555),
            new Person("中国银行",95566),
            new Person("华夏银行",95577),
            new Person("工商银行",95588),
            new Person("农业银行",95599)
        };
        for(int i=0;i<5;i++){
            personList.addAll(Arrays.asList(persons));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add:
                Log.d(TAG, "onOptionsItemSelected: add");
                break;
            case R.id.menu_import:
                Log.d(TAG, "onOptionsItemSelected: import");
                break;
            case R.id.menu_export:
                Log.d(TAG, "onOptionsItemSelected: export");
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
}
