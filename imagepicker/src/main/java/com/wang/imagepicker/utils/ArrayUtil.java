package com.wang.imagepicker.utils;

import com.wang.imagepicker.model.Photo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by wang on 2016/1/15.
 */
public class ArrayUtil{


    @SuppressWarnings("unchecked")
    public static <T extends Photo> void deepCopy(List<T> src, Collection<T> dest){
        for (T aSrc : src) {
            try {
                dest.add((T) aSrc.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> deepCopy(List<T> src){
        List<T> dest = null;
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            dest = (List<T>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dest;
    }
}
