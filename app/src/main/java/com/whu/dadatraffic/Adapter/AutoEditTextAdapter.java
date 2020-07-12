package com.whu.dadatraffic.Adapter;
/**
 * author:王子皓
 * create time：2020.07.11
 * 功能为：自定义适配器，为主界面中AutoCompleteTextView提供适配器，调用自定义的item元素
 */
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.whu.dadatraffic.Base.ViewHolder;
import com.whu.dadatraffic.R;

import java.util.ArrayList;
import java.util.List;

public class AutoEditTextAdapter extends BaseAdapter implements Filterable {

    private List<String> mList1;
    private List<String> mList2;
    private Context context;
    private ArrayFilter mFilter;
    private ArrayList<String> mUnfilteredData;

    public AutoEditTextAdapter(List<String> mList1, List<String> mList2, Context context) {
        this.mList1 = mList1;
        this.mList2 = mList2;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mList1 == null ? 0 : mList1.size();
    }

    @Override
    public Object getItem(int position) {
        return mList1.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if(convertView==null){
            view=View.inflate(context, R.layout.autotext_list_item, null);
            holder = new ViewHolder();
            holder.textView1=view.findViewById(R.id.autoTextItemName);
            holder.textView2=view.findViewById(R.id.autoTextItemAddress);

            view.setTag(holder);
        }
        else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        String pc1 = mList1.get(position);
        String pc2 = mList2.get(position);
        holder.textView1.setText(""+pc1);
        holder.textView2.setText(""+pc2);

        return view;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    static class ViewHolder
    {
        TextView textView1;
        TextView textView2;
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (mUnfilteredData == null) {
                mUnfilteredData = new ArrayList<String>(mList1);
            }
            if (prefix == null || prefix.length() == 0) {
                ArrayList<String> list = mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();
                ArrayList<String> unfilteredValues = mUnfilteredData;
                int count = unfilteredValues.size();
                ArrayList<String> newValues = new ArrayList<String>(count);
                for (int i = 0; i < count; i++) {
                    String pc = unfilteredValues.get(i);
                    if (pc != null) {
                        if(pc!=null && pc.startsWith(prefixString)){
                            newValues.add(pc);
                        }else if(pc!=null && pc.startsWith(prefixString)){
                            newValues.add(pc);
                        }
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mList1 = (List<String>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
