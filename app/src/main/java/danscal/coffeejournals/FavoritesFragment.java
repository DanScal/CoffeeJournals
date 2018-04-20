package danscal.coffeejournals;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Map;

public class FavoritesFragment extends Fragment{
    private static final String TAG = "FavoritesFragment";
    FirebaseListAdapter mAdapter;
    ListView mListView;
    FirebaseAuth mAuth;
    DatabaseReference mRef;

    TextView name;
    TextView vibeRating;
    TextView coffeeRating;
    //TextView location;
    Button websiteBTN;
    ImageView image;

    ArrayList<String> favoritesList;
    String favoritesString;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_coffeeshop_listview, container, false);

        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("users");
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        favoritesString = mRef.child(userID).child("favorites").toString();

        System.out.println("FAVORITES" + favoritesString);
        favoritesList = new ArrayList<>();



        Query query = FirebaseDatabase.getInstance().getReference().child("coffee shops");

        FirebaseListOptions<CoffeeShop> options = new FirebaseListOptions.Builder<CoffeeShop>()
                .setQuery(query, CoffeeShop.class)
                .setLayout(R.layout.list_item_coffeeshop)
                .setLifecycleOwner(this)
                .build();

        //Finally you pass them to the constructor here:
        mAdapter = new FirebaseListAdapter<CoffeeShop>(options) {
            @Override
            protected void populateView(View view, final CoffeeShop shop, int position) {
                //Set the value for the views
                if(favoritesString.equals(shop.getName())){
                    name = view.findViewById(R.id.coffeeshop_name);
                    vibeRating = view.findViewById(R.id.vibe_rating);
                    coffeeRating = view.findViewById(R.id.coffee_rating);
                    //location = view.findViewById(R.id.location);
                    image = view.findViewById(R.id.logo_image);
                    websiteBTN = view.findViewById(R.id.webiste_button);


                    name.setText(shop.getName());
                    vibeRating.setText("Vibe Rating: " + shop.getVibe() + "/5");
                    coffeeRating.setText("Coffee Rating: " + shop.getCoffee() + "/5");
                    //location.setText(shop.getLocation());

                    Glide.with(FavoritesFragment.this)
                            .load(shop.imageURL)
                            .into(image);

                    final String url = shop.getWebsite();

                }


            }
        };

        mListView = (ListView) view.findViewById(R.id.coffeeListView);
        mListView.setAdapter(mAdapter);
        return view;
    }

    }
