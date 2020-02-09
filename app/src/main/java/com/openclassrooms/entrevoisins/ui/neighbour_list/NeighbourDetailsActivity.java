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

import butterknife.BindView;
import butterknife.ButterKnife;

public class NeighbourDetailsActivity extends AppCompatActivity {

    public static String NEIGHBOUR_ID = "arg_id_neighbour";
    public static String NEIGHBOUR_SAVED_TEXT = "L'utilisateur a été enregistré dans les favoris";
    public static String NEIGHBOUR_DELETED_TEXT = "L'utilisateur a été supprimé des favoris";

    @BindView(R.id.neighbour_image)
    AppCompatImageView neighbourImage;
    @BindView(R.id.neighbour_name)
    AppCompatTextView neighbourName;
    @BindView(R.id.cardview_name)
    AppCompatTextView cardViewName;
    @BindView(R.id.item_cardview_link_facebook)
    AppCompatTextView facebookLink;
    @BindView(R.id.image_favorite)
    AppCompatImageView favoriteButton;
    @BindView(R.id.backButton)
    AppCompatImageView backButton;


    private Neighbour mNeighbour;
    public static String PREF_NEIGHBOURS = "neighbourSharedPreferences";
    FavManager manager;
    List<Neighbour> mNeighboursSaved;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neighbour_details);
        Intent intent = getIntent();
        ButterKnife.bind(this);
        //récupere l'id du voisin cliqué
        Integer neighbourId = intent.getIntExtra(NEIGHBOUR_ID, 0);
        NeighbourApiService mApiservice = DI.getNeighbourApiService();
        // récupere le voisin en renseignant l'id à l'API
        mNeighbour = mApiservice.getNeighbourByID(neighbourId);

        manager = new FavManager(this);
        mNeighboursSaved = manager.getFromFav();

        if (mNeighbour != null) {
            applyDetails();
        } else {
            this.finish();
        }

        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void applyDetails() {


        String facebookUrl = "www.facebook.fr/";
        String url = facebookUrl + mNeighbour.getName().toLowerCase();

        neighbourName.setText(mNeighbour.getName());
        cardViewName.setText(mNeighbour.getName());
        facebookLink.setText(url);
        Glide.with(neighbourImage.getContext())
                .load(mNeighbour.getAvatarUrl())
                .apply(RequestOptions.centerCropTransform())
                .into(neighbourImage);
        backButton.setOnClickListener(view -> this.finish());

        if (mNeighboursSaved.contains(mNeighbour)) {
            favoriteButton.setImageResource(R.drawable.ic_star_24dp);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_star_border_yellow_24dp);
        }


        favoriteButton.setOnClickListener(view -> {

            manager.saveNeighbour(mNeighbour);

            if (manager.getFromFav().contains(mNeighbour)) {
                makeToast(NEIGHBOUR_SAVED_TEXT);
                favoriteButton.setImageResource(R.drawable.ic_star_24dp);
            } else {
                favoriteButton.setImageResource(R.drawable.ic_star_border_yellow_24dp);
                makeToast(NEIGHBOUR_DELETED_TEXT);
            }
        });
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}