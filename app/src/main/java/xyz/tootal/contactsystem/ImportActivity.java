package xyz.tootal.contactsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ImportActivity extends AppCompatActivity {
    private static final String TAG = "ImportActivity";
    private ArrayList<Person> personList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        personList=new ArrayList<>();
        setContentView(R.layout.activity_import);
        Button import_system_button=findViewById(R.id.import_system_button);
        import_system_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, "onClick: Read System Contact!");
                if(ContextCompat.checkSelfPermission(ImportActivity.this, Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ImportActivity.this,new String[]{
                            Manifest.permission.READ_CONTACTS
                    },1);
                }else{
                    readSystemContacts();
                }
            }
        });
        Button import_test_button=findViewById(R.id.import_test_button);
        import_test_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTestContacts();
            }
        });
    }

    private void addTestContacts(){
        Log.d(TAG, "addTestContacts: start");
        Person[] persons=new Person[]{
                new Person("建设银行","95533"),
                new Person("招商银行","95555"),
                new Person("中国银行","95566"),
                new Person("华夏银行","95577"),
                new Person("工商银行","95588"),
                new Person("农业银行","95599")
        };
        personList.addAll(Arrays.asList(persons));
        Toast.makeText(this, "add "+String.valueOf(persons.length)+" test contacts.", Toast.LENGTH_SHORT).show();
    }

    private void readSystemContacts(){
        Cursor cursor=null;
        try{
            cursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
            if(cursor!=null){
                while(cursor.moveToNext()){
                    String displayName=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Person person=new Person(displayName,number);
                    personList.add(person);
                }
                Toast.makeText(this, "import "+String.valueOf(personList.size())+" contacts.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor!=null){
                cursor.close();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("SystemContacts",personList);
//        intent.putExtra("SystemContacts","test string");
        setResult(RESULT_OK,intent);
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ok");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    readSystemContacts();
                }else{
                    Toast.makeText(this, "You denied the permisson", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
