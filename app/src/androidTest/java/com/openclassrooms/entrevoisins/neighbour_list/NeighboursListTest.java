
package com.openclassrooms.entrevoisins.neighbour_list;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.core.IsNull.notNullValue;



/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    // This is fixed
    private static int ITEMS_COUNT = 12;



    private ListNeighbourActivity mActivity;

    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }



    /**
     * We ensure that our recyclerview is displaying at least one item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(ViewMatchers.withId(R.id.list_neighbours))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onView(ViewMatchers.withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(ViewMatchers.withId(R.id.list_neighbours))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(ViewMatchers.withId(R.id.list_neighbours)).check(withItemCount(ITEMS_COUNT-1));
    }

    // TEST ADDED

    @Test
    public void onNeighboursList_click_GoToDetail() {
        onView(ViewMatchers.withId(R.id.list_neighbours)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(ViewMatchers.withId(R.id.activity_neighbour_detail)).check(matches(isDisplayed()));
    }

    @Test
    public void onDetailView_userNameIsNotEmpty() {
        onView(ViewMatchers.withId(R.id.list_neighbours)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(ViewMatchers.withId(R.id.neighbour_name)).check(matches(notNullValue()));
    }

    @Test
    public void favoriteViewOnlyDisplayFavNeighbours()  {
        RecyclerView list_neighbours_favorites = mActivityRule.getActivity().findViewById(R.id.list_neighbours_favorites);

        int favCountBeforeClick = 0;
        int favCountAfterClick = 0;
        int i = 0;
        // Clique sur un voisin, clique sur le bouton fav, la condition verifie si on viens de l'ajouter ou de l'enlever des favoris
        while (i != ITEMS_COUNT) {
            favCountBeforeClick = list_neighbours_favorites.getAdapter().getItemCount();
            onView(ViewMatchers.withId(R.id.list_neighbours)).perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));
            onView(ViewMatchers.withId(R.id.image_favorite)).perform(click());
            Espresso.pressBack();
            favCountAfterClick = list_neighbours_favorites.getAdapter().getItemCount();
            onView(ViewMatchers.withId(R.id.list_neighbours_favorites)).check(withItemCount(favCountAfterClick));

            if (favCountBeforeClick > favCountAfterClick) {
                // si on viens d'enlever un fav on doit avoir favCountBeforeClick -1 sinon l'inverse
                onView(ViewMatchers.withId(R.id.list_neighbours_favorites)).check(withItemCount(favCountBeforeClick - 1));
            } else {
                onView(ViewMatchers.withId(R.id.list_neighbours_favorites)).check(withItemCount(favCountBeforeClick + 1));
            }
            i++;
        }

    }
}


