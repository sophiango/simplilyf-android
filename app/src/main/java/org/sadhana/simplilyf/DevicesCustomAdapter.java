package org.sadhana.simplilyf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Sadhana on 10/22/15.
 */
public class DevicesCustomAdapter extends BaseAdapter{
    private final String[] name;
    private final Integer[] imgid;
    private LayoutInflater mLayoutInflater;

    public DevicesCustomAdapter(Context context, String[] itemname,Integer[] imgId){
        System.out.println("In COnstructor");
        this.name=itemname;
        this.imgid=imgId;
        //get the layout inflater
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    //get the data of an item from a specific position
    //i represents the position of the item in the list
    public Object getItem(int i) {
        return null;
    }

    @Override
    //get the position id of the item from the list
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        System.out.println("view method");
        // create a ViewHolder reference
        ViewHolder holder;

        //check to see if the reused view is null or not, if is not null then reuse it
        if (view == null) {
            holder = new ViewHolder();
            System.out.println("layout id"+ R.layout.list_devices);
            view = mLayoutInflater.inflate(R.layout.list_devices, null);
            holder.itemName = (TextView) view.findViewById(R.id.list_item_text_view);
            holder.image=(ImageView)view.findViewById(R.id.imgIcon);

            // the setTag is used to store the data within this view
            view.setTag(holder);
            System.out.println("holder value");
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)view.getTag();
        }

        //get the string item from the position "position" from array list to put it on the TextView

        System.out.println("In Vie holder");


                //set the item name on the TextView
                holder.itemName.setText(name[position]);

                holder.image.setImageResource(imgid[position]);


        System.out.println("In Vie holder with details " );
        //this method must return the view corresponding to the data at the specified position.
        return view;

    }

    /**
     * Static class used to avoid the calling of "findViewById" every time the getView() method is called,
     * because this can impact to your application performance when your list is too big. The class is static so it
     * cache all the things inside once it's created.
     */
    private static class ViewHolder {

        protected TextView itemName;
        protected ImageView image;



    }
}
