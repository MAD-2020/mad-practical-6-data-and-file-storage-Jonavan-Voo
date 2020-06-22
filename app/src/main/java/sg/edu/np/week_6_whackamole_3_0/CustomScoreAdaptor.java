package sg.edu.np.week_6_whackamole_3_0;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class CustomScoreAdaptor extends RecyclerView.Adapter<CustomScoreViewHolder> {
    /* Hint:
        1. This is the custom adaptor for the recyclerView list @ levels selection page

     */
    private static final String FILENAME = "CustomScoreAdaptor.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    private UserData userData;
    ArrayList<Integer> levels = new ArrayList<>();
    ArrayList<Integer> scores = new ArrayList<>();
    private Context context;

    public CustomScoreAdaptor(UserData userdata,Context context){
        /* Hint:
        This method takes in the data and readies it for processing.
         */
        this.userData = userdata;
        this.context = context;
        levels = userdata.getLevels();
        scores = userdata.getScores();
    }

    public CustomScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        /* Hint:
        This method dictates how the viewholder layuout is to be once the viewholder is created.
         */
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.level_select,parent,false);
        return new CustomScoreViewHolder(item);
    }

    public void onBindViewHolder(CustomScoreViewHolder holder, final int position){

        /* Hint:
        This method passes the data to the viewholder upon bounded to the viewholder.
        It may also be used to do an onclick listener here to activate upon user level selections.

        Log.v(TAG, FILENAME + " Showing level " + level_list.get(position) + " with highest score: " + score_list.get(position));
        Log.v(TAG, FILENAME+ ": Load level " + position +" for: " + list_members.getMyUserName());
         */
        final Integer level = levels.get(position);
        Integer score = scores.get(position);
        holder.level.setText("Level " + level);
        holder.highscore.setText("Highest Score: " + score);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Main4Activity.class);
                intent.putExtra("level",levels.get(position));
                intent.putExtra("username",userData.getMyUserName());
                context.startActivity(intent);
            }
        });
        Log.v(TAG, FILENAME + " Showing level " + levels.get(position) + " with highest score: " + scores.get(position));
        Log.v(TAG, FILENAME+ ": Load level " + position +" for: " + userData.getMyUserName());
    }

    public int getItemCount(){
        /* Hint:
        This method returns the the size of the overall data.
         */
        return levels.size();
    }
}