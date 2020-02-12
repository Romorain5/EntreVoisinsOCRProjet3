package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.entrevoisins.model.Neighbour;

import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.openclassrooms.entrevoisins.ui.neighbour_list.NeighbourDetailsActivity.PREF_NEIGHBOURS;

public class FavManager {

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private Gson mGson = new Gson();

    public FavManager(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(PREF_NEIGHBOURS, MODE_PRIVATE);
    }

    public void saveNeighbour(Neighbour neighbour) {
        List<Neighbour> allFavoritesNeighbour = getFromFav();

        if (allFavoritesNeighbour.contains(neighbour)) {
            allFavoritesNeighbour.remove(neighbour);
        } else {
            allFavoritesNeighbour.add(neighbour);
        }

        mSharedPreferences.edit().putString(PREF_NEIGHBOURS, mGson.toJson(allFavoritesNeighbour)).apply();
    }

    public List<Neighbour> getFromFav() {
        Type type = new TypeToken<List<Neighbour>>(){}.getType(); // permet de pouvoir renseigner à JSON une Liste de Neighbour ( car pas possible de dire
        String neighboursSavedString = mSharedPreferences.getString(PREF_NEIGHBOURS, "");
        List<Neighbour> emptyArray = new ArrayList<>();

        List<Neighbour> jsonToList = mGson.fromJson(neighboursSavedString,type);
        if (jsonToList != null) {
            return jsonToList;
        } else {
            return emptyArray;
        }

    }

    public void removeFromFav(Neighbour neighbour) {
        List<Neighbour> allFavoritesNeighbour = getFromFav();

        if (allFavoritesNeighbour.contains(neighbour)) {
            allFavoritesNeighbour.remove(neighbour);
            mSharedPreferences.edit().putString(PREF_NEIGHBOURS, mGson.toJson(allFavoritesNeighbour)).apply();
        }
    }
}
