package org.sadhana.simplilyf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sadhana on 10/22/15.
 */
public class PhilipsCustomAdapter extends BaseAdapter{
    private ArrayList nameList;//light name
    private ArrayList stateList; // for state
    private ArrayList colorList; // for c
    private LayoutInflater mLayoutInflater;



    public PhilipsCustomAdapter(Context context, ArrayList arrayList, ArrayList arrayList2,ArrayList arrayList3){

        nameList = arrayList;
        stateList=arrayList2;
        colorList=arrayList3;
        //get the layout inflater
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        //getCount() represents how many items are in the list
        return nameList.size();
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

        // create a ViewHolder reference
        ViewHolder holder;

        //check to see if the reused view is null or not, if is not null then reuse it
        if (view == null) {
            holder = new ViewHolder();

            view = mLayoutInflater.inflate(R.layout.list_philips, null);
            holder.itemName = (TextView) view.findViewById(R.id.title);
            holder.image=(ImageView)view.findViewById(R.id.imgIcon);
            holder.itemColor=(TextView)view.findViewById(R.id.color);
            holder.itemState=(TextView)view.findViewById(R.id.state);
            // the setTag is used to store the data within this view
            view.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)view.getTag();
        }

        //get the string item from the position "position" from array list to put it on the TextView
        String stringItem = nameList.get(position).toString();
        String stringState = stateList.get(position).toString();
        String stringColor = colorList.get(position).toString();
        if (stringItem != null) {
            if (holder.itemName != null) {
                //set the item name on the TextView
                holder.itemName.setText(stringItem);
                holder.itemColor.setText(stringColor);
                holder.itemState.setText(stringState);
                holder.image.setImageResource(R.mipmap.cupcake);
            }
        }

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
        protected TextView itemColor;
        protected TextView itemState;

    }
}
