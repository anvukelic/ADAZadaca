package ada.osc.taskie.ui.category;

import android.app.DialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import ada.osc.taskie.Consts;
import ada.osc.taskie.R;
import ada.osc.taskie.database.DatabaseHelper;
import ada.osc.taskie.ui.task.TaskHelper;
import ada.osc.taskie.model.Category;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryActivity extends AppCompatActivity implements CategoryDialogFragment.onCategoryChangeListener {
    public static final String CREATE_CATEGORY_TAG = "createCategoryDialog";

    @BindView(R.id.recycler_view_categories)
    RecyclerView mRecyclerView;

    private DatabaseHelper databaseHelper = null;

    private CategoryAdapter mAdapter;
    private Dao<Category, String> categoryDao;
    CategoryClickListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.categories);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        setupFloatingButton();
        try {
            databaseHelper = new DatabaseHelper(this);
            categoryDao = databaseHelper.getCategoryDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setUpTaskClickListener();
        setUpRecyclerView();

    }
    private void setUpTaskClickListener() {
        mListener = new CategoryClickListener() {
            @Override
            public void onClick(Category category) {
                showTaskDialogFragment(Consts.UPDATE_ACTION, category.getId());
            }

            @Override
            public boolean onLongClick(Category category) {
                CategoryHelper.deleteCategory(category, CategoryActivity.this, categoryDao);
                mAdapter.refreshData(getCategories());
                return false;
            }
        };
    }
    private void setUpRecyclerView() {
        mAdapter = new CategoryAdapter(mListener);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.refreshData(getCategories());
    }

    //Setup floating button to showTaskDialogFragment(create action)
    private void setupFloatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTaskDialogFragment(Consts.CREATE_ACTION, null);
            }
        });
    }

    //Show task dialog
    //Two different calls: create and update
    private void showTaskDialogFragment(int action, String categoryId) {
        DialogFragment createCategoryDialogFragment = new CategoryDialogFragment();
        Bundle args = new Bundle();
        args.putInt("action", action);
        if (action == Consts.UPDATE_ACTION) {
            args.putString("categoryId", categoryId);
        }
        createCategoryDialogFragment.setArguments(args);
        createCategoryDialogFragment.show(this.getFragmentManager(), CREATE_CATEGORY_TAG);
    }

    public List<Category> getCategories(){
        return CategoryHelper.getCategories(categoryDao);
    }

    @Override
    public void onCategoryChange(int action) {
        mAdapter.refreshData(getCategories());
    }
}