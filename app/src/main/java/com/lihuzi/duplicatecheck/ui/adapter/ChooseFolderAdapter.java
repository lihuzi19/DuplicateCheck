package com.lihuzi.duplicatecheck.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lihuzi.duplicatecheck.R;
import com.lihuzi.duplicatecheck.utils.LHZCallback;

import java.util.ArrayList;

/**
 * Created by cocav on 2018/3/27.
 */

public class ChooseFolderAdapter extends
        RecyclerView.Adapter<ChooseFolderAdapter.ChooseFolderViewHolder>
{
    private ArrayList<String> _list;
    private LHZCallback<String> _nextCallback;
    private String _choosePath;

    public ChooseFolderAdapter()
    {
        _list = new ArrayList<>();
    }

    public void setData(ArrayList<String> list)
    {
        _list.clear();
        _list.addAll(list);
        notifyDataSetChanged();
    }

    public void setCallback(LHZCallback<String> callback)
    {
        _nextCallback = callback;
    }

    public String getChoosePath()
    {
        return _choosePath;
    }

    @Override
    public ChooseFolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_choose_folder, parent, false);
        return new ChooseFolderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChooseFolderViewHolder holder, int position)
    {
        String path = _list.get(position);
        holder._path.setText(path.substring(path.lastIndexOf("/") + 1));
        holder._path.setTag(path);
        holder._cb.setChecked(_choosePath != null && _choosePath.equals(path));
    }

    @Override
    public int getItemCount()
    {
        return _list.size();
    }

    class ChooseFolderViewHolder extends RecyclerView.ViewHolder
    {
        public CheckBox _cb;
        public TextView _path;

        public ChooseFolderViewHolder(View itemView)
        {
            super(itemView);
            _cb = itemView.findViewById(R.id.viewholder_choose_folder_cb);
            _path = itemView.findViewById(R.id.viewholder_choose_folder_path_tv);
            _path.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    String path = (String) v.getTag();
                    _nextCallback.callback(path);
                }
            });
            _cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (buttonView.isPressed())
                    {
                        String path = (String) _path.getTag();
                        if (_choosePath != null && _choosePath.equals(path))
                        {
                            _choosePath = null;
                        }
                        else
                        {
                            _choosePath = path;
                        }
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

}
