package tfg2016.gymapp_tfg.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tfg2016.gymapp_tfg.model.Client;

/**
 * Created by Mireia on 18/06/2016.
 */
public class ClientPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private Client myClient;
    public Client getMyClient() {
        return myClient;
    }
    public void setMyClient(Client myClient) {
        this.myClient = myClient;
    }

    public ClientPagerAdapter(FragmentManager fm, int NumOfTabs, Client client) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        setMyClient(client);

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                CustomerCurrentTasks tabCurrent = new CustomerCurrentTasks();
                tabCurrent.setMyClient(getMyClient());
                return tabCurrent;
            case 1:
                CustomerOldTasks tabOld = new CustomerOldTasks();
                tabOld.setMyClient(getMyClient());
                return tabOld;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}