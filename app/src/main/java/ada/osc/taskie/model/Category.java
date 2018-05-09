package ada.osc.taskie.model;

import android.graphics.Color;

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
    @DatabaseField(columnName = "color")
    private String mColor;

    public Category() {
        mId = UUID.randomUUID().toString();
    }

    public Category(String mName,String mColor) {
        mId = UUID.randomUUID().toString();
        this.mName = mName;
        this.mColor = mColor;
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

    public String getColor() {
        return mColor;
    }

    public void setColor(String mColor) {
        this.mColor = mColor;
    }

    @Override
    public String toString() {
        return getName();
    }
}
