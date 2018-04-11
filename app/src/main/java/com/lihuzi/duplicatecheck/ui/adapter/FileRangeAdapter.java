package com.lihuzi.duplicatecheck.ui.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.lihuzi.duplicatecheck.ui.activity.FileDetailsActivity;
import com.lihuzi.duplicatecheck.utils.FileLengthUtils;

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

    public boolean chooseCancel()
    {
        if (_chooseMap != null)
        {
            _chooseMap = null;
            notifyDataSetChanged();
            return true;
        }
        else
        {
            return false;
        }
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
        holder.setFileBean();
    }


    @Override
    public int getItemCount()
    {
        return _list.size();
    }

    class FileRangeViewHolder extends RecyclerView.ViewHolder
    {
        public CheckBox _chooseCb;
        public TextView _nameTv;
        public TextView _pathTv;
        public TextView _sizeTv;
        public TextView _duplicateCountTv;

        public FileRangeViewHolder(View itemView)
        {
            super(itemView);
            _nameTv = itemView.findViewById(R.id.viewholder_file_range_name_tv);
            _pathTv = itemView.findViewById(R.id.viewholder_file_range_content_tv);
            _sizeTv = itemView.findViewById(R.id.viewholder_file_range_size_tv);
            _duplicateCountTv = itemView.findViewById(R.id.viewholder_file_range_duplicate_count_tv);
            _chooseCb = itemView.findViewById(R.id.viewholder_file_range_cb);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (_chooseMap != null)
                    {
                        choose(_list.get(getAdapterPosition()));
                    }
                    else
                    {
                        Intent i = new Intent(v.getContext(), FileDetailsActivity.class);
                        i.putExtra("hash", _list.get(getAdapterPosition()).hash);
                        v.getContext().startActivity(i);
                    }
                }
            });
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

        public void setFileBean()
        {
            FileBean fileBean = _list.get(getAdapterPosition());
            _nameTv.setText(fileBean.getName());
            _pathTv.setText("文件路径: " + fileBean.getPath().toString());
            _sizeTv.setText("文件大小: " + FileLengthUtils.getLengthStr(fileBean.getLength()));
            _duplicateCountTv.setText("文件数量: " + fileBean.getDuplicateCount());
            if (_chooseMap != null)
            {
                _chooseCb.setVisibility(View.VISIBLE);
                _chooseCb.setChecked(_chooseMap.containsKey(fileBean.hash));
            }
            else
            {
                _chooseCb.setVisibility(View.GONE);
            }
        }
    }
}
