package com.lihuzi.duplicatecheck.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by cocav on 2018/3/27.
 */

public class ChooseFolderAdapter extends
        RecyclerView.Adapter<ChooseFolderAdapter.ChooseFolderViewHolder>
{
    private ArrayList<String> _list;

    @Override
    public ChooseFolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public void onBindViewHolder(ChooseFolderViewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return 0;
    }

    class ChooseFolderViewHolder extends RecyclerView.ViewHolder
    {
        public ChooseFolderViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
