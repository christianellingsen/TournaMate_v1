package com.dtu.tournamate_v1;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dtu.tournamate_v1.createNewTournament.NewTournament_frag;
import com.dtu.tournamate_v1.createNewTournament.TournamentReady_frag;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by ce on 18-04-2016.
 */
public class TournamentRecyclerAdapter extends RecyclerView.Adapter<TournamentRecyclerAdapter.ViewHolder> {

    private String TAG = "T_RecyclerAdaper";

    private Handler handler = new Handler();
    private Fragment fragment;
    private boolean doneFetchingFirebase = false;
    private String tID;
    private ProgressDialog progressDialog;

    private ArrayList<Match> fetchedMatchesFirebase = new ArrayList();
    private ArrayList<String> pendingDelete = new ArrayList();
    private ArrayList<Tournament> mDataset;

    private Tournament pendingDeleteTournament;
    private Firebase myFirebaseRef = new Firebase(MyApplication.firebase_URL);

    private Paint p = new Paint();
    private RecyclerView recyclerView;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View mView;
        TextView tName;
        TextView tDate;
        TextView tOpen;
        TextView tStatus;

        public ViewHolder(View v) {
            super(v);
            tName = (TextView) v.findViewById(R.id.tournament_list_name);
            tDate = (TextView) v.findViewById(R.id.tournament_list_date);
            tOpen = (TextView) v.findViewById(R.id.tournament_list_open);
            tStatus = (TextView) v.findViewById(R.id.tournament_list_status);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TournamentRecyclerAdapter(ArrayList<Tournament> myDataset, Fragment fragment, RecyclerView recyclerView) {
        mDataset = myDataset;
        this.fragment = fragment;
        this.recyclerView = recyclerView;
        initSwipe();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TournamentRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tournament_listelement, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Tournament t = mDataset.get(position);
        holder.tName.setText(t.getName());
        holder.tDate.setText(t.getCreatedAt());
        if (t.getIsOpenToJoin()){
            holder.tOpen.setText("Open");
        }
        else {holder.tOpen.setText("Closed");}

        if (t.getIsStarted()){
            if (t.getIsDone()){
                holder.tStatus.setText("Done");
            }
            else {
                holder.tStatus.setText("Started");
            }
        }
        else {
            holder.tStatus.setText("Not started");
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w(TAG, "You clicked on " + position);
                //mRecycleViewAdapter.getRef(position).removeValue();
                tID = MyApplication.getUser().getStoredTournamentsID().get(position).toString();
                Log.w(TAG, "You clicked on t ID " + tID);
                fetchTournament();
                fetchFromFireBase();

                //fetchMatches(tID);

            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.RIGHT){

                    Snackbar snackbar = Snackbar
                            .make(fragment.getView(), "Tournament is deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    undoDeleteTournament(position);
                                }
                            });

                    snackbar.show();
                    Log.d(TAG, "Deleting: " + position);
                    deleteTournament(position);
                    //view = viewHolder.itemView;
                    //removeView();

                } else {
                    Snackbar snackbar = Snackbar
                            .make(fragment.getView(), "Added to favourites.. if it was implemented..", Snackbar.LENGTH_LONG);

                    snackbar.show();
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(fragment.getResources().getColor(R.color.colorPrimary));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(fragment.getResources(), R.drawable.ic_delete_white_48dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(fragment.getResources().getColor(R.color.colorAccent));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(fragment.getResources(), R.drawable.ic_favorite_black_48dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void deleteTournament(int i) {

        Log.d(TAG,"delete called on element i: "+i);
        ArrayList<String> tArray = MyApplication.getUser().getStoredTournamentsID();
        Log.d(TAG, "Array: " + tArray.toString());
        String toDelete = tArray.get(i);
        Log.d(TAG, "To delete: " + toDelete);
        pendingDelete.clear();
        pendingDelete.add(toDelete);

        tArray.remove(toDelete);

        pendingDeleteTournament = mDataset.get(i);
        mDataset.remove(i);


        //SharedPreferences prefs = getActivity().getSharedPreferences("com.dtu.tournamate_v1", Context.MODE_PRIVATE);
        //prefs.edit().putStringSet("tournaments", new HashSet<String>(MyApplication.getUser().getStoredTournamentsID())).commit();

        Log.d(TAG, "Update firebase: " + tArray);
        Firebase userRef = myFirebaseRef.child(MyApplication.usersString);
        userRef.child(MyApplication.getUser().getU_ID()).child("storedTournamentsID").setValue(tArray);

        // animation
        remove(i);

    }


    public void undoDeleteTournament(int posistion) {

        //ArrayList<String> tArray = new ArrayList<>(MyApplication.getUser().getStoredTournamentsID());
        Log.d(TAG, "undo delete called on posistion: " + posistion);
        String undoDelete = pendingDelete.get(0);
        Log.d(TAG,"pending delete : "+ pendingDelete.get(0));
        Log.d(TAG,"t list before: "+ MyApplication.getUser().getStoredTournamentsID().toString());
        MyApplication.getUser().getStoredTournamentsID().add(posistion,undoDelete);
        Log.d(TAG, "t list after: " + MyApplication.getUser().getStoredTournamentsID().toString());

        //SharedPreferences prefs = getActivity().getSharedPreferences("com.dtu.tournamate_v1", Context.MODE_PRIVATE);
        //prefs.edit().putStringSet("tournaments", new HashSet<String>(MyApplication.getUser().getStoredTournamentsID())).commit();

        Firebase userRef = myFirebaseRef.child(MyApplication.usersString);
        userRef.child(MyApplication.getUser().getU_ID()).setValue(MyApplication.getUser());

        mDataset.add(posistion,pendingDeleteTournament);

        // animation
        add(posistion);
    }

    public void add(int pos){
        notifyItemInserted(pos);
    }

    public void remove(int pos){
        notifyItemRemoved(pos);
    }


    public void fetchTournament() {

        MyApplication.resumingTournament = true;
        MyApplication.matchList.clear();
        MyApplication.teams.clear();
        fetchedMatchesFirebase.clear();

        Firebase matchRef = myFirebaseRef.child(MyApplication.matchesString);
        Firebase tournamentRef = myFirebaseRef.child(MyApplication.tournamentsString);
        Firebase teamsRef = myFirebaseRef.child(MyApplication.teamsString);

        Query t_query = tournamentRef.orderByChild("t_ID").equalTo(tID);

        t_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Tournament listener called: ");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    //String t_objectID = (String) child.getKey();
                    //if (t_objectID.equals(tID)) {
                        Tournament t = child.getValue(Tournament.class);
                        MyApplication.setActiveTournament(t);
                        Log.d(TAG, "Tournament found: " + MyApplication.getActiveTournament().getT_ID());
                   // }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

        Query te_query = teamsRef.orderByChild("tournamentID").equalTo(tID);

        te_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Team listener called: ");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Team t = child.getValue(Team.class);
                        MyApplication.teams.add(t);
                        Log.d(TAG, "Team found: " + t.getTeamName());
                }
                Log.d(TAG, "Done fetching teams");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "Team fetch error: " + firebaseError.getMessage());
            }
        });

        Query m_query = matchRef.orderByChild("tournamentID").equalTo(tID);

        m_query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Log.d("Firebase", "Match listener called: ");
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Match m = child.getValue(Match.class);
                            Log.d(TAG, "Match found: " + (String) child.getKey());
                            fetchedMatchesFirebase.add(m);
                    }
                    MyApplication.matchList = fetchedMatchesFirebase;
                    doneFetchingFirebase = true;
                    Log.d(TAG, "Done fetching matches, and added to Myapplication");

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d(TAG, "Match fetch error: " + firebaseError.getMessage());
                }
            });
    }


    public void fetchFromFireBase() {
        progressDialog = ProgressDialog.show(fragment.getActivity(), "",
                "Loading. Please wait...", true);
        progressDialog.setCancelable(true);
        new Thread(resumeWhenReady).start();
    }

    public final Runnable resumeWhenReady = new Runnable() {

        @Override
        public void run() {
            //Log.d("Firebase", "Runnable call");
            if (doneFetchingFirebase ) {
                Log.d(TAG, "doneFetchingFirebase true ");
                Log.d(TAG, "Size of matchlist:" + MyApplication.matchList.size() + " and number of matches: " + MyApplication.getActiveTournament().getNumberOfMatches());

                // If dummy match still in data base, delete it

                if (MyApplication.matchList.size()>MyApplication.getActiveTournament().getNumberOfMatches()){
                    for (Match m : MyApplication.matchList){
                        if (m.getMatchTitle().equals("Dummy")){
                            myFirebaseRef.child(MyApplication.matchesString).child(m.getMatchID()).setValue(null);
                            MyApplication.matchList.remove(m);
                            Log.d(TAG,"Removed dummy match");
                            break;
                        }
                    }
                }


                if ((MyApplication.matchList.size() == MyApplication.getActiveTournament().getNumberOfMatches()&& MyApplication.matchList.size() > 0) ) {
                    Log.d(TAG, "come on");
                    progressDialog.dismiss();

                    if (MyApplication.getActiveTournament().getIsStarted()) {
                        fragment.getFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, new NewTournament_frag())
                                .addToBackStack(null)
                                .commit();
                    }
                    else {
                        fragment.getFragmentManager().beginTransaction()
                                .replace(R.id.main_frame, new TournamentReady_frag())
                                .addToBackStack(null)
                                .commit();
                    }
                    ((MainMenu_akt)fragment.getActivity()).fabOnOff(0);

                }
                else {
                    Log.d(TAG, "handler postDelay");
                    handler.postDelayed(resumeWhenReady, 1000);
                }

            } else {
                Log.d(TAG, "handler postDelay");
                handler.postDelayed(resumeWhenReady, 1000);

            }
        }
    };



}


