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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private FirebaseUser user;

    private RecyclerView recyclerView;
    private ArrayList<Items> items;
    private ItemAdapter itemAdapter;

    private FirebaseFirestore firestore;
    private CollectionReference itemCollection;

    private Query.Direction sort;
    private boolean sortB;

    private int gridNumber = 1;
    private boolean viewRow = true;

    private NotificationHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sort = Query.Direction.ASCENDING;
        sortB = true;

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

        firestore = FirebaseFirestore.getInstance();
        itemCollection = firestore.collection("Items");

        queryData();

        handler = new NotificationHandler(this);

        handler.cancelNotification();
    }

    private void queryData() {
        items.clear();

        itemCollection.orderBy("name", sort).limit(5).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                Items item = snapshot.toObject(Items.class);
                item.setId(snapshot.getId());
                items.add(item);
                Log.i(LOG_TAG, "Betöltött item: " + item.getName() + "\nid: " + item._getId());
            }

            if(items.size() == 0) {
                initializeData();
                queryData();
            }

            itemAdapter.notifyDataSetChanged();
        });
    }

    private void initializeData() {
        String[] itemsList = getResources().getStringArray(R.array.itemTitles);
        String[] itemsInfo = getResources().getStringArray(R.array.itemDescriptions);
        String[] itemsPrice = getResources().getStringArray(R.array.itemPrices);
        TypedArray itemsImage = getResources().obtainTypedArray(R.array.itemImages);
        TypedArray itemsRate = getResources().obtainTypedArray(R.array.itemRates);

        for(int i = 0; i < itemsList.length; i++) {
            itemCollection.add(new Items(
                    itemsList[i],
                    itemsPrice[i],
                    itemsInfo[i],
                    itemsRate.getFloat(i, 0),
                    itemsImage.getResourceId(i, 0),
                    0));
        }

        itemsImage.recycle();
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
            case R.id.menuDeleteUser: {
                Log.d(LOG_TAG, "menuDeleteUser clicked");
                user.delete();
                finish();
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
            case R.id.menuSort: {
                Log.d(LOG_TAG, "menuSort clicked");
                if(sortB) {
                    sort = Query.Direction.DESCENDING;
                    sortB = false;
                    queryData();
                    item.setTitle(R.string.a_z);
                }
                else {
                    sort = Query.Direction.ASCENDING;
                    sortB = true;
                    queryData();
                    item.setTitle(R.string.z_a);
                }
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

    public void updateCount(Items currentItem) {
        itemCollection.document(currentItem._getId()).update("count", currentItem.getCount() + 1).addOnSuccessListener(unused -> Log.d(LOG_TAG, "Sikeres count növelés!"));

        queryData();

        handler.sendNotification("Növelt count: " + currentItem.getName() + ", jelenleg: " + currentItem.getCount());
    }

    public void deleteItem(Items currentItem) {
        String name = currentItem.getName();
        itemCollection.document(currentItem._getId()).delete().addOnSuccessListener(unused -> {
            Log.d(LOG_TAG, "Törölt Item: " + name);

            handler.sendNotification("Törölt Item: " + name);
            queryData();
        }).addOnFailureListener(e -> {
            Log.d(LOG_TAG, "Sikertelen Item törlés; neve: " + name + " hiba: " + e.getMessage());

            handler.sendNotification("Nem sikerült törölni az Item - t: " + name);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.cancelNotification();
    }
}