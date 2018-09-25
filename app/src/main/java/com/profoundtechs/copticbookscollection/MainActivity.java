package com.profoundtechs.copticbookscollection;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    ContentAdapter contentAdapter;
    List<Content> contentList;
    ListView lvMain,lvDrawer;
    Button btnCenter;
    ImageButton btnPrev,btnNext;
    List<String> book1,book2,book3,book4,book5,book6,book7,book8;

    ExpandableListAdapter expandableListAdapter;
    ExpandableListView elvDrawer;
    List<String> listBookTitles;
    HashMap<String,List<String>> listSubTitles;

    private DrawerLayout dlMain;
    private ActionBarDrawerToggle mToggle;
    private Toolbar toolbarMain;

    private int groupPosition,childPosition;
    private int previousGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Displaying the toolbar
        toolbarMain=(Toolbar)findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbarMain);

        //Displaying the navigation drawer and drawer toggle
        dlMain=(DrawerLayout)findViewById(R.id.dlMain);
        mToggle=new ActionBarDrawerToggle(this,dlMain,R.string.open,R.string.close);
        dlMain.addDrawerListener(mToggle);
        mToggle.syncState();

        //Displaying the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Expandable list adaptor
        elvDrawer=(ExpandableListView)findViewById(R.id.elvDrawer);
        prepareListData();
        expandableListAdapter=new ExpandableListAdapter(this,listBookTitles,listSubTitles);
        elvDrawer.setAdapter(expandableListAdapter);

        lvMain=(ListView)findViewById(R.id.lvMain);
        btnPrev=(ImageButton)findViewById(R.id.btnPrev);
        btnNext=(ImageButton)findViewById(R.id.btnNext);
        btnCenter=(Button)findViewById(R.id.btnCenter);

        //Event when a child item is clicked on the expandable list on the navigation drawer
        elvDrawer.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                MainActivity.this.groupPosition=groupPosition;
                MainActivity.this.childPosition=childPosition;
                contentList=databaseHelper.getListContent(listBookTitles.get(groupPosition),childPosition);
                contentAdapter=new ContentAdapter(MainActivity.this,contentList);
                lvMain.setAdapter(contentAdapter);
                getSupportActionBar().setTitle(listBookTitles.get(groupPosition));
                dlMain.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        //Event when a group item is clicked on the expandable list on the navigation drawer
        elvDrawer.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long id) {
                if (expandableListView.isGroupExpanded(groupPosition)){
                    expandableListView.collapseGroup(groupPosition);
                } else {
                    if (groupPosition!=previousGroup){
                        expandableListView.collapseGroup(previousGroup);
                    }
                    previousGroup=groupPosition;
                    expandableListView.expandGroup(groupPosition);
                }
                expandableListView.smoothScrollToPosition(groupPosition);
                return true;
            }
        });

        //Event when the previous arrow is clicked
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (childPosition>0){
                    childPosition--;
                    contentList=databaseHelper.getListContent(listBookTitles.get(groupPosition),childPosition);
                    contentAdapter=new ContentAdapter(MainActivity.this,contentList);
                    lvMain.setAdapter(contentAdapter);
                }
            }
        });

        //Event when the next arrow is clicked
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (childPosition+1<expandableListAdapter.getChildrenCount(groupPosition)){
                    childPosition++;
                    contentList=databaseHelper.getListContent(listBookTitles.get(groupPosition),childPosition);
                    contentAdapter=new ContentAdapter(MainActivity.this,contentList);
                    lvMain.setAdapter(contentAdapter);
                }
            }
        });

        //Event when the back to top button is clicked
        btnCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvMain.smoothScrollToPositionFromTop(0,0);
            }
        });

        //Code for copying the database to the device
        databaseHelper=new DatabaseHelper(this);
        File database=getApplicationContext().getDatabasePath(DatabaseHelper.DB_NAME);
        if (!database.exists()){
            databaseHelper.getReadableDatabase();
            if (copyDatabase(this)){
            }else {
                Toast.makeText(this, "Error occurred during copying database", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //Code for displaying content when the application launches for the first time
        SharedPreferences sharedPreferences = getSharedPreferences("readerPosition", Context.MODE_PRIVATE);
        groupPosition=sharedPreferences.getInt("groupPosition",7);
        childPosition=sharedPreferences.getInt("childPosition",0);
        getSupportActionBar().setTitle(listBookTitles.get(groupPosition));
        contentList=databaseHelper.getListContent(listBookTitles.get(groupPosition),childPosition);
        contentAdapter=new ContentAdapter(MainActivity.this,contentList);
        lvMain.setAdapter(contentAdapter);
    }

    //Method for copying database (Used above)
    public boolean copyDatabase(Context context){
        try{
            InputStream inputStream=context.getAssets().open(DatabaseHelper.DB_NAME);
            String outFileName=DatabaseHelper.DB_LOCATION+DatabaseHelper.DB_NAME;
            OutputStream outputStream=new FileOutputStream(outFileName);
            byte[] buff=new byte[1024];
            int length=0;
            while ((length=inputStream.read(buff))>0){
                outputStream.write(buff,0,length);
            }
            outputStream.flush();
            outputStream.close();
            Log.v("MainActivity","Database Copied!");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    //Creates options main at the top right
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //Code for toggling the navigation drawer and event on the options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        if (item.getItemId()==R.id.start){
            startActivity(new Intent(MainActivity.this,StartActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Code for saving the reader position to display it when a user opens the app next time
        SharedPreferences sharedPreferences = getSharedPreferences("readerPosition",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("groupPosition",groupPosition);
        editor.putInt("childPosition",childPosition);
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {

            //show start activity
            startActivity(new Intent(MainActivity.this, StartActivity.class));
            finish();
        }
    }

    //Method for handling list of books and book chapters
    private void prepareListData() {
        listBookTitles=new ArrayList<>();
        listSubTitles=new HashMap<>();

        listBookTitles.add("A Day with Lord Jesus");
        listBookTitles.add("Life of Faith");
        listBookTitles.add("Life of Thanksgiving");
        listBookTitles.add("Lord, How?");
        listBookTitles.add("Return to God");
        listBookTitles.add("Tears in Spiritual Life");
        listBookTitles.add("Ten Concepts");
        listBookTitles.add("The Release of the Spirit");

        book1=new ArrayList<>();
        book1.add("Preface");
        book1.add("Dear reader");
        book1.add("When I wake up...");
        book1.add("While washing my face...");
        book1.add("While dressing up...");
        book1.add("While having breakfast...");
        book1.add("When reading the Bible...");
        book1.add("Morning prayer...");
        book1.add("Handling everyday tasks...");
        book1.add("During work...");
        book1.add("At noon...");
        book1.add("Burdens and heat ...");
        book1.add("At the dinner table...");
        book1.add("Rest and tranquillity...");
        book1.add("With friends...");
        book1.add("The ninth hour...");
        book1.add("Evening song...");
        book1.add("Supper with Christ...");
        book1.add("By night...");
        book1.add("Unclothing...");
        book1.add("Going to bed...");

        book2=new ArrayList<>();
        book2.add("Cover");
        book2.add("Introduction");
        book2.add("How great faith is");
        book2.add("What is faith?");
        book2.add("Degrees and kinds of faith");
        book2.add("The relationship...peace...");
        book2.add("The relationship...purity...");
        book2.add("Simplicity of faith");
        book2.add("Obedience of faith...");
        book2.add("What strengthens the faith");
        book2.add("What weakens the faith");
        book2.add("The test of faith ...");

        book3=new ArrayList<>();
        book3.add("The Story of This Book");
        book3.add("Life of thanksgiving");
        book3.add("Fields of thanksgiving");
        book3.add("Virtues relating to...");
        book3.add("Why we give thanks?");

        book4=new ArrayList<>();
        book4.add("Preface");
        book4.add("Psalm III");
        book4.add("Introduction");
        book4.add("David blames God");
        book4.add("The reproach of other...");
        book4.add("God likes us to reproach...");
        book4.add("The occasion on which...");
        book4.add("In fact, not all...");
        book4.add("There is no help for...");
        book4.add("You, o Lord, art a...");
        book4.add("My Glory and the lifter...");
        book4.add("I laid down and slept...");
        book4.add("Three interpretations...");
        book4.add("The interpretation...");
        book4.add("Spiritual Contemplation...");
        book4.add("David the symbol of...");
        book4.add("I  will not be afraid...");
        book4.add("Arise, O Lord");
        book4.add("For Thou hast smitten all...");
        book4.add("You have broken the...");
        book4.add("Your blessing is upon...");
        book4.add("So are Davids Psalms");

        book5=new ArrayList<>();
        book5.add("In The Name Of The...");
        book5.add("Introduction");
        book5.add("Sin is the state of being...");
        book5.add("Sin is being cut off from...");
        book5.add("The serious...");
        book5.add("The return to God");
        book5.add("The story of mans...");
        book5.add("What does in mean to...");
        book5.add("God wants us to return");
        book5.add("Prayer is the means of...");
        book5.add("Adversity as a reason...");
        book5.add("Reconciliation with God");
        book5.add("Sin is contending against...");
        book5.add("Sin being unfaithful to God");
        book5.add("God is reconciled with us");
        book5.add("How reconciliation takes...");

        book6=new ArrayList<>();
        book6.add("The story of this book...");
        book6.add("The pinnacle of tears");
        book6.add("The beatification of tears");
        book6.add("Types of weeping");
        book6.add("Tears in the ministry");
        book6.add("Tears in the lives of the...");
        book6.add("The reasons for tears");
        book6.add("The obstacles of tears");
        book6.add("Back cover");

        book7=new ArrayList<>();
        book7.add("Introduction");
        book7.add("The concept of power");
        book7.add("The concept of freedom");
        book7.add("The concept of rest &...");
        book7.add("The concept of ambition");
        book7.add("The concept of sin");
        book7.add("The concept of love &...");
        book7.add("The concept of offense");
        book7.add("The concept of meekness");
        book7.add("The concept of truth &...");
        book7.add("The concept of knowledge");

        book8=new ArrayList<>();
        book8.add("The history of this book");
        book8.add("The release from the...");
        book8.add("The release unto the...");
        book8.add("The release of the spirit");
        book8.add("Enclosed within four walls");
        book8.add("Greater than heaven and...");
        book8.add("He was fast asleep");
        book8.add("Know yourself");
        book8.add("Your self and the praise...");
        book8.add("Your self and the offences");
        book8.add("Be released from your self");
        book8.add("Your self in the presence...");
        book8.add("Be released from your...");
        book8.add("Be released from the...");
        book8.add("I want nothing of the world");
        book8.add("Learning from God");
        book8.add("Be released from the...");
        book8.add("Be released of the feeling...");
        book8.add("Be released from the...");
        book8.add("The wretched");
        book8.add("It happened that nigh");
        book8.add("And will leave me alone");
        book8.add("Meditation on light and...");
        book8.add("When I sit alone with...");
        book8.add("Reveal to me Yourself");
        book8.add("Love of the way which...");
        book8.add("Leave me now");
        book8.add("Our Lord is present");

        listSubTitles.put(listBookTitles.get(0),book1);
        listSubTitles.put(listBookTitles.get(1),book2);
        listSubTitles.put(listBookTitles.get(2),book3);
        listSubTitles.put(listBookTitles.get(3),book4);
        listSubTitles.put(listBookTitles.get(4),book5);
        listSubTitles.put(listBookTitles.get(5),book6);
        listSubTitles.put(listBookTitles.get(6),book7);
        listSubTitles.put(listBookTitles.get(7),book8);

    }
}
