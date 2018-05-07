package ada.osc.taskie.ui.category;

import ada.osc.taskie.model.Category;
import ada.osc.taskie.model.Task;

/**
 * Created by avukelic on 30-Apr-18.
 */
public interface CategoryClickListener {
    void onClick(Category category);
    boolean onLongClick(Category category);
}
