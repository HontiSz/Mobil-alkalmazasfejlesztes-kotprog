package hu.mobilalk.shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private FirebaseUser user;

    private RecyclerView recyclerView;
    private ArrayList<Items> items;
    private ItemAdapter itemAdapter;

    private int gridNumber = 1;
    private boolean viewRow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            Log.i(LOG_TAG, "Bejelentkezett felhasznaló!");
        }
        else {
            Log.i(LOG_TAG, "Nincs bejelentkezett felhasznaló!");
            finish();
        }

        recyclerView = findViewById(R.id.mainRecycleView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        items = new ArrayList<>();

        itemAdapter = new ItemAdapter(this, items);
        recyclerView.setAdapter(itemAdapter);

        initializeData();
    }

    private void initializeData() {
        String[] itemsList = getResources().getStringArray(R.array.itemTitles);
        String[] itemsInfo = getResources().getStringArray(R.array.itemDescriptions);
        String[] itemsPrice = getResources().getStringArray(R.array.itemPrices);
        TypedArray itemsImage = getResources().obtainTypedArray(R.array.itemImages);
        TypedArray itemsRate = getResources().obtainTypedArray(R.array.itemRates);

        items.clear();

        for(int i = 0; i < itemsList.length; i++) {
            items.add(new Items(itemsList[i], itemsPrice[i], itemsInfo[i], itemsRate.getFloat(i, 0), itemsImage.getResourceId(i, 0)));
        }

        itemsImage.recycle();

        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(LOG_TAG, s);
                itemAdapter.getFilter().filter(s);

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menuLogOut: {
                Log.d(LOG_TAG, "Kijelentkezett a felhasznalo!");
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            }
            case R.id.menuSettings: {
                Log.d(LOG_TAG, "menuSetting clicked");
                return true;
            }
            case R.id.menuCart: {
                Log.d(LOG_TAG, "menuCart clicked");
                return true;
            }
            case R.id.menuSelector: {
                Log.d(LOG_TAG, "menuSelector clicked");
                if(viewRow) {
                    changeSpanCount(item, R.drawable.ic_grid, 1);
                }
                else {
                    changeSpanCount(item, R.drawable.ic_row, 2);
                }
                return true;
            }
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void changeSpanCount(MenuItem item, int ic_grid, int i) {
        viewRow = !viewRow;
        item.setIcon(ic_grid);
        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        layoutManager.setSpanCount(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}