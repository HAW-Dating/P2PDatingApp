package de.haw_landshut.haw_dating.p2pdatingapp;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Tobias on 06.06.2016.
 */

/**
 * Create an P2pInterface object with your mainactivity.
 * Call p2pInterface.initiate() in onCreate().
 * Call p2pInterface.onResume() in onResume().
 * Call p2pInterface.onPause() in onPause().
 * To Send Data call p2pInterface.sendProfile(String data).
 * To get incoming data, talk to the developer...
 */
// TODO: 07.06.2016 Better Exceptionhandling and User-information
public class P2pInterface {

    private static final String TAG = "P2pInterface";
    private static String profile = "Not valid yet";
    private static boolean shouldDisconnect;
    private final FindYourLoveMessageListener loveMessagelistener;
    private final IntentFilter intentFilter = new IntentFilter();
    String groupOwnerAdress;
    private Activity activity;
    private WifiP2pManager.Channel mChannel;
    private WifiP2pManager mManager;
    private P2pBroadcastReceiver receiver;
    private List peers = new ArrayList();
    private boolean isGroupOwner = false;
    private boolean isConnected = false;
    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager
            .PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            //Out with you, ya old little stinky peers...
            peers.clear();
            //In with the new yummy peers
            peers.addAll(peerList.getDeviceList());

            // If an AdapterView is backed by this data, notify it
            // of the change.  For instance, if you have a ListView of available
            // peers, trigger an update.
            //((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
            if (peers.size() == 0) {
                Log.d(TAG, "PeerListListener: no peers found");
                //Log.d(WiFiDirectActivity.TAG, "No devices found");
                return;
            } else {

                WifiP2pDevice p2pDevice = (WifiP2pDevice) peers.get(0);
                Log.d(TAG, "PeerListListener: found " + peers.size() + " peers, top peer: " +
                        p2pDevice.deviceName);
            }

        }
    };

    //Notifies me, when the Connection Info changes
    private WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager
            .ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            // TODO: 07.06.2016 Fails after disconnect: NullPointerException. I should probably
            // do something about it...

            // InetAddress from WifiP2pInfo struct.
            groupOwnerAdress = info.groupOwnerAddress.getHostAddress();

            // After the group negotiation, we can determine the group owner.
            if (info.groupFormed && info.isGroupOwner) {
                // Do whatever tasks are specific to the group owner.
                // One common case is creating a server thread and accepting
                // incoming connections.
                isGroupOwner = true;
                Log.d(TAG, "ConnectionInfoListener: You are the GroupOwner");
                writeMessage(profile);
            } else if (info.groupFormed) {
                // The other device acts as the client. In this case,
                // you'll want to create a client thread that connects to the group
                // owner.
                isGroupOwner = false;
                Log.d(TAG, "ConnectionInfoListener: You are not the GroupOwner");
                if (!isShouldDisconnect()) {
                    writeMessage(profile);
                } else {
                    Log.d(TAG, "ConnectionInfoListener: should disconnect");
                    disconnect();
                }
            }

        }
    };

    public P2pInterface(Activity activity, FindYourLoveMessageListener listener) {
        this.activity = activity;
        this.loveMessagelistener = listener;
        Log.d(TAG, "created P2pInterface");
    }

    protected String getProfile() {
        return profile;
    }


    public void sendProfile(String profile) {
        Log.d(TAG, "sendProfile()");
        P2pInterface.profile = profile;
        connect();

        // TODO: 06.06.2016 full fill
    }

    public void initiate() {
        Log.d(TAG, "initiate()");
        //Lets add intentions to the intentionFilter...
        //  Indicates a change in the Wi-Fi Peer-to-Peer status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


        //Let's get a intance of the WiFiP2PManager and a channel....
        mManager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(activity, activity.getMainLooper(), null);


        //My try to set up the BroadcastReceiver...
        receiver = new P2pBroadcastReceiver(mManager, mChannel, this);
        activity.registerReceiver(receiver, intentFilter);

        discover();
        fetch();
    }

    private void discover() {
        Log.d(TAG, "discover()");
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "discover(): Success");
            }

            @Override
            public void onFailure(int reasonCode) {
                Log.d(TAG, "discover(): Failure: " + reasonCode);
            }
        });
    }

    protected void fetch() {
        Log.d(TAG, "fetch()");
        mManager.requestPeers(mChannel, peerListListener);
    }

    private void connect() {
        Log.d(TAG, "connect()");
        // TODO: 10.06.2016 run through all peers
        final WifiP2pDevice device = (WifiP2pDevice) peers.get(0);
        final WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "connect(): Success, deviceName: " + device.deviceName);
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "connect(): Failure: " + reason);
            }
        });
    }

    private void writeMessage(String s) {
        Log.d(TAG, "writeMessage()");
        AsyncTask<String, Void, String> asyncTask = new ConnectionAsyncTask(this).execute();
        //connectionAsyncTask.doInBackground(null);
        try {
            String message = asyncTask.get();
            loveMessagelistener.onLoveMessageReceive(message);
            Log.d(TAG, "writeMessage(): Nachricht erhalten: " + message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    public void disconnect() {
        Log.d(TAG, "disconnect()");
        if (mManager != null && mChannel != null) {
            mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
                @Override
                public void onGroupInfoAvailable(WifiP2pGroup group) {
                    if (group != null && mManager != null && mChannel != null
                            /*&& group.isGroupOwner()*/) {
                        mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {

                            @Override
                            public void onSuccess() {
                                setIsConnected(false);
                                setShouldDisconnect(false);
                                Log.d(TAG, "disconnect: removeGroup onSuccess -");
                                initiate();
                            }

                            @Override
                            public void onFailure(int reason) {
                                Log.d(TAG, "disconnect(): removeGroup onFailure -" + reason);
                            }
                        });
                    }
                }
            });
        }
    }

    protected void onResume() {
        Log.d(TAG, "onResume()");
        receiver = new P2pBroadcastReceiver(mManager, mChannel, this);
        activity.registerReceiver(receiver, intentFilter);
    }

    protected void onPause() {
        Log.d(TAG, "onPause()");
        if (isConnected) {
            disconnect();
        }
        activity.unregisterReceiver(receiver);
    }

    protected WifiP2pManager.ConnectionInfoListener getConnectionInfoListener() {
        return connectionInfoListener;
    }

    protected WifiP2pManager.PeerListListener getPeerListListener() {
        return peerListListener;
    }

    protected boolean getIsGroupOwner() {
        return isGroupOwner;
    }

    protected String getGroupOwnerAdress() {
        return groupOwnerAdress;
    }

    protected void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    synchronized public static boolean isShouldDisconnect() {
        return shouldDisconnect;
    }

    synchronized public static void setShouldDisconnect(boolean shouldDisconnect) {
        P2pInterface.shouldDisconnect = shouldDisconnect;
    }
}
