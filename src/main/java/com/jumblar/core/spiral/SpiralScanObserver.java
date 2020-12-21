package com.jumblar.core.spiral;

public class SpiralScanObserver {

    public SpiralScanListener listener;
    private int actualRounds;

    public void increment(){
        actualRounds++;
        if (listener != null){
            listener.call();
        }
    }

    public int getActualRounds() {
        return actualRounds;
    }
}
