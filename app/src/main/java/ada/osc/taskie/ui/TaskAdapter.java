package ada.osc.taskie.ui;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.TaskClickListener;
import ada.osc.taskie.model.Task;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by avukelic on 28-Apr-18.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> mTasks;
    private TaskClickListener mListener;

    public TaskAdapter(TaskClickListener listener) {
        mListener = listener;
        mTasks = new ArrayList<>();
    }
    public void refreshData(List<Task> tasks) {
        mTasks.clear();
        mTasks.addAll(tasks);
        notifyDataSetChanged();
    }

    /**
     * View holder class
     */


    @Override
    public void onBindViewHolder(final TaskViewHolder holder, final int position) {
        Task t = mTasks.get(position);
        holder.mTitle.setText(t.getTitle());
        holder.mDescription.setText(t.getDescription());
        switch (t.getPriority()) {
            case 0:
                holder.mPrioritiy.setImageResource(R.drawable.shape_low_priority);
                break;
            case 1:
                holder.mPrioritiy.setImageResource(R.drawable.shape_medium_priority);
                break;
            case 2:
                holder.mPrioritiy.setImageResource(R.drawable.shape_high_priority);
        }
        holder.mDate.setText(formatDate(t.getDate()));
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(v);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_recyclerview_title) TextView mTitle;
        @BindView(R.id.textview_recyclerview_description) TextView mDescription;
        @BindView(R.id.textview_recyclerview_date) TextView mDate;
        @BindView(R.id.imageview_recyclerview_priority) ImageView mPrioritiy;
        @BindView(R.id.textview_recyclerview_item_menu) TextView mItemMenu;

        public TaskViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        @OnClick(R.id.textview_recyclerview_item_menu)
        public void onTaskOptionsClick(View view){
            mListener.onClick(mTasks.get(getAdapterPosition()),R.id.textview_recyclerview_item_menu,view);
        }
        @OnClick(R.id.imageview_recyclerview_priority)
        public void changePriorityOnClick(){
            mListener.onClick(mTasks.get(getAdapterPosition()),R.id.imageview_recyclerview_priority, null);
        }
    }

    private String formatDate(Date date) {
        SimpleDateFormat sd = new SimpleDateFormat("dd.\nMMM");
        return sd.format(date);
    }


}