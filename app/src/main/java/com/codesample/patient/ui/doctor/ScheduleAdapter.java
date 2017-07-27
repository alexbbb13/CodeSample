package com.codesample.newpatient.ui.doctor;

/**
 * Created by Acer on 26.06.2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codesample.newpatient.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by baneizalfe on 8/3/16.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DoctorContract.ScheduleView{

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    List<Schedule> list;

    public ScheduleAdapter(Context context) {
        this.list = new ArrayList<>();
    }

    public void setList(List<Schedule> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<Schedule> getList() {
        return list;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.description)
        TextView description;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Schedule schedule, int position) {
            title.setText(schedule.title);
            description.setText(String.format("%s - %s", schedule.startTime, schedule.endTime));
        }
    }

    public class FooterHolder extends RecyclerView.ViewHolder {
        public FooterHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()) return TYPE_FOOTER;
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_FOOTER)
            return new FooterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_schedule_list_footer, parent, false));

        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_schedule_list_item, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemHolder) {
            final Schedule schedule = list.get(position);
            ((ItemHolder) holder).bind(schedule, position);
             holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DoctorPresenter.getInstance().showScheduleDialog(schedule);
                    }
                });
        }

        if (holder instanceof FooterHolder) {
               holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DoctorPresenter.getInstance().onFooterSelected();
                    }
                });
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

}