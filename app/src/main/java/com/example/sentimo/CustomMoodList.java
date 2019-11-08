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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

//// This class is a modified version of the CustomList class in the ListyCity Demo from Lab 3 */
//// I don't know who wrote the ListyCity demo, but the Android Studio .zip file is on eclass at this URL
//// Url == https://eclass.srv.ualberta.ca/course/view.php?id=54165

/**
 * This class represents the format of the Mood objects added to the ListView
 * on the Main Screen.
 * Extends the ArrayAdapter<Mood> class which allows the moods to be specifically
 * added to the ListView.
 */
public class CustomMoodList extends ArrayAdapter<Mood> {

    private ArrayList<Mood> moods;
    private Context context;

    /**
     * Constructor of the CustomMoodList
     * Takes a Context and an ArrayList of moods which tells the parent class
     * that this is the ArrayList of moods that should be represented in the
     * ListView.
     * @param context
     *      This is the context of the ListView.
     * @param moods
     *      This is the ArrayList of moods that should be represented in
     *      the ListView
     */
    public CustomMoodList(Context context, ArrayList<Mood> moods){
        super(context, 0, moods);
        this.moods = moods;
        this.context = context;
    }

    /**
     * This function fits the data provided by each of the mood objects in the
     * Mood List into a .xml file to be displayed in the ListView on the MainScreen.
     * Makes sure that there is a view that represents what each object in the
     * ListView should look like
     * @param position
     *      This is the position of the mood in the Mood List that is to be
     *      displayed in the ListView.
     * @param convertView
     *      This is the design of each object in the ListView. Should be passed
     *      each time the Mood List changes, but the first time it is automatically
     *      set to the specific .xml file.
     * @param parent
     *      This is the ListView that the CustomMoodList should display in.
     * @return view
     *      This is the updated view with the moods in the Mood List added to the
     *      ListView.
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.mood_content, parent, false);
        }

        Mood mood = moods.get(position);
        TextView date = view.findViewById(R.id.mood_date);
        TextView time = view.findViewById(R.id.mood_time);
        ImageView emoji = view.findViewById(R.id.mood_emoji);
        LinearLayout background = view.findViewById(R.id.mood_list_background);

        date.setText(mood.getTime().getDateString());
        time.setText(mood.getTime().getTimeString());
        emoji.setImageResource(mood.getEmotion().getImage());
        background.setBackgroundColor(Color.parseColor(mood.getEmotion().getColour()));

        return view;
    }

}
