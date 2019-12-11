package xyz.tootal.contactsystem;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder>{

    private static final String TAG = "PersonAdapter";

    private Context mContext;

    private List<Person> mPersonList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView personImage;
        TextView personName;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            personImage = (ImageView) view.findViewById(R.id.person_image);
            personName = (TextView) view.findViewById(R.id.person_name);
        }
    }

    public PersonAdapter(List<Person> personList) {
        mPersonList = personList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.person_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Person person = mPersonList.get(position);
                Intent intent=new Intent(mContext,PersonActivity.class);
                intent.putExtra("person",person);
                mContext.startActivity(intent);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showPersonMenu(view,holder);
                return true;
            }
        });
        return holder;
    }
    private void showPersonMenu(View v,ViewHolder holder){
        final ViewHolder holder1=holder;
        PopupMenu popupMenu=new PopupMenu(mContext,v);
        popupMenu.getMenuInflater().inflate(R.menu.main_longclick,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.person_menu_show:
//                        Toast.makeText(mContext, "查看联系人", Toast.LENGTH_SHORT).show();

                        int position = holder1.getAdapterPosition();
                        Person person = mPersonList.get(position);
                        Intent intent=new Intent(mContext,PersonActivity.class);
                        intent.putExtra("person",person);
                        mContext.startActivity(intent);
                        break;
                    case R.id.person_menu_edit:
//                        Toast.makeText(mContext, "编辑联系人", Toast.LENGTH_SHORT).show();

                        int PersonPosition = holder1.getAdapterPosition();
                        Person personDetail = mPersonList.get(PersonPosition);
                        Intent DetailIntent=new Intent(mContext,ModifyDetailActivity.class);
                        DetailIntent.putExtra("person",personDetail);
                        ((MainActivity) mContext).startActivityForResult(DetailIntent,1);
//                        ((MainActivity) mContext).refreshPersons();
                        break;
                    case R.id.person_menu_delete:
//                        Toast.makeText(mContext, "删除联系人", Toast.LENGTH_SHORT).show();
                        int position1 = holder1.getAdapterPosition();
                        Person person1 = mPersonList.get(position1);
                        DataSupport.deleteAll(Person.class,"id = ?",String.valueOf(person1.getId()));
                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                        ((MainActivity) mContext).refreshPersons();
                        break;
                    case R.id.person_menu_share:
                        Toast.makeText(mContext, "分享联系人", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Person person = mPersonList.get(position);
        holder.personName.setText(person.getName());
        Glide.with(mContext).load(person.getImageUri()).into(holder.personImage);
    }

    @Override
    public int getItemCount() {
        return mPersonList.size();
    }

}
