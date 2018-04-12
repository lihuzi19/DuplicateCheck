package com.lihuzi.duplicatecheck.ui.adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lihuzi.duplicatecheck.R;
import com.lihuzi.duplicatecheck.db.FileDB;
import com.lihuzi.duplicatecheck.model.FileBean;
import com.lihuzi.duplicatecheck.utils.LHZFileUtils;

import java.io.File;

/**
 * Created by cocav on 2018/3/20.
 */

public class FileDetailsPathAdapter extends
        RecyclerView.Adapter<FileDetailsPathAdapter.FileDetailsPathViewHolder>
{
    public FileDetailsPathAdapter(String hash)
    {
        _hash = hash;
        initPathList();
    }

    private void initPathList()
    {
        _FileBean = FileDB.getFileBean(_hash);
    }

    private FileBean _FileBean;
    private String _hash;

    @Override
    public FileDetailsPathViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_file_details_path, parent, false);
        return new FileDetailsPathViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FileDetailsPathViewHolder holder, int position)
    {
        TextView pathTv = holder.itemView.findViewById(R.id.viewholder_file_details_path_tv);
        String path = _FileBean.path.get(position);
        pathTv.setText(path);
        pathTv.setTag(path);
    }

    @Override
    public int getItemCount()
    {
        return _FileBean.path.size();
    }

    class FileDetailsPathViewHolder extends RecyclerView.ViewHolder
    {

        public FileDetailsPathViewHolder(View itemView)
        {
            super(itemView);
            itemView.findViewById(R.id.viewholder_file_details_path_tv).setOnClickListener(click);
            itemView.findViewById(R.id.viewholder_file_details_path_tv).setOnLongClickListener(longClickListener);
        }
    }

    private View.OnClickListener click = new View.OnClickListener()
    {
        @Override
        public void onClick(final View v)
        {
            String path = (String) v.getTag();
            File f = new File(path);
            if (f.exists())
            {
                LHZFileUtils.openFile(v.getContext(), f);
            }
        }
    };
    private View.OnLongClickListener longClickListener = new View.OnLongClickListener()
    {
        @Override
        public boolean onLongClick(final View v)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setItems(new String[]{"删除"}, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    String path = (String) v.getTag();
                    File f = new File(path);
                    if (f.exists())
                    {
                        f.delete();
                    }
                    for (String s : _FileBean.path)
                    {
                        if (path.equals(s))
                        {
                            _FileBean.path.remove(s);
                        }
                    }
                    FileDB.update(_FileBean);
                    initPathList();
                    notifyDataSetChanged();
                }
            });
            builder.show();
            return true;
        }
    };
}
