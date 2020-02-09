package com.openclassrooms.entrevoisins.service;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.ui.neighbour_list.FavManager;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * Unit test on Neighbour service
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = "src/main/AndroidManifest.xml")
public class NeighbourServiceTest {

    private NeighbourApiService service;
    private Context mContext;
    private FavManager mManager;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mManager = new FavManager(mContext);
    }

    @Test
    public void getNeighboursWithSuccess() {
        List<Neighbour> neighbours = service.getNeighbours();
        List<Neighbour> expectedNeighbours = DummyNeighbourGenerator.DUMMY_NEIGHBOURS;
        assertThat(neighbours, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedNeighbours.toArray()));
    }

    @Test
    public void deleteNeighbourWithSuccess() {
        Neighbour neighbourToDelete = service.getNeighbours().get(0);
        service.deleteNeighbour(neighbourToDelete);
        assertFalse(service.getNeighbours().contains(neighbourToDelete));
    }


    // TESTS ADDED


    @Test
    public void saveNeighbourAsFavoriteWithSuccess() {
        Neighbour expectedToBeSavedToFav = DummyNeighbourGenerator.DUMMY_NEIGHBOURS.get(0);
        mManager.saveNeighbour(expectedToBeSavedToFav);
        List<Neighbour> allFavs = mManager.getFromFav();
        assertThat(allFavs, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedToBeSavedToFav));
    }

     @Test
     public void getFavNeighboursWithSuccess() {
         Neighbour expectedToBeFav = DummyNeighbourGenerator.DUMMY_NEIGHBOURS.get(0);
         mManager.saveNeighbour(expectedToBeFav);
         List<Neighbour> allFavs = mManager.getFromFav();
         assertThat(allFavs, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedToBeFav));
     }

     @Test
    public void removeFavNeighbourWithSuccess() {
         Neighbour expectedToBeRemovedFromFav = DummyNeighbourGenerator.DUMMY_NEIGHBOURS.get(0);
         mManager.saveNeighbour(expectedToBeRemovedFromFav);
         mManager.saveNeighbour(expectedToBeRemovedFromFav);
         assertFalse(mManager.getFromFav().contains(expectedToBeRemovedFromFav));
     }
}
