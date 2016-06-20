package de.haw_landshut.haw_dating.p2pdatingapp.P2p;

/**
 * Created during the students project "FH-Tinder" at HaW-Landshut, University of Applied Sciences.
 * Supervising professor: Prof. Andreas Siebert, Ph.D
 * <p/>
 * 6/7/16 by s-gheldd
 */
public interface FindYourLoveMessageListener {
    void onLoveMessageReceive(final String message);
    void onPeersDiscovered(final P2pInterface p2pInterface);
}
