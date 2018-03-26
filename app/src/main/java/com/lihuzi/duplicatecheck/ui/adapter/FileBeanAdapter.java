package com.lihuzi.duplicatecheck.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lihuzi.duplicatecheck.ui.activity.FileDetailsActivity;
import com.lihuzi.duplicatecheck.R;
import com.lihuzi.duplicatecheck.model.FileBean;

import java.util.ArrayList;

/**
 * Created by cocav on 2018/3/16.
 */

public class FileBeanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ArrayList<FileBean> _list;

    public FileBeanAdapter()
    {
        _list = new ArrayList<>();
    }

    public void addList(ArrayList<FileBean> list)
    {
        if (list != null)
        {
            _list.clear();
            _list.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void clear()
    {
        _list.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_file, parent, false);
        return new FileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        FileBean model = _list.get(position);
        TextView tv = holder.itemView.findViewById(R.id.viewholder_file_content);
        StringBuilder sb = new StringBuilder(model.name);
        sb.append("\n");
        sb.append(model.toString());
        tv.setText(sb.toString());
        tv.requestLayout();
        tv.setTag(model);
    }

    @Override
    public int getItemCount()
    {
        return _list.size();
    }

    class FileViewHolder extends RecyclerView.ViewHolder
    {
        public FileViewHolder(View itemView)
        {
            super(itemView);
            itemView.findViewById(R.id.viewholder_file_content).setOnClickListener(click);
        }
    }

    private View.OnClickListener click = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            FileBean model = (FileBean) v.getTag();
            Intent i = new Intent(v.getContext(), FileDetailsActivity.class);
            i.putExtra("hash", model.hash);
            v.getContext().startActivity(i);
        }
    };
}
