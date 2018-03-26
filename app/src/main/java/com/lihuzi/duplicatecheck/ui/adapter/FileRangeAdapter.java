package com.lihuzi.duplicatecheck.ui.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
        _chooseMap = new HashMap<>();
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
        TextView _content = holder.itemView.findViewById(R.id.viewholder_file_range_content_tv);
        _content.setText(fileBean.toString());
        CheckBox cb = holder.itemView.findViewById(R.id.viewholder_file_range_cb);
        if (_chooseMap != null)
        {
            cb.setVisibility(View.VISIBLE);
            cb.setChecked(_chooseMap.containsKey(fileBean.hash));
        }
        else
        {
            cb.setVisibility(View.GONE);

        }
    }


    @Override
    public int getItemCount()
    {
        return _list.size();
    }

    class FileRangeViewHolder extends RecyclerView.ViewHolder
    {

        public FileRangeViewHolder(View itemView)
        {
            super(itemView);
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
                            LHZBroadcast.sendBroadcast(LHZBroadcastAction.ACTION_RANGE_CHOOSE, null);
                        }
                    });
                    builder.show();
                    return true;
                }
            });
        }
    }
}
