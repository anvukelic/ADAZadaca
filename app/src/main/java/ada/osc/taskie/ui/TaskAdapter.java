package ada.osc.taskie.ui;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.model.Task;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by avukelic on 28-Apr-18.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {
    private List<Task> mTasks;

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_recyclerview_title)
        TextView mTitle;
        @BindView(R.id.textview_recyclerview_description)
        TextView mDescription;
        @BindView(R.id.textview_recyclerview_date)
        TextView mDate;
        @BindView(R.id.imageview_recyclerview_priority)
        ImageView mPrioritiy;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public TaskAdapter(List<Task> tasks) {
        this.mTasks = tasks;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Task t = mTasks.get(position);
        holder.mTitle.setText(t.getTitle());
        holder.mDescription.setText(t.getDescription());
        switch (t.getPriority()){
            case 1:
                holder.mPrioritiy.setImageResource(R.drawable.shape_low_priority);
                break;
            case 2:
                holder.mPrioritiy.setImageResource(R.drawable.shape_medium_priority);
                break;
            default:
                holder.mPrioritiy.setImageResource(R.drawable.shape_high_priority);
        }

        holder.mDate.setText(formatDate(t.getDate()));
        holder.mDate.setText(formatDate(t.getDate()));

    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new MyViewHolder(v);
    }

    public void refreshData(List<Task> tasks) {
        mTasks.clear();
        mTasks.addAll(tasks);
        notifyDataSetChanged();
    }

    private String formatDate(Date date){
        SimpleDateFormat sd = new SimpleDateFormat("dd.\nMMM");
        return sd.format(date);
    }

}
