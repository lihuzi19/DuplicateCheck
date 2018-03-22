package com.lihuzi.duplicatecheck;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        _fileModel = FileDB.getFileModel(_hash);
        if (_fileModel != null)
        {
            _list = _fileModel.path.split(",");
            if (_list.length == 0)
            {
                _list = new String[]{_fileModel.path};
            }
        }
        else
        {
            _list = new String[]{};
        }
    }

    private FileModel _fileModel;
    private String[] _list;
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
        String path = _list[position];
        pathTv.setText(path);
        pathTv.setTag(path);
    }

    @Override
    public int getItemCount()
    {
        return _list.length;
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
                LHZFileUtils.openFile(((Activity) v.getContext()), f);
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
                    String path_list = _fileModel.path;
                    if (path_list.contains("," + path))
                    {
                        _fileModel.path = path_list.replace("," + path, "");
                    }
                    else if (path_list.contains(path + ","))
                    {
                        _fileModel.path = path_list.replace(path + ",", "");
                    }
                    else if (path_list.contains(path))
                    {
                        _fileModel.path = path_list.replace(path, "");
                    }
                    FileDB.update(_fileModel);
                    initPathList();
                    notifyDataSetChanged();
                }
            });
            builder.show();
            return true;
        }
    };
}
