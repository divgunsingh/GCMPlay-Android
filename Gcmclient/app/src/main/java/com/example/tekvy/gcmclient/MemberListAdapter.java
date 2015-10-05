package com.example.tekvy.gcmclient;

/**
 * Created by tekvy on 6/5/15.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.ViewHolder>{
    private List<MemberListModel> modelList;
    public Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtHeader;
        public TextView txtFooter;
        public ImageView image;


        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.title);
            txtFooter = (TextView) v.findViewById(R.id.email_footer);
            image= (ImageView) v.findViewById(R.id.thumbnail);
        }
    }

    public void add(MemberListModel model,int pos) {
        modelList.add(model);
        notifyItemInserted(pos);
    }

    public void remove(String item) {
        //int position = mDataset.indexOf(item);
        // mDataset.remove(position);
        // notifyItemRemoved(position);
    }

    public MemberListAdapter(List<MemberListModel> modelList,Context context) {
        this.context=context;
        this.modelList= modelList;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MemberListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.txtHeader.setText(modelList.get(i).getName());
       viewHolder.txtFooter.setText(modelList.get(i).getEmail());

    }


    @Override
    public int getItemCount() {
        return (null != modelList ? modelList.size() : 0);
    }
}
