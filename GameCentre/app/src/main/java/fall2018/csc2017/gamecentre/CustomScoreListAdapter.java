package fall2018.csc2017.gamecentre;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import fall2018.csc2017.gamecentre.R;
import fall2018.csc2017.gamecentre.games.Score;

/**
 * Adapter for each row in scoreboard ListView.
 * Adapted from https://appsandbiscuits.com/listview-tutorial-android-12-ccef4ead27cc
 */
class CustomScoreListAdapter extends ArrayAdapter<Score> {

    //to reference the Activity
    private final Activity context;

    public CustomScoreListAdapter(Activity context, ArrayList<Score> scores) {
        super(context, R.layout.score_listview_row, scores);
        this.context = context;
    }

    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        Score score = getItem(position);

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.score_listview_row, null, true);

        //this code gets references to objects in the listview_row.xml file
        TextView nameTextField = rowView.findViewById(R.id.scoreboard_username);
        TextView scoreTextField = rowView.findViewById(R.id.scoreboard_user_score);
        TextView rankTextField = rowView.findViewById(R.id.scoreboard_user_rank);

        //this code sets the values of the objects to values from the arrays
        nameTextField.setText(score.getName());
        String scoreText = "Score: " + String.valueOf(score.getScoreNum());
        scoreTextField.setText(scoreText);
        rankTextField.setText(String.valueOf(position + 1));
        if (position < 3) { // gold, silver, or bronze color!
            if (position == 0) rankTextField.setTextColor(Color.parseColor("#f1b92f"));
            else if (position == 1) rankTextField.setTextColor(Color.parseColor("#dde0e0"));
            else if (position == 2) rankTextField.setTextColor(Color.parseColor("#df9860"));
        }
        return rowView;

    }

}