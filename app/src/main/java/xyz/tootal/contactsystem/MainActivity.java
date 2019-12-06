package xyz.tootal.contactsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
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
    private PersonAdapter personAdapter;
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        initSharedPrefs();
        dbHelper=new MyDatabaseHelper(this,"Contact.db",null,1);
        db=dbHelper.getWritableDatabase();
        personAdapter=new PersonAdapter(MainActivity.this,R.layout.person_item,personList);
        initPersons();
        ListView listView=findViewById(R.id.list_view);
        listView.setAdapter(personAdapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Person person=personList.get(position);
//                Toast.makeText(MainActivity.this, person.getNumber(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,PersonDetailActivity.class);
                intent.putExtra("person",person);
                startActivity(intent);
            }
        });
    }

    private void initSharedPrefs(){
//        SharedPreferences pref=getSharedPreferences("data",MODE_PRIVATE);
//        maxId=pref.getInt("maxId",0);
    }

    @Override
    protected void onPause() {
        saveSharedPrefs();
        super.onPause();
    }

    private void saveSharedPrefs(){
//        SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
//        editor.putInt("maxId",0);
//        editor.apply();
    }

    private void initPersons(){
//        Log.d(TAG, "initPersons: start");
        personList.clear();
        Cursor cursor=db.rawQuery("select id,name,number from person",null);
        if(cursor.moveToFirst()){
            do{
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                String name=cursor.getString(cursor.getColumnIndex("name"));
                String number=cursor.getString(cursor.getColumnIndex("number"));
                Person person=new Person(id,name,number);
                personList.add(person);
            }while(cursor.moveToNext());
        }
        cursor.close();
        personAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)menuInfo;
        Person person=personList.get(info.position);
        menu.setHeaderTitle(person.getName());
        menu.add(0,1,1,"编辑");
        menu.add(0,2,1,"多选");
        menu.add(0,3,1,"删除");
        menu.add(0,4,1,"分享");
    }

    private void deletePerson(int pos){
//        Log.d(TAG, "deletePerson: pos="+String.valueOf(pos));
        db.execSQL("delete from person where id=?",new String[]{String.valueOf(personList.get(pos).getId())});
        personList.remove(pos);
        personAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()){
            case 1://编辑
                Toast.makeText(this, "编辑功能尚未实现", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "多选功能尚未实现", Toast.LENGTH_SHORT).show();
                break;
            case 3:
//                Toast.makeText(this, "删除功能尚未实现", Toast.LENGTH_SHORT).show();
                deletePerson(info.position);
                break;
            case 4:
                Toast.makeText(this, "分享功能尚未实现", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add:
//                Log.d(TAG, "onOptionsItemSelected: add");
                Intent intent2=new Intent(MainActivity.this,NewActivity.class);
                startActivityForResult(intent2,2);
                break;
            case R.id.menu_import:
//                Log.d(TAG, "onOptionsItemSelected: import");
                Intent intent=new Intent(MainActivity.this,ImportActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.menu_export:
                Toast.makeText(this, "导出功能尚未实现", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "onOptionsItemSelected: export");
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        Log.d(TAG, "onActivityResult: begin");
        switch (requestCode){
            case 1://import persons
                if(resultCode==RESULT_OK){
//                    Log.d(TAG, "onActivityResult: 1");
//                    Toast.makeText(this, "get "+data.getStringExtra("SystemContacts"), Toast.LENGTH_SHORT).show();
                    ArrayList systemContactList=(ArrayList<Person>) data.getSerializableExtra("SystemContacts");
                    for(int i=0;i<systemContactList.size();i++){
                        Person person=(Person)systemContactList.get(i);
                        db.execSQL("insert into person(name,number) values(?,?)",new String[]{
                                person.getName(),
                                person.getNumber()
                        });
                    }
                    initPersons();
//                    personList.addAll(systemContactList);
//                    personAdapter.notifyDataSetChanged();
//                    Log.d(TAG, "onActivityResult: added");
                }
                break;
            case 2://add new person
                if(resultCode==RESULT_OK){
//                    Log.d(TAG, "onActivityResult: start2");
                    Person person=(Person)data.getSerializableExtra("new_person");
                    db.execSQL("insert into person(name,number) values(?,?)",new String[]{
                            person.getName(),
                            person.getNumber()
                    });
//                    personList.add(person);
//                    personAdapter.notifyDataSetChanged();
                    initPersons();
                }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
}
