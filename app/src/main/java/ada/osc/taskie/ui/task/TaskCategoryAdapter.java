package ada.osc.taskie.ui.task;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.model.Category;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by avukelic on 28-Apr-18.
 */
public class TaskCategoryAdapter extends RecyclerView.Adapter<TaskCategoryAdapter.TaskCategoryViewHolder> {
    private List<Category> mCategories;
    private TaskCategoryClickListener mListener;

    public TaskCategoryAdapter(TaskCategoryClickListener listener) {
        mListener = listener;
        mCategories = new ArrayList<>();
    }

    public void refreshData(List<Category> categories) {
        mCategories.clear();
        mCategories.addAll(categories);
        notifyDataSetChanged();
    }

    /**
     * View holder class
     */
    @Override
    public void onBindViewHolder(final TaskCategoryViewHolder holder, final int position) {
        Category c = mCategories.get(position);
        holder.mName.setText(c.getName());
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    @Override
    public TaskCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new TaskCategoryViewHolder(v, mListener);
    }

    public class TaskCategoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_category_name)
        TextView mName;
        @BindView(R.id.imageview_category_selected)
        ImageView mCategorySelected;

        public TaskCategoryViewHolder(View view, TaskCategoryClickListener taskCategoryClickListener) {
            super(view);
            ButterKnife.bind(this, view);
            mName.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            mCategorySelected.getLayoutParams().height = 55;
        }

        @OnClick
        public void onCategoryClick(){
            if(mCategorySelected.getVisibility()==View.GONE){
                mCategorySelected.setVisibility(View.VISIBLE);
            } else {
                mCategorySelected.setVisibility(View.GONE);
            }
            mListener.onClick(mCategories.get(getAdapterPosition()));
        }

    }

}