package ada.osc.andrejvukeliccv;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by avukelic on 25-Apr-18.
 */
public class KeyAchivementsAdapter extends ArrayAdapter<KeyAchivement> {

    public KeyAchivementsAdapter(@NonNull Context context, List<KeyAchivement> achivements){
        super(context, 0, achivements);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.key_achivements_list_item, parent, false);
        }

        KeyAchivement currentAchivement = getItem(position);

        ImageView achivementImage = listItemView.findViewById(R.id.achivement_image);
        achivementImage.setImageResource(currentAchivement.getImageId());

        TextView achivementTitle = listItemView.findViewById(R.id.achivement_title);
        achivementTitle.setText(currentAchivement.getName());

        TextView achivementText = listItemView.findViewById(R.id.achivement_text);
        achivementText.setText(currentAchivement.getText());

        return listItemView;
    }
}
