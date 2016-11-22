package edu.casetools.dcase.extensions.io;

import java.util.HashMap;
import java.util.Map.Entry;

public class LocationManager {

    private static LocationManager instance = null;
    private HashMap<Integer, String> map;
    private int id;

    public LocationManager() {
	map = new HashMap<>();
	id = 0;
    }

    public static LocationManager getInstance() {
	if (instance == null) {
	    instance = new LocationManager();
	}
	return instance;
    }

    public void registerLocation(String name) {
	map.put(id, name);
	id++;
    }

    public String getLocationId(Object value) {
	for (Entry<Integer, String> o : map.entrySet()) {
	    if (map.get(o.getKey()).equals(value)) {
		return Integer.toString(o.getKey());
	    }
	}
	return null;
    }

}
