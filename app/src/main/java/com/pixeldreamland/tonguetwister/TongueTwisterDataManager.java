package com.pixeldreamland.tonguetwister;

import java.util.ArrayList;
import java.util.List;

public class TongueTwisterDataManager {


    private static TongueTwisterDataManager INSTANCE;
    private final List<TongueTwister> tongueTwisterList;
    private int currentIndex;
    private int numberOfTongueTwistersInDatabase;
    private final ArrayList<Integer> numberOfTongueTwistersInTheDatabase;
    private boolean getMoreTongueTwistersIsFinished;

    public TongueTwisterDataManager() {
        tongueTwisterList = new ArrayList<>();
        numberOfTongueTwistersInTheDatabase = new ArrayList<>();
        getMoreTongueTwistersIsFinished = false;


    }

    public static TongueTwisterDataManager getInstance() {
        if (INSTANCE == null) {


            INSTANCE = new TongueTwisterDataManager();

        }
        return INSTANCE;

    }

    public List<TongueTwister> getTongueTwisterList() {
        return tongueTwisterList;
    }

    public List<Integer> getNumberOfTongueTwistersInTheDatabase() {
        return numberOfTongueTwistersInTheDatabase;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void incrementCurrentIndex() {
        currentIndex++;
    }

    public void decrementCurrentIndex() {
        currentIndex--;
    }

    public int getNumberOfTongueTwistersInDatabase() {
        return numberOfTongueTwistersInDatabase;
    }

    public void setNumberOfTongueTwistersInDatabase(int numberOfTongueTwistersInDatabase) {
        this.numberOfTongueTwistersInDatabase = numberOfTongueTwistersInDatabase;
    }

    public boolean isGetMoreTongueTwistersIsFinished() {
        return getMoreTongueTwistersIsFinished;
    }

    public void setGetMoreTongueTwistersIsFinished(boolean getMoreTongueTwistersIsFinished) {
        this.getMoreTongueTwistersIsFinished = getMoreTongueTwistersIsFinished;
    }

    public void destroy() {
        INSTANCE = null;
    }
}


