package com.lihuzi.duplicatecheck.ui.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lihuzi.duplicatecheck.R;
import com.lihuzi.duplicatecheck.broadcast.LHZBroadcast;
import com.lihuzi.duplicatecheck.broadcast.LHZBroadcastAction;
import com.lihuzi.duplicatecheck.model.FileBean;
import com.lihuzi.duplicatecheck.ui.activity.FileDetailsActivity;

import java.util.ArrayList;

/**
 * Created by cocav on 2018/4/11.
 */

public class CustomSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ArrayList<FileBean> _list;

    public CustomSearchAdapter()
    {
        _list = new ArrayList<>();
    }

    public void setData(ArrayList<FileBean> list)
    {
        _list.clear();
        _list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_custom_search, parent, false);
        return new CustomSearchHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ((CustomSearchHolder) holder).setFileBean(_list.get(position));
    }

    @Override
    public int getItemCount()
    {
        return _list.size();
    }

    private class CustomSearchHolder extends RecyclerView.ViewHolder
    {
        private FileBean _fileBean;
        private TextView _contentTv;

        public CustomSearchHolder(View itemView)
        {
            super(itemView);
            _contentTv = itemView.findViewById(R.id.viewholder_custom_search_content_tv);
            itemView.setOnClickListener(clickListener);
            itemView.setOnLongClickListener(longClickListener);
        }

        public void setFileBean(FileBean fileBean)
        {
            _fileBean = fileBean;
            refresh();
        }

        private void refresh()
        {
            _contentTv.setText(_fileBean.toString());
        }

        private View.OnClickListener clickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(v.getContext(), FileDetailsActivity.class);
                i.putExtra("hash", _fileBean.hash);
                v.getContext().startActivity(i);
            }
        };
        private View.OnLongClickListener longClickListener = new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setItems(new String[]{"批量处理"}, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
//                        choose(_list.get(getAdapterPosition()));
                        LHZBroadcast.sendBroadcast(LHZBroadcastAction.ACTION_RANGE_CHOOSE, null);
                    }
                });
                builder.show();
                return true;
            }
        };
    }
}
