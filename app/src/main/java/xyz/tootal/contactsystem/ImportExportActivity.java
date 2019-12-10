package xyz.tootal.contactsystem;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ImportExportActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private TextView import_testdata_textview;
    private TextView import_systemdata_textview;
    private TextView import_json_textview;
    private TextView export_json_textview;
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
        import_json_textview=(TextView) findViewById(R.id.import_json_textview);
        export_json_textview=(TextView) findViewById(R.id.export_json_textview);
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
                Toast.makeText(ImportExportActivity.this, "导入系统通讯录功能尚未实现", Toast.LENGTH_SHORT).show();
            }
        });
        import_json_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ImportExportActivity.this, "导入JSON文件功能尚未实现", Toast.LENGTH_SHORT).show();
            }
        });
        export_json_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ImportExportActivity.this, "导出JSON文件功能尚未实现", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void import_testdata(){
        ArrayList<Person> personList=new ArrayList<>();
        Person person_nezha=new Person("哪吒","10086");
        person_nezha.setNote("哪吒是魔丸转世，李靖之子。因为魔丸转世的身份，他遭到了陈塘关百姓的歧视、排斥、嘲笑和敌对。也因此，他性格孤僻、冷漠、叛逆、憋屈、玩世不恭，时不时就要跑出门大闹陈塘关百姓，让大家也不得安生。玩世不恭的外表下，哪吒比谁都孤独，比谁都渴望认同。");
        person_nezha.setImageUri(imageTranslateUri(R.drawable.nezha));
        personList.add(person_nezha);
        Person person_aobing=new Person("敖丙","10086");
        person_aobing.setNote("敖丙是灵珠转世，东海龙王三太子，申公豹的徒弟。身形飘逸，举止儒雅，一派翩翩美少年形象。他背负整个龙族翻身的期望，全族压力令他痛苦不堪而走上了邪路，做出了冰压陈塘关的举动。敖丙在哪吒的影响下，最终学会敢于做自己、不认命，并与哪吒联手抵抗命运，成为对方“唯一的朋友”。");
        person_aobing.setImageUri(imageTranslateUri(R.drawable.aobing));
        personList.add(person_aobing);
        Person person_lijing=new Person("李靖","10086");
        person_lijing.setNote("李靖是哪吒的父亲，殷夫人的丈夫，陈塘关的镇关总兵，负责守护百姓抵挡妖魔鬼怪。不善言辞、沉默少言。他对哪吒不是排斥的、霸道的，而是主动寻求一种温和、平等的沟通，即便哪吒把陈塘关惹得鸡飞狗跳，他也不惜舍弃自己的情面，帮助哪吒得到世人认可。");
        person_lijing.setImageUri(imageTranslateUri(R.drawable.lijing));
        personList.add(person_lijing);
        Person person_shengongbao=new Person("申公豹","10086");
        person_shengongbao.setNote("元始天尊的弟子之一，阐教门人，太乙真人的师弟，敖丙的师傅，豹子修炼成精的妖魔。一个口齿不伶俐，内心很压抑的角色。他邪恶狡诈，备受天庭偏见而愤愤不平。为了争夺十二金仙的地位，逆天行事，更改了魔丸和灵珠的命运。");
        person_shengongbao.setImageUri(imageTranslateUri(R.drawable.shengongbao));
        personList.add(person_shengongbao);
        Person person_taiyizhenren=new Person("太乙真人","10086");
        person_taiyizhenren.setNote("哪吒的师傅，乾元山金光洞的洞主，阐教大仙，元始天尊的弟子之一。说着一口“川普”，生性洒脱，为人诙谐幽默，不贪念权色却嗜酒如命。元始天尊命徒弟太乙真人将灵珠托生于李靖之子哪吒身上，然而阴差阳错下，灵珠和魔丸被掉包。太乙真人后收哪吒为徒，导其向善。");
        person_taiyizhenren.setImageUri(imageTranslateUri(R.drawable.taiyizhenren));
        personList.add(person_taiyizhenren);
        Person person_yinfuren=new Person("殷夫人","10086");
        person_yinfuren.setNote("哪吒的母亲，李靖的妻子，性格火爆，巾帼不让须眉，在哪吒成长道路上起了重要的引导作用。爱子如命，虽工作忙碌却尽力抽出时间陪伴哪吒成长，她和丈夫李靖对于哪吒均无私付出，但因不被哪吒谅解而遭到怨恨。");
        person_yinfuren.setImageUri(imageTranslateUri(R.drawable.yinfuren));
        personList.add(person_yinfuren);

        Intent intent=new Intent();
        intent.putExtra("import_data", personList);
//        intent.putExtra("SystemContacts","test string");
        setResult(RESULT_OK,intent);
        finish();
    }
    private String imageTranslateUri(int resId) {

        Resources r = getResources();
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(resId) + "/"
                + r.getResourceTypeName(resId) + "/"
                + r.getResourceEntryName(resId));

        return uri.toString();
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
