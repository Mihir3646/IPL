package com.ipl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.ipl.R;
import com.ipl.firebase.FirebaseConstant;
import com.ipl.fragment.CreateLeagueFragment;
import com.ipl.fragment.JoinLeagueFragment;
import com.ipl.fragment.MyLeagueFragment;
import com.ipl.model.LeaguePointModel;
import com.ipl.model.MatchResultModel;
import com.ipl.model.PredictedResultModel;
import com.ipl.utils.Utils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout navDrawer;
    private NavigationView navigationView;
    private NavigationView navigationViewDrawer;
    private Intent intent;
    private String leagueName;
    private ArrayList<MatchResultModel> matchResultModelArrayList = new ArrayList<>();
    private ArrayList<PredictedResultModel> predictedResultModelArrayList = new ArrayList<>();
    private ArrayList<LeaguePointModel> leaguePointModelArrayList = new ArrayList<>();
    private int point = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    /**
     * This method is for the initialization of component
     */
    private void initView() {
        intent = getIntent();
        sideDrawerLayout();
        Utils.replaceFragment(this, new CreateLeagueFragment(), R.id.activity_home_container);
        generateLeaderBoard();
    }

    /**
     * Method is to show side drawer
     */
    private void sideDrawerLayout() {
        navDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationViewDrawer = (NavigationView) findViewById(R.id.nav_view_drawer);

        final CircleImageView civUserImage = (CircleImageView) navigationView.findViewById(R.id.drawer_header_menu_img_user_image);
        final TextView tvUserName = (TextView) navigationView.findViewById(R.id.drawer_header_menu_tv_user_name);

        tvUserName.setText(intent.getStringExtra(getString(R.string.db_key_userNickName)));
        Glide.with(this).load(intent.getStringExtra(getString(R.string.db_key_userImageURl))).into(civUserImage);

        final Toolbar iplToolbar = (Toolbar) findViewById(R.id.ipl_toolbar);
        iplToolbar.setTitle(getString(R.string.home));
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, navDrawer, iplToolbar, R.string.drawer_open, R.string.drawer_close);
        navDrawer.addDrawerListener(toggle);

        navigationViewDrawer.setNavigationItemSelectedListener(this);
        navigationViewDrawer.getMenu().getItem(0).setChecked(true);

        toggle.syncState();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.act_home_nav_home:

                break;

            case R.id.act_home_nav_create_league:
                Utils.replaceFragment(this, new CreateLeagueFragment(), R.id.activity_home_container);
                navDrawer.closeDrawer(GravityCompat.START);
                navigationViewDrawer.getMenu().getItem(1).setChecked(true);
                break;

            case R.id.act_home_nav_join_league:
                Utils.replaceFragment(this, new JoinLeagueFragment(), R.id.activity_home_container);
                navDrawer.closeDrawer(GravityCompat.START);
                navigationViewDrawer.getMenu().getItem(2).setChecked(true);
                break;

            case R.id.act_home_nav_my_league:
                Utils.replaceFragment(this, new MyLeagueFragment(), R.id.activity_home_container);
                navDrawer.closeDrawer(GravityCompat.START);
                navigationViewDrawer.getMenu().getItem(3).setChecked(true);
                break;

            case R.id.act_home_nav_log_out:
                navDrawer.closeDrawer(GravityCompat.START);
                navigationViewDrawer.getMenu().getItem(4).setChecked(true);
                final Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return false;
    }

    /**
     * getter setter for pending request league name
     */
    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    /**
     * This method is to generate leaderboard
     */
    private void generateLeaderBoard() {
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        firebase.child(getString(R.string.db_key_matchResult)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    final MatchResultModel matchResultModel = new MatchResultModel();
                    matchResultModel.setMatchId(postSnapshot.getKey());
                    matchResultModel.setWinningTeam(postSnapshot.getValue().toString());
                    matchResultModelArrayList.add(matchResultModel);
                }
                if (matchResultModelArrayList != null) {
                    getPredictions();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    /**
     * This method is to get prediction of league
     */
    private void getPredictions() {
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        firebase.child(getString(R.string.db_key_prediction)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    firebase.child(getString(R.string.db_key_prediction)).child(postSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot postSnapshot1 : dataSnapshot.getChildren()) {
                                firebase.child(getString(R.string.db_key_prediction)).child(postSnapshot.getKey()).child(postSnapshot1.getKey()).
                                        addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot postSnapshot2 : dataSnapshot.getChildren()) {
                                                    final PredictedResultModel predictedResultModel = new PredictedResultModel();
                                                    predictedResultModel.setLeagueName(postSnapshot.getKey());
                                                    predictedResultModel.setMatchId(postSnapshot1.getKey());
                                                    predictedResultModel.setUserId(postSnapshot2.getKey());
                                                    predictedResultModel.setSelectedTeam(postSnapshot2.getValue().toString());
                                                    predictedResultModelArrayList.add(predictedResultModel);
                                                }
//                                                createLeaderBoard();
                                                createLeaderBoard();
                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {

                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    /**
     * This method is to create leaderboard
     */
    private int createLeaderBoard() {
        final Firebase firebase = new Firebase(FirebaseConstant.FIREBASE_URL);
        for (int i = 0; i < matchResultModelArrayList.size(); i++) {
            for (int j = 0; j < predictedResultModelArrayList.size(); j++) {
                if (matchResultModelArrayList.get(i).getMatchId().equals(predictedResultModelArrayList.get(j).getMatchId())) {
                    if (matchResultModelArrayList.get(i).getWinningTeam().equals(predictedResultModelArrayList.get(j).getSelectedTeam())) {
                        firebase.child(getString(R.string.db_key_leaderBoard)).child(predictedResultModelArrayList.get(j).getLeagueName())
                                .child(predictedResultModelArrayList.get(j).getMatchId()).child(predictedResultModelArrayList.get(j).getUserId())
                                .setValue(1, new Firebase.CompletionListener() {
                                    @Override
                                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                                        if (firebaseError != null) {
                                        } else {

                                        }
                                    }
                                });
                    }
                }
            }
        }
        return point;
    }

}
