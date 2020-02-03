package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.entrevoisins.model.Neighbour;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FavoritesManager {

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private Gson mGson = new Gson();

    public FavoritesManager(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(NeighbourDetailsActivity.NEIGHBOUR_PREF, MODE_PRIVATE);
    }

    public void saveNeighbour(Neighbour neighbour) {
        List<Neighbour> allFavoritesNeighbour = getFavoritesNeighbour();

        if (allFavoritesNeighbour.contains(neighbour)) {
            allFavoritesNeighbour.remove(neighbour);
        } else {
            allFavoritesNeighbour.add(neighbour);
        }

        mSharedPreferences.edit().putString(NeighbourDetailsActivity.NEIGHBOUR_PREF, mGson.toJson(allFavoritesNeighbour)).apply();
    }

    public List<Neighbour> getFavoritesNeighbour() {
        Type type = new TypeToken<List<Neighbour>>(){}.getType(); // permet de pouvoir renseigner à JSON une Liste de Neighbour
        String neighboursSavedString = mSharedPreferences.getString(NeighbourDetailsActivity.NEIGHBOUR_PREF, "Not working");
        //List<Neighbour> allFavNeighbour = new ArrayList<>();

        List<Neighbour> allFavNeighbour = mGson.fromJson(neighboursSavedString, type);

      //  if (jsonToList != null) {
      //      allFavNeighbour.addAll(jsonToList);
      //  }

        return allFavNeighbour;
    }

    public void deleteFavoriteNeighbour(Neighbour neighbour) {
        List<Neighbour> allFavoritesNeighbour = getFavoritesNeighbour();

        if (allFavoritesNeighbour.contains(neighbour)) {
            allFavoritesNeighbour.remove(neighbour);
            mSharedPreferences.edit().putString(NeighbourDetailsActivity.NEIGHBOUR_PREF, mGson.toJson(allFavoritesNeighbour)).apply();
        }
    }
}
