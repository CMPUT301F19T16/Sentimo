package com.example.sentimo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomFriendMoodList extends ArrayAdapter<Mood> {

    private ArrayList<Mood> moods;
    private Context context;

    public CustomFriendMoodList(Context context, ArrayList<Mood> moods){
        super(context, 0, moods);
        this.moods = moods;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.friend_mood_content, parent, false);
        }

        Mood mood = moods.get(position);
        TextView date = view.findViewById(R.id.mood_date);
        TextView time = view.findViewById(R.id.mood_time);
        ImageView emoji = view.findViewById(R.id.mood_emoji);
        LinearLayout background = view.findViewById(R.id.mood_list_background);

        TextView usernameTextView = view.findViewById(R.id.friend_name_textview);
        usernameTextView.setText(mood.getUsername());


        date.setText(mood.getTime().getDateString());
        time.setText(mood.getTime().getTimeString());
        emoji.setImageResource(mood.getEmotion().getImage());
        background.setBackgroundColor(Color.parseColor(mood.getEmotion().getColour()));

        return view;
    }
}
