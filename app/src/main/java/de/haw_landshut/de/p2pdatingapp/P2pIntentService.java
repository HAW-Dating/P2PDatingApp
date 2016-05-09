/*
 * Copyright (c) 2016. Alisa Buchner, Derya Turkmen, Daniel Altrichter, Tobias Weiden, David Manhart, Georg Held
 *
 *
 */

package de.haw_landshut.de.p2pdatingapp;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class P2pIntentService extends IntentService {

    public static final String TAG = "P2pIntentService";

    final HashMap<String, String> profiles = new HashMap<String, String>();
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public P2pIntentService(String name) {
        super(name);
    }

    public P2pIntentService(){
        super("P2pService");
    }



    //Hier gehts ab
    @Override
    protected void onHandleIntent(Intent workIntent){
        //Doing the work here
        //Sending an Intent to LocalBroadcastManager
        Log.d(TAG, "Service Intent started");

        //Reading data...
        String dataString = workIntent.getDataString();

        // TODO: 09.05.2016  Which informations should be inserted?
        //Creating map about informations to share
        Map record = new HashMap();
        record.put("Profile", dataString);


        // TODO: 09.05.2016 Add an better instance name and no hard coding
        //Creating a service
        WifiP2pDnsSdServiceInfo serviceInfo = WifiP2pDnsSdServiceInfo
                .newInstance("Magic Instance Name", "HAW Dating App Hot Coffee", record);


        //Initialising WifiP2pManager and Channel
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);

        //Add localServiec with service Info
        mManager.addLocalService(mChannel, serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Local service successfully added");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Failed to add the local service, reason: " + reason);
            }
        });


        discoverService();

    }








    private P2pIntentService getThis(){return this;}

    //Implementing the DnsSdTxtRecordListener and the DnsSdResponseListener
    //and discovering other services...
    private void discoverService(){

        //Is there another Service?
        //If we discover one, this Listener will react...
        //We will save his address, if his instanceName and registrationType are ok...
        WifiP2pManager.DnsSdServiceResponseListener servListener = new WifiP2pManager.DnsSdServiceResponseListener() {
            @Override
            public void onDnsSdServiceAvailable(String instanceName, String registrationType, WifiP2pDevice srcDevice) {
                Log.d(TAG, "Service Available: " + srcDevice.deviceAddress + "; " + srcDevice.deviceName);
                if(instanceName == "Magic Instance Name" && registrationType == "HAW Dating App Hot Coffee"){
                    profiles.put(srcDevice.deviceAddress, "");
                }
            }
        };

        //Does this Service have an Map?
        //Does it depend to our App?
        //Save his address and profile...
        WifiP2pManager.DnsSdTxtRecordListener txtListener = new WifiP2pManager.DnsSdTxtRecordListener() {
            @Override
            public void onDnsSdTxtRecordAvailable(String fullDomainName, Map<String, String> txtRecordMap, WifiP2pDevice srcDevice) {
                if(profiles.containsKey(srcDevice.deviceAddress)){
                    profiles.put(srcDevice.deviceAddress, txtRecordMap.get("Profile"));
                    Log.d(TAG, "Added a Service: " + srcDevice.deviceAddress);
                }
            }
        };

        //Set these Listeners...
        mManager.setDnsSdResponseListeners(mChannel, servListener, txtListener);

        //Add service request
        WifiP2pDnsSdServiceRequest serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        mManager.addServiceRequest(mChannel, serviceRequest, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Added Service Request successfully");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Failed to add Service Request");
            }
        });

        //Lets start to discover those old, sneaky Sons of a P2p Service
        mManager.discoverServices(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Now Discovering Services");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Failed to discover Services");
            }
        });
    }
}
