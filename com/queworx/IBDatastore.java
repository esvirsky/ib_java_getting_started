package com.queworx;

import java.util.HashMap;

public class IBDatastore {

    public Integer nextValidId = null;

    private HashMap<Integer, Tick> __map = new HashMap<Integer, Tick>();

    public Tick getLatestTick(int symbolId) {
        return __map.get(symbolId);
    }
}
