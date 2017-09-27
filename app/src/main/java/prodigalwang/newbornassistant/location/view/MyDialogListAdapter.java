package prodigalwang.newbornassistant.location.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import prodigalwang.newbornassistant.R;
import prodigalwang.newbornassistant.bean.SearchInfo;

/**
 * Created by ProdigalWang on 2017/1/10.
 */

public class MyDialogListAdapter extends BaseAdapter{

    private List<SearchInfo> searchInfoList;
    private Context context;
    public MyDialogListAdapter(List<SearchInfo> searchInfoList,Context context){
        this.context=context;
        this.searchInfoList=searchInfoList;
    }

    @Override
    public int getCount() {
        return searchInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private ViewHolder viewHolder;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final SearchInfo mSearchInfo = (SearchInfo) getItem(position);

        if (convertView==null){
            convertView = View.inflate(context,R.layout.dialog_list_item, null);
            viewHolder=new ViewHolder();
            viewHolder.desnameTv=(TextView) convertView.findViewById(R.id.desname);
            viewHolder.addressTv=(TextView) convertView.findViewById(R.id.address);
            viewHolder.ivMapView= (ImageView) convertView.findViewById(R.id.iv_map_view);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.desnameTv.setText(mSearchInfo.getDesname());
        viewHolder.addressTv.setText(mSearchInfo.getAddress());
        viewHolder.ivMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PanoramaActivity.class);
                intent.putExtra("panoramaLatLng", new double[]{mSearchInfo.getLongtiude(), mSearchInfo.getLatitude()});
                context.startActivity(intent);
            }
        });
        return convertView;
    }


    private class ViewHolder{
        private TextView desnameTv;
        private TextView addressTv;
        private ImageView ivMapView;
    }
}
