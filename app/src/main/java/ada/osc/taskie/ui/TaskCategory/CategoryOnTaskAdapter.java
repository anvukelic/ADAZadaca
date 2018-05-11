package ada.osc.taskie.ui.TaskCategory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.Consts;
import ada.osc.taskie.R;
import ada.osc.taskie.database.DatabaseHelper;
import ada.osc.taskie.model.Category;
import ada.osc.taskie.model.TaskCategory;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by avukelic on 28-Apr-18.
 */
public class CategoryOnTaskAdapter extends RecyclerView.Adapter<CategoryOnTaskAdapter.TaskCategoryViewHolder> {
    private List<Category> mCategories;
    Context mContext;


    public CategoryOnTaskAdapter(List<Category> categories, Context context) {
        mCategories = categories;
        mContext = context;
    }

    /**
     * View holder class
     */
    @Override
    public void onBindViewHolder(final TaskCategoryViewHolder holder, final int position) {
        Category c = mCategories.get(position);
        holder.mCategory.setText(c.getName());
        changeColorOnCategory(holder, c);
    }

    private void changeColorOnCategory(TaskCategoryViewHolder holder, Category c) {
        switch (c.getColor()) {
            case Consts.COLOR_RED:
                holder.mCategory.setBackgroundColor(mContext.getResources().getColor(R.color.colorCategoryRed));
                break;
            case Consts.COLOR_GREEN:
                holder.mCategory.setBackgroundColor(mContext.getResources().getColor(R.color.colorCategoryGreen));
                break;
            case Consts.COLOR_BLUE:
                holder.mCategory.setBackgroundColor(mContext.getResources().getColor(R.color.colorCategoryBlue));
                break;
            case Consts.COLOR_PURPLE:
                holder.mCategory.setBackgroundColor(mContext.getResources().getColor(R.color.colorCategoryPurple));
                break;
            case Consts.COLOR_ORANGE:
                holder.mCategory.setBackgroundColor(mContext.getResources().getColor(R.color.colorCategoryOrange));
                break;
            default:
                holder.mCategory.setBackgroundColor(mContext.getResources().getColor(R.color.colorCategoryBlack));
        }
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    @Override
    public TaskCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_on_task, parent, false);
        return new TaskCategoryViewHolder(v);
    }

    public class TaskCategoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_category_on_task_name)
        TextView mCategory;


        public TaskCategoryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}