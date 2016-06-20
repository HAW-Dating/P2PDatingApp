package de.haw_landshut.haw_dating.p2pdatingapp.P2p;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    private Activity activity;
    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel mChannel;
    private WifiP2pManager mManager;
    private P2pBroadcastReceiver receiver;
    private List peers = new ArrayList();
    private boolean isGroupOwner = false;
    String groupOwnerAdress;
    private boolean isConnected = false;
    public static final String NOT_VALID_YET = "Not valid yet";
    private static String profile = NOT_VALID_YET;

    // TODO: 15.06.2016 Add in App
    private final FindYourLoveMessageListener loveMessagelistener;

    // TODO: 15.06.2016 Add to App
    public static final int FAILURE_BUSY = 2;

    synchronized public static boolean isShouldDisconnect() {
        return shouldDisconnect;
    }

    synchronized public static void setShouldDisconnect(boolean shouldDisconnect) {
        P2pInterface.shouldDisconnect = shouldDisconnect;
    }

    private static boolean shouldDisconnect = false;
    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
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
                Log.d(TAG, "PeerListListener: found " + peers.size() + " peers, top peer: " + p2pDevice.deviceName);
            }

        }
    };

    //Notifies me, when the Connection Info changes
    private WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            if (isShouldDisconnect()) {
                Log.d(TAG, "ConnectionInfoListener: should disconnect");
                //isGroupOwner = false;
                //disconnect();
                //discover();
                return;
            } else {
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

                } else if (info.groupFormed && info.groupOwnerAddress != null) {
                    // The other device acts as the client. In this case,
                    // you'll want to create a client thread that connects to the group
                    // owner.
                    isGroupOwner = false;
                    Log.d(TAG, "ConnectionInfoListener: You are not the GroupOwner");
                    writeMessage(profile);
                }
            }

        }
    };

    public P2pInterface(Activity activity, FindYourLoveMessageListener listener) {
        this.activity = activity;
        this.loveMessagelistener = listener;
        Log.d(TAG, "created P2pInterface");
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

    protected void discover() {
        Log.d(TAG, "discover()");
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "discover(): Success");
            }

            @Override
            public void onFailure(int reasonCode) {
                Log.d(TAG, "discover(): Failure: " + reasonCode);
                if (reasonCode == mManager.BUSY) {
                    restart();
                }
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

        if (!peers.isEmpty()) {
            setShouldDisconnect(false);
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
                    if (reason == mManager.BUSY) {
                        restart();
                    }
                }
            });
        } else if (peers.isEmpty()) {
            Toast.makeText(activity, "There are no other hot lovers around.\nTry again later, you stinky bastard!", Toast.LENGTH_LONG).show();
        }
    }

    private void writeMessage(String s) {
        Log.d(TAG, "writeMessage()");
        AsyncTask<String, Void, String> asyncTask = new ConnectionAsyncTask(this).execute();
        //connectionAsyncTask.doInBackground(null);
        try {
            String message = asyncTask.get(5, TimeUnit.SECONDS);
            Log.d(TAG, "writeMessage(): Nachricht erhalten: " + message);
            loveMessagelistener.onLoveMessageReceive(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            Log.d(TAG, "writeMessage(): asyncTask TimeOut");
            setShouldDisconnect(true);
            if (!asyncTask.cancel(true)) {
                Log.d(TAG, "writeMessage(): asyncTask couldn't be canceled");
            }
        }
        disconnect();
        //restart();
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
                                Log.d(TAG, "disconnect: removeGroup onSuccess -");
                            }

                            @Override
                            public void onFailure(int reason) {
                                Log.d(TAG, "disconnect(): removeGroup onFailure -" + reason);
                            }
                        });
                        restart();
                    }
                }
            });
        }
    }

    public void onResume() {
        Log.d(TAG, "onResume()");
        receiver = new P2pBroadcastReceiver(mManager, mChannel, this);
        activity.registerReceiver(receiver, intentFilter);
        discover();
    }

    public void onPause() {
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

    // TODO: 10.06.2016 Add to app


    synchronized protected static void setProfile(String profile) {
        P2pInterface.profile = profile;
    }

    synchronized protected static String getProfile() {
        return profile;
    }

    private void restart() {
        Log.d(TAG, "restart()");
        mManager.cancelConnect(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "restart(): cancelConnect(): success");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "restart(): cancelConnect(): fail: " + reason);
            }
        });

        mManager.stopPeerDiscovery(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "restart(): stopPeerDiscovery: success");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "restart(): stopPeerDiscovery: fail: " + reason);
            }
        });
        discover();

    }

    public void setIsGroupOwner(boolean isGroupOwner) {
        this.isGroupOwner = isGroupOwner;
    }


}
