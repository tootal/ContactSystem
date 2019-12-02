package xyz.tootal.contactsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

class PersonAdapter extends ArrayAdapter<Person> {
    private final int resourceId;

    PersonAdapter(@NonNull Context context, int resource, @NonNull List<Person> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Person person=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view=LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.personName=view.findViewById(R.id.person_name);
            view.setTag(viewHolder);
        }else{
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        assert person != null;
        viewHolder.personName.setText(person.getName());
        return view;
    }

    class ViewHolder{
        TextView personName;
    }
}
