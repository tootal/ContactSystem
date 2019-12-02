package xyz.tootal.contactsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private List<Person> personList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        initPeople();
        PersonAdapter adapter=new PersonAdapter(MainActivity.this,R.layout.person_item,personList);
        ListView listView=findViewById(R.id.list_view);
        listView.setAdapter(adapter);
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
        for(Person person:persons){
            personList.add(person);
        }
        for(Person person:persons){
            personList.add(person);
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
