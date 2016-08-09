package a3danimation.android.com.a3danimation;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListActivity extends Activity {

    private SemiCircularListView mCircularListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_main);

        mCircularListView = (SemiCircularListView) findViewById(R.id.circularListView);

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        for (int i = 0; i < 25; i++) {
            listAdapter.add(""+i);
        }

        mCircularListView.setAdapter(listAdapter);

    }

}
