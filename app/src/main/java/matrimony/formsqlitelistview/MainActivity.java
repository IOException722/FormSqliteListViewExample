package matrimony.formsqlitelistview;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends ActionBarActivity {
   /* CheckBox mDebit, mCredit;*/

    Integer mValidBit;
    EditText name;
    EditText location;
    Spinner mSpinner;
    ArrayAdapter mSpinnerAdapter;
    ArrayList<String> mSpinnerList;
    EditText detail;
    EditText amt;
    Button save, show;
    boolean fDebit, fCredit;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /*mDebit = (CheckBox) findViewById(R.id.debit);
        mCredit = (CheckBox) findViewById(R.id.credit);*/
        name = (EditText) findViewById(R.id.name);
        location =(EditText) findViewById(R.id.location);
        detail = (EditText) findViewById(R.id.detail);
        amt =  (EditText) findViewById(R.id.amount);
        save = (Button) findViewById(R.id.save);
        show = (Button) findViewById(R.id.show);
        mSpinner = (Spinner) findViewById(R.id.debitcredit);
        mSpinnerList = new ArrayList<String>();
        mSpinnerList.add("Credit");
        mSpinnerList.add("Debit");

        mSpinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1,mSpinnerList);
        mSpinner.setAdapter(mSpinnerAdapter);
        mValidBit = 0;
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mValidBit = position;
                mSpinnerAdapter.notifyDataSetChanged();
                Log.v("mValidBitin", mValidBit.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Log.v("nothing", "nothing");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean fname = true;
                boolean flocation = true;
                boolean fdetail = true;
                boolean famt = true;
                Log.v("insavevalidbit", mValidBit.toString());
                if(name.getText().toString().length()<1){
                    fname = false;
                    Toast.makeText(getApplicationContext(), "Please enter name !",Toast.LENGTH_LONG).show();
                }
                if(location.getText().toString().length()<1){
                    flocation = false;
                    Toast.makeText(getApplicationContext(), "Please enter location!",Toast.LENGTH_LONG).show();
                }

                if(detail.getText().toString().length()<1){
                    fdetail = false;
                    Toast.makeText(getApplicationContext(), "Please enter detail !",Toast.LENGTH_LONG).show();
                }

                if(amt.getText().toString().length()<1){
                    famt = false;
                    Toast.makeText(getApplicationContext(), "Please enter amount !",Toast.LENGTH_LONG).show();
                }
                if(famt && fdetail && flocation && fname)
                    mDataBase();
            }
        }) ;

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowList.class);
                startActivity(intent);
            }
        });
    }

    public void mDataBase()
    {
        SQLiteDatabase mDb = null;
        String TABLE_NAME = "Money";
        String COL_NAME1 = "ToP";
        String COL_NAME2 = "Locn";
        String COL_NAME3 = "Det";
        String COL_NAME4 = "Amnt";
        String COL_DATE = "pDate";
        String COL_BIT = "validBit";
      /*  mValidBit = mValidBit==0 ? 1 : 0;*/

        Log.v("validvittoggle", mValidBit.toString());
      //  String DROP_TABLE = "DROP TABLE Money";
        String STRING_CREATE = " CREATE TABLE IF NOT EXISTS " +TABLE_NAME+ " (" +COL_NAME1+ " TEXT NOT NULL, " +COL_NAME2+ " TEXT NOT NULL, " +COL_NAME3+ " TEXT NOT NULL," +COL_NAME4+ " INT NOT NULL," + COL_DATE+ " DATE , "+ COL_BIT + " INT NOT NULL );";

        String Data="";
        try{
            mDb = this.openOrCreateDatabase("mDataBaseName", MODE_PRIVATE, null);
           // mDb.execSQL(DROP_TABLE);
            mDb.execSQL(STRING_CREATE);
            ContentValues cv = new ContentValues(6);
            cv.put(COL_NAME1, name.getText().toString());
            cv.put(COL_NAME2, location.getText().toString());
            cv.put(COL_NAME3, detail.getText().toString());
            Integer amnt = Integer.parseInt(amt.getText().toString());
            if(mValidBit==0)
                amnt = -1*amnt;
            cv.put(COL_NAME4, amnt);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM HH:ss:mm");
            cv.put(COL_DATE, dateFormat.format(new Date()));
            cv.put(COL_BIT, mValidBit);
            mDb.insert(TABLE_NAME, null, cv);
        }

        catch(Exception e) {
            Log.e("Error", "Error", e);
        }
        finally {
            if (mDb != null)
                mDb.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

