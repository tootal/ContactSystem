package xyz.tootal.contactsystem;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class PersonActivity extends AppCompatActivity {

    public static final String PERSON_NAME = "person_name";

    public static final String PERSON_IMAGE_URI = "person_image_uri";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Intent intent=getIntent();
        Person person=(Person)intent.getSerializableExtra("person");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView personImageView = (ImageView) findViewById(R.id.person_image_view);
        TextView person_number_textview=(TextView)findViewById(R.id.person_number_textview);
        TextView personContentText = (TextView) findViewById(R.id.person_note_textview);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(person.getName());

        Glide.with(this).load(person.getImageUri()).into(personImageView);

        person_number_textview.setText(person.getNumber());
        personContentText.setText(person.getNote());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
