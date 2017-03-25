package com.mobileappclass.travel.Modules;
import java.util.List;

/**
 * Created by Vishalsingh on 11/23/2016.
 */

public interface PlaceFinderListener {
    void PlaceFinderStart(String type);
    void PlaceFinderSuccess(List<Place> place,String type);
}
