package de.haw_landshut.haw_dating.p2pdatingapp.P2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * Created by Tobias on 06.06.2016.
 */
public class P2pBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "P2pBroadcastReceiver";
    private P2pInterface p2pInterface;
    private WifiP2pManager.Channel mChannel;
    private WifiP2pManager mManager;



    public P2pBroadcastReceiver(WifiP2pManager mManager, WifiP2pManager.Channel mChannel, P2pInterface p2pInterface){
        this.p2pInterface = p2pInterface;
        this.mManager = mManager;
        this.mChannel = mChannel;
    }



    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Determine if Wifi Direct mode is enabled or not, alert
            // the Activity.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);


            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                //activity.setIsWifiP2pEnabled(true);
                Log.d(TAG, "WIFI_P2P_STATE_CHANGED_ACTION: true");
            } else {
                //activity.setIsWifiP2pEnabled(false);
                Log.d(TAG, "WIFI_P2P_STATE_CHANGED_ACTION: false");
            }
        }

        else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // The peer list has changed!  We should probably do something about
            // that.
            // TODO: 06.06.2016 What to change about that?
            Log.d(TAG, "WIFI_P2P_PEERS_CHANGED_ACTION: changed");
            p2pInterface.fetch();
        }

        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            // Connection state changed!  We should probably do something about
            // that.
            Log.d(TAG, "WIFI_P2P_CONNECTION_CHANGED_ACTION: changed");


            //Now some awesome Stuff to insanly increase the power of this listener to over ninethousand
            if(mManager == null){
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {
                p2pInterface.setIsConnected(true);


                // We are connected with the other device, request connection
                // info to find group owner IP
                Log.d(TAG, "WIFI_P2P_CONNECTION_CHANGED_ACTION: isConnected");
                mManager.requestConnectionInfo(mChannel, p2pInterface.getConnectionInfoListener());
            }
            else if (networkInfo.isConnected() == false) {
                p2pInterface.setIsConnected(false);
                p2pInterface.setShouldDisconnect(false);
                Log.d(TAG, "WIFI_P2P_CONNECTION_CHANGED_ACTION: is not connected");
            }
        }

        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            //DeviceListFragment fragment = (DeviceListFragment) activity.getFragmentManager()
            //        .findFragmentById(R.id.frag_list);
            //fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
            //        WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
            Log.d(TAG, "WIFI_P2P_THIS_DEVICE_CHANGED_ACTION: changed");

            // Request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            if (mManager != null) {
                mManager.requestPeers(mChannel, p2pInterface.getPeerListListener());
            }
        }
    }
}