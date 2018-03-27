package com.lihuzi.duplicatecheck.ui.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lihuzi.duplicatecheck.R;
import com.lihuzi.duplicatecheck.broadcast.LHZBroadcast;
import com.lihuzi.duplicatecheck.broadcast.LHZBroadcastAction;
import com.lihuzi.duplicatecheck.model.FileBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cocav on 2018/3/26.
 */

public class FileRangeAdapter extends RecyclerView.Adapter<FileRangeAdapter.FileRangeViewHolder>
{
    private ArrayList<FileBean> _list;
    private HashMap<String, FileBean> _chooseMap;

    public FileRangeAdapter()
    {
        _list = new ArrayList<>();
    }

    public void setData(ArrayList<FileBean> list)
    {
        _list.clear();
        _list.addAll(list);
        notifyDataSetChanged();
    }

    private void choose(FileBean fileBean)
    {
        if (_chooseMap == null)
        {
            _chooseMap = new HashMap<>();
        }
        if (_chooseMap.containsKey(fileBean.hash))
        {
            _chooseMap.remove(fileBean.hash);
        }
        else
        {
            _chooseMap.put(fileBean.hash, fileBean);
        }
        notifyDataSetChanged();
    }

    public ArrayList<FileBean> getChooseList()
    {
        ArrayList<FileBean> list = new ArrayList<>();
        for (int i = 0; i < _list.size(); i++)
        {
            FileBean fileBean = _list.get(i);
            if (_chooseMap.containsKey(fileBean.hash))
            {
                list.add(fileBean);
            }
        }
        return list;
    }

    public void chooseCancel()
    {
        _chooseMap = null;
        notifyDataSetChanged();
    }

    @Override
    public FileRangeViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_file_range, parent, false);
        return new FileRangeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FileRangeViewHolder holder, int position)
    {
        FileBean fileBean = _list.get(position);
        holder._contentTv.setText(fileBean.toString());
        if (_chooseMap != null)
        {
            holder._chooseCb.setVisibility(View.VISIBLE);
            holder._chooseCb.setChecked(_chooseMap.containsKey(fileBean.hash));
        }
        else
        {
            holder._chooseCb.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount()
    {
        return _list.size();
    }

    class FileRangeViewHolder extends RecyclerView.ViewHolder
    {
        public CheckBox _chooseCb;
        public TextView _contentTv;

        public FileRangeViewHolder(View itemView)
        {
            super(itemView);
            _contentTv = itemView.findViewById(R.id.viewholder_file_range_content_tv);
            _chooseCb = itemView.findViewById(R.id.viewholder_file_range_cb);
            itemView.setOnLongClickListener(new View.OnLongClickListener()
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
                            choose(_list.get(getAdapterPosition()));
                            LHZBroadcast.sendBroadcast(LHZBroadcastAction.ACTION_RANGE_CHOOSE, null);
                        }
                    });
                    builder.show();
                    return true;
                }
            });
            _chooseCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (buttonView.isPressed())
                    {
                        choose(_list.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}
