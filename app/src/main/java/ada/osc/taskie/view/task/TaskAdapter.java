package ada.osc.taskie.view.task;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.model.Task;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by avukelic on 28-Apr-18.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    public interface OnBottomReachedListener {

        void onBottomReached(int position);

    }
    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }

    private List<Task> tasks;
    private TaskClickListener taskClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private boolean allTasks;

    OnBottomReachedListener onBottomReachedListener;


    public TaskAdapter(TaskClickListener listener,OnItemLongClickListener onItemLongClickListener, boolean allTasks) {
        taskClickListener = listener;
        this.onItemLongClickListener = onItemLongClickListener;
        this.allTasks = allTasks;
        tasks = new ArrayList<>();
    }

    public TaskAdapter(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
        tasks = new ArrayList<>();
    }

    public void refreshData(List<Task> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
        notifyDataSetChanged();
    }

    /**
     * View holder class
     */
    @Override
    public void onBindViewHolder(final TaskViewHolder holder, final int position) {
        if (position == tasks.size() - 1){
            onBottomReachedListener.onBottomReached(position);
        }
        Task t = tasks.get(position);
        holder.title.setText(t.getTitle());
        holder.description.setText(t.getDescription());
        switch (t.getPriority()) {
            case 1:
                holder.priority.setImageResource(R.drawable.shape_low_priority);
                break;
            case 2:
                holder.priority.setImageResource(R.drawable.shape_medium_priority);
                break;
            case 3:
                holder.priority.setImageResource(R.drawable.shape_high_priority);
        }
        holder.date.setText(formatDate(new Date(Long.parseLong(t.getDate()))));
        if (allTasks) {
            holder.favoriteBackground.setVisibility(View.GONE);
        } else {
            holder.notFavoriteBackground.setVisibility(View.GONE);
        }
        if (t.isDone()) {
            holder.status.setVisibility(View.GONE);
        } else {
            holder.status.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public Task getItem(int position) {
        return tasks.get(position);
    }

    public void removeItem(int position) {
        tasks.remove(position);
        notifyItemRemoved(position);
    }

    public void addItems(List<Task> newTasks){
        tasks.addAll(newTasks);
        notifyDataSetChanged();
    }

    public void addItem(Task task){
        tasks.add(task);
        notifyDataSetChanged();
    }

    public void updateItem(Task task){
        Task taskForUpdate = tasks.get(tasks.indexOf(task));
        task.setTitle(task.getTitle());
        task.setDescription(task.getDescription());
        task.setDate(task.getDate());
        taskForUpdate.setPriority(task.getPriority());
        notifyDataSetChanged();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(v);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_task_title)
        TextView title;
        @BindView(R.id.textview_task_description)
        TextView description;
        @BindView(R.id.textview_task_date)
        TextView date;
        @BindView(R.id.imageview_task_priority)
        ImageView priority;
        @BindView(R.id.switch_task_status)
        Switch status;
        @BindView(R.id.view_background)
        public RelativeLayout backgroundLayer;
        @BindView(R.id.view_foreground)
        public RelativeLayout foregroundLayer;
        @BindView(R.id.favorite_item)
        LinearLayout notFavoriteBackground;
        @BindView(R.id.remove_item)
        LinearLayout favoriteBackground;


        public TaskViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick
        public void onTaskClick() {
            taskClickListener.onClick(tasks.get(getAdapterPosition()));
        }

        @OnLongClick
        public boolean onLongClick() {
            return onItemLongClickListener.deleteTask(tasks.get(getAdapterPosition()), getAdapterPosition());
        }

        @OnClick(R.id.imageview_task_priority)
        public void changePriorityOnClick() {
            taskClickListener.onPriorityChangeClick(tasks.get(getAdapterPosition()));
        }

        @OnClick(R.id.switch_task_status)
        public void changeStatusOnSwitch() {
            taskClickListener.onStatusSwitchChange(tasks.get(getAdapterPosition()));
        }
    }
    private String formatDate(Date date) {
        SimpleDateFormat sd = new SimpleDateFormat("dd.\nMMM");
        return sd.format(date);
    }

}