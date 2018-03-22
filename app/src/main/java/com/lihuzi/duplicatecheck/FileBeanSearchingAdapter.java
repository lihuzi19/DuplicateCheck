package com.lihuzi.duplicatecheck;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lihuzi.duplicatecheck.model.FileBean;

import java.util.ArrayList;

/**
 * Created by cocav on 2018/3/17.
 */

public class FileBeanSearchingAdapter extends
        RecyclerView.Adapter<FileBeanSearchingAdapter.SearchingViewHolder>
{
    private ArrayList<FileBean> _list;

    public FileBeanSearchingAdapter()
    {
        _list = new ArrayList<>();
    }

    public void add(FileBean model)
    {
        _list.add(0, model);
        notifyDataSetChanged();
    }

    public void clear()
    {
        _list.clear();
        notifyDataSetChanged();
    }

    @Override
    public SearchingViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_searching_file, parent, false);
        return new SearchingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SearchingViewHolder holder, int position)
    {
        TextView content = holder.itemView.findViewById(R.id.viewholder_searching_file_content);
        content.setText(_list.get(position).toString());
    }

    @Override
    public int getItemCount()
    {
        return _list.size();
    }

    class SearchingViewHolder extends RecyclerView.ViewHolder
    {

        public SearchingViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
