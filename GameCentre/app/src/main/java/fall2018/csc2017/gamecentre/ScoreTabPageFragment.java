package fall2018.csc2017.gamecentre;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fall2018.csc2017.gamecentre.games.Score;

/**
 * The fragment for each tab page of game scores.
 * Tab Layout setting adapted from
 * https://android.jlelse.eu/tablayout-and-viewpager-in-your-android-app-738b8840c38a
 */
public class ScoreTabPageFragment extends Fragment {

    /**
     * The required empty constructor for fragment.
     */
    public ScoreTabPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_score_page, container, false);

        // Receive game name and difficulty name passed in.
        Bundle b = getArguments();
        assert b != null;
        String[] infoArray = b.getStringArray("info");
        assert infoArray != null;
        String gameName = infoArray[0];
        String difficultyName = infoArray[1];

        // Initialize the list view for each row of score.
        ArrayList<Score> scores = new ArrayList<>();
        final CustomScoreListAdapter adapter = new CustomScoreListAdapter(getActivity(), scores);
        assert gameName != null;
        assert difficultyName != null;
        firebaseGetScore(rootView, gameName, difficultyName, adapter);

        // Set up the listview adapter.
        ListView listView = rootView.findViewById(R.id.fragment_score_page_list_view);
        listView.setAdapter(adapter);

        return rootView;
    }

    /**
     * Asynchronously query the score for the corresponding game name and difficulty name.
     * When score is successfully received from database, update the UI.
     *
     * @param rootView       the score page view.
     * @param gameName       the name of the game given
     * @param difficultyName the difficulty of the game given
     * @param adapter        the listview adapter for displaying scores.
     */
    private void firebaseGetScore(final View rootView, String gameName, String difficultyName,
                                  final CustomScoreListAdapter adapter) {
        // Query the database to find the score.
        DatabaseReference scoreDataRef = FirebaseDatabase.getInstance().getReference
                ("Scoreboards").child(gameName).child(difficultyName);

        Query queryRef = scoreDataRef.orderByChild("scoreNum");
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // If query is successful, update the UI.
                TextView emptyText = rootView.findViewById(R.id.fragment_score_page_empty);
                if (dataSnapshot.exists()) {
                    // Remove the text showing "Nobody has ever played this level...", if there is.
                    emptyText.setText("");
                    Iterable<DataSnapshot> childrens = dataSnapshot.getChildren();
                    List<Score> scoreList = new ArrayList<>();
                    for (DataSnapshot scoreSnapshot : childrens) {
                        Score score = scoreSnapshot.getValue(Score.class);
                        scoreList.add(score);
                    }
                    Collections.reverse(scoreList);
                    adapter.clear();
                    adapter.addAll(scoreList);
                    adapter.notifyDataSetChanged();
                } else {
                    // If nothing found on database, display a text showing nobody played.
                    String emptyString = "Nobody has ever played this level...";
                    emptyText.setText(emptyString);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}