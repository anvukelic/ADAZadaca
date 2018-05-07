package ada.osc.taskie.ui.category;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.model.Category;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by avukelic on 28-Apr-18.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.TaskViewHolder> {
    private List<Category> mCategories;
    private CategoryClickListener mListener;

    public CategoryAdapter(CategoryClickListener listener) {
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
    public void onBindViewHolder(final TaskViewHolder holder, final int position) {
        Category c = mCategories.get(position);
        holder.mName.setText(c.getName());
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new TaskViewHolder(v, mListener);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_category_name)
        TextView mName;

        public TaskViewHolder(View view, CategoryClickListener categoryClickListener) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick
        public void onCategoryClick(){
            mListener.onClick(mCategories.get(getAdapterPosition()));
        }

        @OnLongClick
        public boolean onLongClick(){
            return mListener.onLongClick(mCategories.get(getAdapterPosition()));
        }
    }

}