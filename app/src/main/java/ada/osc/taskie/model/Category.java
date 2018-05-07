package ada.osc.taskie.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.UUID;

/**
 * Created by avukelic on 07-May-18.
 */
@DatabaseTable(tableName = "categories")
public class Category {

    @DatabaseField(id = true, columnName = "id")
    private String mId;
    @DatabaseField(columnName = "name")
    private String mName;

    public Category() {
        mId = UUID.randomUUID().toString();
    }

    public Category(String mName) {
        mId = UUID.randomUUID().toString();
        this.mName = mName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    @Override
    public String toString() {
        return getName();
    }
}
