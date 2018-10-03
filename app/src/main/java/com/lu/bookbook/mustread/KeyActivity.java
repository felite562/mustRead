package com.lu.bookbook.mustread;


        import android.content.Intent;
        import android.support.v4.view.MenuItemCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.SearchView;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ListView;
        import android.widget.ProgressBar;
        import android.widget.Toast;

        import com.loopj.android.http.JsonHttpResponseHandler;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import cz.msebera.android.httpclient.Header;
        import java.util.ArrayList;



public class KeyActivity extends AppCompatActivity {
    private ListView lvBooks;
    private BookAdapter bookAdapter;
    private OpenLibrary client;
    private ProgressBar progress;
    public static SQLiteHelper mSQLiteHelper;
    public static final String BOOK_DETAIL_KEY = "book";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key);
        lvBooks = (ListView) findViewById(R.id.listBooks);
        ArrayList<Book> aBooks = new ArrayList<Book>();
        bookAdapter = new BookAdapter(this, aBooks);
        lvBooks.setAdapter(bookAdapter);
        //fetchBooks(String query));
        progress = (ProgressBar) findViewById(R.id.progress);
        //creating database
        mSQLiteHelper = new SQLiteHelper(this, "RECORDDB.sqlite", null, 1);

        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(KeyActivity.this, BookDetailActivity.class);
                intent.putExtra(BOOK_DETAIL_KEY, bookAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }
    private void fetchBooks(String query) {
        progress.setVisibility(ProgressBar.VISIBLE);
        client = new OpenLibrary();
        client.getBooks(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try { progress.setVisibility(ProgressBar.GONE);
                    JSONArray docs = null;
                    if(response != null) {
                        // Get the docs json array
                        docs = response.getJSONArray("docs");
                        // Parse json array into array of model objects
                        final ArrayList<Book> books = Book.fromJson(docs);
                        // Remove all books from the adapter
                        bookAdapter.clear();
                        // Load model objects into the adapter
                        for (Book book : books) {
                            bookAdapter.add(book); // add book through the adapter
                        }
                        bookAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    // Invalid JSON format, show appropriate error.
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progress.setVisibility(ProgressBar.GONE);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menubook, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Fetch the data remotely
                fetchBooks(query);
                // Reset SearchView
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                // Set activity title to search query
                KeyActivity.this.setTitle(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }
}
