/*
 *作者：施武轩 创建时间：2020.7.15
 * update: 2020.7.16
 */

package com.whu.dadatraffic.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Iterator;
import java.util.Map;

//负责将数据存放到本地文件
public class LocalStorageUtil {

    /**
     * 将字符串数据保存到本地
     * @param context 上下文
     * @param filename 生成XML的文件名
     * @param map <生成XML中每条数据名,需要保存的数据>
     */
    public static void saveSettingNote(Context context, String filename , Map<String, String> map) {
        SharedPreferences.Editor note = context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
            note.putString(entry.getKey(), entry.getValue());
        }
        note.commit();
    }

    /**
     * 从本地取出要保存的数据
     * @param context 上下文
     * @param filename 文件名
     * @param dataname 生成XML中每条数据名
     * @return 对应的数据(找不到为空字符串)
     */
    public static String getSettingNote(Context context,String filename ,String dataname) {
        SharedPreferences read = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return read.getString(dataname, "");
    }

    /**
     * 将本地的某条记录删除
     * @param context 上下文
     * @param filename 文件名
     * @param dataname 需要删除的数据名
     */
    public static void deleteSettingNote(Context context,String filename ,String dataname){
        SharedPreferences.Editor note = context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        note.remove(dataname);
        note.commit();
    }

    /**
     * 清空本地记录
     * @param context 上下文
     * @param filename 文件名
     */
    public static void clearSettingNote(Context context,String filename){
        SharedPreferences.Editor note = context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit();
        note.clear();
        note.commit();
    }
}
