package mchehab.com.customlistviewimage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muhammadchehab on 10/29/17.
 */

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Person> listPersons;
    private List<Person> listPersonsFilter;

    private List<Person> listPersonsSelected;
    private List<View> listSelectedRows;

    public ListViewAdapter(Context context, List<Person> listPersons){
        this.context = context;
        this.listPersons = listPersons;
        listPersonsFilter = new ArrayList<>(listPersons);
        listPersonsSelected = new ArrayList<>();
        listSelectedRows = new ArrayList<>();
    }

    public void addItem(Person person){
        listPersons.add(person);
        listPersonsFilter.add(person);
    }

    public void setItem(int position, Person person){
        for(int i=0;i<listPersons.size();i++){
            if(listPersons.get(i).equals(listPersonsFilter.get(position))){
                listPersons.set(i, person);
                break;
            }
        }
        listPersonsFilter.set(position, person);
    }

    public void handleLongPress(int position, View view){
        if(listSelectedRows.contains(view)){
            listSelectedRows.remove(view);
            listPersonsSelected.remove(listPersons.get(position));
            view.setBackgroundResource(R.color.colorWhite);
        }else{
            listPersonsSelected.add(listPersons.get(position));
            listSelectedRows.add(view);
            view.setBackgroundResource(R.color.colorDarkGray);
        }
    }

    public List<Person> getListPersonsSelected(){
        return listPersonsSelected;
    }

    public void removeSelectedPersons(){
        listPersons.removeAll(listPersonsSelected);
        listPersonsFilter.removeAll(listPersonsSelected);
        listPersonsSelected.clear();
        for(View view : listSelectedRows)
            view.setBackgroundResource(R.color.colorWhite);
        listSelectedRows.clear();
    }

    @Override
    public int getCount() {
        return listPersonsFilter.size();
    }

    @Override
    public Object getItem(int position) {
        return listPersonsFilter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.row_listview, null);

            viewHolder = new ViewHolder();

            viewHolder.imageViewProfilePic = convertView.findViewById(R.id.imageViewProfilePic);
            viewHolder.textViewName = convertView.findViewById(R.id.textViewName);
            viewHolder.textViewDescription = convertView.findViewById(R.id.textViewDescription);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Person person = listPersonsFilter.get(position);

        viewHolder.textViewName.setText(person.getFirstName() + " " + person.getLastName());
        viewHolder.textViewDescription.setText(person.getDescription());
        viewHolder.imageViewProfilePic.setImageDrawable(getImageDrawable(person.getImageName()));

        int resource = listPersonsSelected.contains(person) ? R.color.colorDarkGray:R.color.colorWhite;
        convertView.setBackgroundResource(resource);

        return convertView;
    }

    public void filter(String text){
        text = text.toLowerCase();
        listPersonsFilter.clear();
        if(text.length() == 0){
            listPersonsFilter.addAll(listPersons);
        }else{
            String name;
            for(Person person : listPersons){
                name = (person.getFirstName() + " " + person.getLastName()).toLowerCase();
                if(name.contains(text)){
                    listPersonsFilter.add(person);
                }
            }
        }

        notifyDataSetChanged();
    }

    private Drawable getImageDrawable(String imageName){
        int id  = context.getResources().getIdentifier(imageName, "drawable",
                context.getPackageName());
        return context.getResources().getDrawable(id);
    }

    class ViewHolder{
        ImageView imageViewProfilePic;
        TextView textViewName;
        TextView textViewDescription;
    }
}