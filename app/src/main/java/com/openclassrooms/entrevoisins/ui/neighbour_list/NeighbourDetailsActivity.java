package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import java.util.List;

public class NeighbourDetailsActivity extends AppCompatActivity {

    public static String NEIGHBOUR_ID = "arg_id_neighbour";
    public static String NEIGHBOUR_SAVED_TEXT = "L'utilisateur a été enregistré dans les favoris";
    public static String NEIGHBOUR_DELETED_TEXT = "L'utilisateur a été supprimé des favoris";


    private String facebookUrl = "www.facebook.fr/";
    private NeighbourApiService mApiservice;
    private Neighbour mNeighbour;
    public static String NEIGHBOUR_PREF = "neighbourSharedPreferences";
    FavoritesManager manager;

    List<Neighbour> mNeighboursSaved;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighbour_details);
        Intent intent = getIntent();
        //récupere l'id du voisin cliqué
        Integer neighbourId = intent.getIntExtra(NEIGHBOUR_ID, 0);
        mApiservice = DI.getNeighbourApiService();
        // récupere le voisin en renseignant l'id à l'API
        mNeighbour = mApiservice.getNeighbourByID(neighbourId);

        // if user click on favorite button, we already have all favorites users to compare
        manager = new FavoritesManager(this);
        mNeighboursSaved = manager.getFavoritesNeighbour();

        if (mNeighbour != null) {
            bindNeighbourDetail();
        } else {
            this.finish();
        }

        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void bindNeighbourDetail() {
        AppCompatImageView imageNeighbour = findViewById(R.id.item_image_neighbour);
        AppCompatTextView nameNeighbour = findViewById(R.id.item_name_neighbour);
        AppCompatTextView cardViewNameNeighbour = findViewById(R.id.cardview_name);
        AppCompatTextView facebookLinkNeighbour = findViewById(R.id.item_cardview_link_facebook);
        AppCompatImageView favoriteButton = findViewById(R.id.image_favorite);
        AppCompatImageView backButton = findViewById(R.id.item_backButton);

        String url = facebookUrl + mNeighbour.getName().toLowerCase();

        nameNeighbour.setText(mNeighbour.getName());
        cardViewNameNeighbour.setText(mNeighbour.getName());
        facebookLinkNeighbour.setText(url);
        Glide.with(imageNeighbour.getContext())
                .load(mNeighbour.getAvatarUrl())
                .apply(RequestOptions.centerCropTransform())
                .into(imageNeighbour);
        backButton.setOnClickListener(view -> {
            this.finish();
        });

        if (mNeighboursSaved.contains(mNeighbour)) {
            favoriteButton.setImageResource(R.drawable.ic_star_24dp);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_star_border_yellow_24dp);
        }


        favoriteButton.setOnClickListener(view -> {

            manager.saveNeighbour(mNeighbour);

            if (manager.getFavoritesNeighbour().contains(mNeighbour)) {
                makeToast(NEIGHBOUR_SAVED_TEXT);
                favoriteButton.setImageResource(R.drawable.ic_star_24dp);
            } else {
                makeToast(NEIGHBOUR_DELETED_TEXT);
                favoriteButton.setImageResource(R.drawable.ic_star_border_yellow_24dp);
            }
        });
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}