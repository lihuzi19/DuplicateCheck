package com.lihuzi.duplicatecheck.model;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by cocav on 2018/3/22.
 */

public class StringConverter implements PropertyConverter<ArrayList<String>, String>
{
    @Override
    public ArrayList<String> convertToEntityProperty(String databaseValue)
    {
        if (databaseValue == null)
        {
            return null;
        }
        else
        {
            ArrayList<String> list = new ArrayList<>();
            list.addAll(Arrays.asList(databaseValue.split(",")));
            return list;
        }
    }

    @Override
    public String convertToDatabaseValue(ArrayList<String> entityProperty)
    {
        if (entityProperty == null)
        {
            return null;
        }
        else
        {
            StringBuilder sb = new StringBuilder();
            for (String link : entityProperty)
            {
                sb.append(link);
                sb.append(",");
            }
            return sb.toString();
        }
    }
}
