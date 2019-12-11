package xyz.tootal.contactsystem;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ImportExportActivity extends AppCompatActivity {

    private List<Person> personList;

    private DrawerLayout mDrawerLayout;
    private TextView import_testdata_textview;
    private TextView import_systemdata_textview;
    private TextView import_json_textview;
    private TextView export_systemdata_textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_export);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_port);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_port);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_cancle);
        }
        findTextView();
        setClickListener();
    }
    private void findTextView(){
        import_testdata_textview=(TextView) findViewById(R.id.import_testdata_textview);
        import_systemdata_textview=(TextView) findViewById(R.id.import_systemdata_textview);
        export_systemdata_textview=(TextView) findViewById(R.id.export_systemdata_textview);
    }

    private void setClickListener(){
        import_testdata_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(ImportExportActivity.this, "导入测试数据功能尚未实现", Toast.LENGTH_SHORT).show();
                import_testdata();
            }
        });
        import_systemdata_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(ImportExportActivity.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ImportExportActivity.this,new String[]{
                            Manifest.permission.READ_CONTACTS
                    },1);
                }else{
                    readSystemContacts();
                }
//                Toast.makeText(ImportExportActivity.this, "导入系统通讯录功能尚未实现", Toast.LENGTH_SHORT).show();
            }
        });
        import_json_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ImportExportActivity.this, "导入JSON文件功能尚未实现", Toast.LENGTH_SHORT).show();
            }
        });
        export_systemdata_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(ImportExportActivity.this, Manifest.permission.WRITE_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ImportExportActivity.this,new String[]{
                            Manifest.permission.WRITE_CONTACTS
                    },2);
                }else{
                    writeSystemContacts();
                }
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    readSystemContacts();
                }else{
                    Toast.makeText(this, "You denied the permisson", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    writeSystemContacts();
                }else{
                    Toast.makeText(this, "You denied the permisson", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private void readSystemContacts(){
        ArrayList<Person> personList=new ArrayList<>();
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

                Intent intent=new Intent();
                intent.putExtra("import_system_data", personList);
//        intent.putExtra("SystemContacts","test string");
                setResult(2,intent);
                finish();
//                Toast.makeText(this, "import "+String.valueOf(personList.size())+" contacts.", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor!=null){
                cursor.close();
            }
        }
    }

    private void writeSystemContacts(){

        personList= DataSupport.findAll(Person.class);

        for(int i=0;i<personList.size();i++){
            ContentValues values = new ContentValues();

            // 向RawContacts.CONTENT_URI空值插入，
            // 先获取Android系统返回的rawContactId
            // 后面要基于此id插入值
            Uri rawContactUri = getBaseContext().getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
            long rawContactId = ContentUris.parseId(rawContactUri);
            values.clear();

            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            // 内容类型
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
            // 联系人名字
            values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, personList.get(i).getName());
            // 向联系人URI添加联系人名字
            getBaseContext().getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
            values.clear();

            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            // 联系人的电话号码
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, personList.get(i).getNumber());
            // 电话类型
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            // 向联系人电话号码URI添加电话号码
            getBaseContext().getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
            values.clear();
        }
        Toast.makeText(ImportExportActivity.this, "成功导出到系统通讯录", Toast.LENGTH_SHORT).show();
        finish();
    }


    private void import_testdata(){
        ArrayList<Person> personList=new ArrayList<>();
        Person person_nezha=new Person("哪吒","10086");
        person_nezha.setNote("哪吒是魔丸转世，李靖之子。因为魔丸转世的身份，他遭到了陈塘关百姓的歧视、排斥、嘲笑和敌对。也因此，他性格孤僻、冷漠、叛逆、憋屈、玩世不恭，时不时就要跑出门大闹陈塘关百姓，让大家也不得安生。玩世不恭的外表下，哪吒比谁都孤独，比谁都渴望认同。");
        person_nezha.setImageUri(Util.imageTranslateUri(this,R.drawable.nezha));
        personList.add(person_nezha);
        Person person_aobing=new Person("敖丙","10086");
        person_aobing.setNote("敖丙是灵珠转世，东海龙王三太子，申公豹的徒弟。身形飘逸，举止儒雅，一派翩翩美少年形象。他背负整个龙族翻身的期望，全族压力令他痛苦不堪而走上了邪路，做出了冰压陈塘关的举动。敖丙在哪吒的影响下，最终学会敢于做自己、不认命，并与哪吒联手抵抗命运，成为对方“唯一的朋友”。");
        person_aobing.setImageUri(Util.imageTranslateUri(this,R.drawable.aobing));
        personList.add(person_aobing);
        Person person_lijing=new Person("李靖","10086");
        person_lijing.setNote("李靖是哪吒的父亲，殷夫人的丈夫，陈塘关的镇关总兵，负责守护百姓抵挡妖魔鬼怪。不善言辞、沉默少言。他对哪吒不是排斥的、霸道的，而是主动寻求一种温和、平等的沟通，即便哪吒把陈塘关惹得鸡飞狗跳，他也不惜舍弃自己的情面，帮助哪吒得到世人认可。");
        person_lijing.setImageUri(Util.imageTranslateUri(this,R.drawable.lijing));
        personList.add(person_lijing);
        Person person_shengongbao=new Person("申公豹","10086");
        person_shengongbao.setNote("元始天尊的弟子之一，阐教门人，太乙真人的师弟，敖丙的师傅，豹子修炼成精的妖魔。一个口齿不伶俐，内心很压抑的角色。他邪恶狡诈，备受天庭偏见而愤愤不平。为了争夺十二金仙的地位，逆天行事，更改了魔丸和灵珠的命运。");
        person_shengongbao.setImageUri(Util.imageTranslateUri(this,R.drawable.shengongbao));
        personList.add(person_shengongbao);
        Person person_taiyizhenren=new Person("太乙真人","10086");
        person_taiyizhenren.setNote("哪吒的师傅，乾元山金光洞的洞主，阐教大仙，元始天尊的弟子之一。说着一口“川普”，生性洒脱，为人诙谐幽默，不贪念权色却嗜酒如命。元始天尊命徒弟太乙真人将灵珠托生于李靖之子哪吒身上，然而阴差阳错下，灵珠和魔丸被掉包。太乙真人后收哪吒为徒，导其向善。");
        person_taiyizhenren.setImageUri(Util.imageTranslateUri(this,R.drawable.taiyizhenren));
        personList.add(person_taiyizhenren);
        Person person_yinfuren=new Person("殷夫人","10086");
        person_yinfuren.setNote("哪吒的母亲，李靖的妻子，性格火爆，巾帼不让须眉，在哪吒成长道路上起了重要的引导作用。爱子如命，虽工作忙碌却尽力抽出时间陪伴哪吒成长，她和丈夫李靖对于哪吒均无私付出，但因不被哪吒谅解而遭到怨恨。");
        person_yinfuren.setImageUri(Util.imageTranslateUri(this,R.drawable.yinfuren));
        personList.add(person_yinfuren);

        Intent intent=new Intent();
        intent.putExtra("import_data", personList);
//        intent.putExtra("SystemContacts","test string");
        setResult(1,intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }

}
