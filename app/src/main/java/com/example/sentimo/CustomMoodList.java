package com.example.sentimo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

//// This class is a modified version of the CustomList class in the ListyCity Demo from Lab 3 */
//// I don't know who wrote the ListyCity demo, but the Android Studio .zip file is on eclass at this URL
//// Url == https://eclass.srv.ualberta.ca/course/view.php?id=54165
public class CustomMoodList extends ArrayAdapter<Mood> {

    private ArrayList<Mood> moods;
    private Context context;

    public CustomMoodList(Context context, ArrayList<Mood> moods){
        super(context, 0, moods);
        this.moods = moods;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.mood_content, parent, false);
        }

        Mood mood = moods.get(position);
        TextView date = view.findViewById(R.id.mood_date);
        TextView time = view.findViewById(R.id.mood_time);
        //TextView emoji = view.findViewById(R.id.mood_emoji);

        date.setText(mood.getTime().getDateString());
        time.setText(mood.getTime().getTimeString());
        //image.setImage(mood.getEmoji());

        return view;
    }

}
