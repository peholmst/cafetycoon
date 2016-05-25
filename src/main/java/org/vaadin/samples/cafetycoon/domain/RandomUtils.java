package org.vaadin.samples.cafetycoon.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public final class RandomUtils {
	
	private static final Random RND = new Random();
	
	private RandomUtils() {		
	}
	
    public static <T> T pickRandom(Collection<T> items) {
        List<T> itemList;
        if (items instanceof List) {
            itemList = (List<T>) items;
        } else {
            itemList = new ArrayList<>(items);
        }
        return itemList.get(RND.nextInt(itemList.size()));
    }
}
