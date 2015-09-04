package matrimony.formsqlitelistview;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;

public class ShowList extends AppCompatActivity {
    ListView mListView;
    ArrayList<String> mName;
    ArrayList<String> mLocation;
    ArrayList<String> mDetail;
    ArrayList<String > mFragmentTagList;
    ArrayList<Integer> mAmount;
    ArrayList<String > mdateTime;
    LayoutInflater mInflater;
    Spinner mNameSpinner,mCreditOrDebitSpinner;
    ArrayAdapter mNameAdapter,mCreditDebitAdapter;
    ArrayList<String> mNameList, mCreditOrDebitList;
    boolean isLongClicked;
    public  static boolean flag;
    MyAdapter myAdapter;
    String TABLE_NAME = "Money";
    public static  int length;
    SQLiteDatabase mDb;
    public int totalAmount;
    String selectedString, selectedString1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_list);
        mAmount = new ArrayList<Integer>();
        mDetail = new ArrayList<String>();
        mLocation = new ArrayList<String>();
        mName  = new ArrayList<String>();
        mdateTime = new ArrayList<String>();

        mFragmentTagList = new ArrayList<String>();
        mNameList = new ArrayList<String>();
        mCreditOrDebitList = new ArrayList<String>();
        totalAmount = 1000000000;
        flag = false;


/*****Action bar back button.....*///
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mListView = (ListView) findViewById(R.id.list_view);
        mNameSpinner = (Spinner) findViewById(R.id.spinner1);
        mCreditOrDebitSpinner = (Spinner) findViewById(R.id.spinner2);

        mInflater = getLayoutInflater();
        isLongClicked = false;
        mDb = null;


        mNameList.add("overall");
        mCreditOrDebitList.add("Credit+Debit");
        mCreditOrDebitList.add("Credit");
        mCreditOrDebitList.add("Debit");
        mNameAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1,mNameList);
        mNameSpinner.setAdapter(mNameAdapter);
        selectedString =  "overall";
        selectedString1 ="Credit+Debit";
        showAllTransations("overall", "Credit+Debit");
        mNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedString = mNameList.get(position);
                Log.v("selected is in spinner", selectedString);
                mName.clear();
                mLocation.clear();
                mAmount.clear();
                mDetail.clear();
                mdateTime.clear();
                totalAmount = getSum(selectedString,"Credit+Debit");
                mNameAdapter.notifyDataSetChanged();
                myAdapter.notifyDataSetChanged();
                showAllTransations(selectedString,selectedString1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Log.v("nothing", "nothing");
            }
        });

        mCreditDebitAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1,mCreditOrDebitList);
        mCreditOrDebitSpinner.setAdapter(mCreditDebitAdapter);

        mCreditOrDebitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedString1 = mCreditOrDebitList.get(position);

                mName.clear();
                mLocation.clear();
                mAmount.clear();
                mDetail.clear();
                mdateTime.clear();
                totalAmount = getSum(selectedString, selectedString1);
                showAllTransations(selectedString, selectedString1);
                mCreditDebitAdapter.notifyDataSetChanged();
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.v("nothing", "nothing");
            }
        });

        myAdapter = new MyAdapter();
        callAdapter();
    }

    public void showAllTransations(String str, String str1) {
        Log.v("selected is in show", str);
        Log.v("selected in show", str1);
        String COL_NAME1 = "ToP";
        String COL_NAME2 = "Locn";
        String COL_NAME3 = "Det";
        String COL_NAME4 = "Amnt";
        String COL_DATE = "pDate";
        String COL_BIT = "validBit";
        String query;

        if (str.equals("overall") && str1.equals("Credit+Debit"))
            query = "SELECT * FROM " + TABLE_NAME;
        else if(str.equals("overall") && str1.equals("Credit"))
            query = "SELECT * FROM " + TABLE_NAME + " WHERE validBit "+ " = '"+0+"'";
        else if(str.equals("overall") && str1.equals("Debit"))
            query = "SELECT * FROM " + TABLE_NAME + " WHERE validBit "+ " = '"+1+"'";
        else if(!str.equals("overall") && str1.equals("Credit+Debit"))
            query = "SELECT * FROM " + TABLE_NAME + " WHERE ToP "+ " = '"+str+"'";
        else if(!str.equals("overall") && str1.equals("Credit"))
            query = "SELECT * FROM " + TABLE_NAME + " WHERE ToP "+ " = '"+str+"'" + " AND validBit "+ " = '"+0+"'";
        else
            query = "SELECT * FROM " + TABLE_NAME + " WHERE ToP "+ " = '"+str+"'" + " AND validBit "+ " = '"+1+"'";
        try {
            mDb = this.openOrCreateDatabase("mDataBaseName", MODE_PRIVATE, null);
            Cursor c = mDb.rawQuery(query , null);
            int Column1 = c.getColumnIndex(COL_NAME1);
            int Column2 = c.getColumnIndex(COL_NAME2);
            int Column3 =c.getColumnIndex(COL_NAME3);
            int Column4 = c.getColumnIndex(COL_NAME4);
            int Column5 = c.getColumnIndex(COL_DATE);
            int Column6 = c.getColumnIndex(COL_BIT);
            c.moveToFirst();
            if (c != null) {
                do {
                    String name = c.getString(Column1);
                    String location = c.getString(Column2);
                    String detail = c.getString(Column3);
                    int amt = c.getInt(Column4);
                    int validBit = c.getInt(Column6);
                    boolean flag = false;
                    for (int i = 0; i < mNameList.size(); i++)
                    {
                        if(name.equals(mNameList.get(i)))
                            flag = true;
                    }
                    if(!flag)
                        mNameList.add(name);

                    String date = c.getString(Column5);
                    mName.add(name);
                    mLocation.add(location);
                    mDetail.add(detail);
                    mAmount.add(amt);
                    mdateTime.add(date);

                }while(c.moveToNext());
            }
        }
        catch(Exception e) {
            Log.e("Error", "Error", e);
        }
        finally {
            if (mDb != null)
                mDb.close();
        }
        length = mAmount.size();
    }

    public void callAdapter()
    {
        mListView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }

    public int getSum(String str, String str1)
    {
        mDb = this.openOrCreateDatabase("mDataBaseName", MODE_PRIVATE, null);
        String qry ;
        if(str.equals("overall") && str1.equals("Credit+Debit"))
            qry =  "SELECT SUM(Amnt) FROM Money";
        else if(str.equals("overall") && str1.equals("Credit"))
            qry = "SELECT SUM(Amnt) FROM Money WHERE validBit "+ " = '"+0+"'";
        else if(str.equals("overall") && str1.equals("Debit"))
            qry = "SELECT SUM(Amnt) FROM Money WHERE validBit "+ " = '"+1+"'";
        else if(!str.equals("overall") && str1.equals("Credit+Debit"))
            qry = "SELECT SUM(Amnt) FROM Money WHERE ToP "+ " = '"+str+"'";
        else if(!str.equals("overall") && str1.equals("Credit"))
            qry = "SELECT SUM(Amnt) FROM Money WHERE ToP "+ " = '"+str+"'" + " AND validBit "+ " = '"+0+"'" ;
        else
            qry = "SELECT SUM(Amnt) FROM Money WHERE ToP "+ " = '"+str+"'" + " AND validBit "+ " = '"+1+"'";

        Cursor cursor = mDb.rawQuery(qry, null);
        if(cursor.moveToFirst()) {
            mDb.close();
            return cursor.getInt(0);
        }
        else {
            mDb.close();
            return 0;
        }

    }
    public void delete(String date) {

        mDb = this.openOrCreateDatabase("mDataBaseName", MODE_PRIVATE, null);
        mDb.execSQL("delete from " + TABLE_NAME + " where pDate='" + date + "'");
        Log.v("deleting", date);
        mDb.close();
    }

    public class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(convertView == null){

                convertView = mInflater.inflate(R.layout.listall,parent,false);
            }

            TextView tName = (TextView) convertView.findViewById(R.id.name);
            tName.setText(mName.get(position));

            TextView tLocation = (TextView) convertView.findViewById(R.id.location);
            tLocation.setText(mLocation.get(position));

            TextView tDetail = (TextView) convertView.findViewById(R.id.detail);
            tDetail.setText(mDetail.get(position));

            TextView tAmount = (TextView) convertView.findViewById(R.id.amountt);
            tAmount.setText(mAmount.get(position).toString());

            TextView tDate = (TextView) convertView.findViewById(R.id.datetime);
            tDate.setText(mdateTime.get(position));

            if(mAmount.get(position)<0)
                convertView.setBackgroundColor(Color.RED);
            else
                convertView.setBackgroundColor(Color.BLUE);

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    FireMissilesDialogFragment fr = new FireMissilesDialogFragment(position);
                    android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    fr.show(transaction, "Dialog1");
                    return true;
                }
            });

            TextView tTotal = (TextView) convertView.findViewById(R.id.totala);
            tTotal.setText(Integer.toString(totalAmount));
            return convertView;
        }

    }

    public void deleteRowDialog(int position)
    {
        delete(mdateTime.get(position));
        mName.remove(position);
        mAmount.remove(position);
        mLocation.remove(position);
        mDetail.remove(position);
        mdateTime.remove(position);
        length--;
        isLongClicked = true;
        myAdapter.notifyDataSetChanged();
    }

    public  class FireMissilesDialogFragment extends DialogFragment {
        int position;
        public  FireMissilesDialogFragment()
        {

        }
        @SuppressLint("ValidFragment")
        public FireMissilesDialogFragment(int position)
        {
            this.position = position;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Realy want to delete entry : "+ mName.get(position)+ "  "+mLocation.get(position)+ "  "+mDetail.get(position)+"  "+mAmount.get(position)+"  "+mdateTime.get(position)+"  "+totalAmount + " ? ")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                            flag = true;
                            Log.v("intrue", Boolean.toString(flag));
                            deleteRowDialog(position);

                        }
                    })

                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            flag = false;
                            Log.v("infalse",Boolean.toString(flag));
                        }
                    });

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_lisy, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    /*    switch (item.getItemId()) {
            case R.drawable.back:
                // app icon in action bar clicked; go home
                *//*Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*//*
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/

    }
}
