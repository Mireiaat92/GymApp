package tfg2016.gymapp_tfg.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.model.Entrenador;

/**
 * Created by Mireia on 19/06/2016.
 */
public class EntrenadorPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    private Entrenador myEntrenador;
    public Entrenador getMyEntrenador() {
        return myEntrenador;
    }
    public void setMyEntrenador(Entrenador myEntrenador) {
        this.myEntrenador = myEntrenador;
    }

    private Client selectedClient;
    public Client getSelectedClient() {
        return selectedClient;
    }
    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }

    public EntrenadorPagerAdapter(FragmentManager fm, int NumOfTabs, Entrenador entrenador, Client selectedClient) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        setMyEntrenador(entrenador);
        setSelectedClient(selectedClient);

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                EntrenadorOldTasks tabOld = new EntrenadorOldTasks();
                tabOld.setMyEntrenador(getMyEntrenador());
                tabOld.setSelectedClient(getSelectedClient());
                return tabOld;
            case 1:
                EntrenadorCurrentTasks tabCurrent = new EntrenadorCurrentTasks();
                tabCurrent.setMyEntrenador(getMyEntrenador());
                tabCurrent.setSelectedClient(getSelectedClient());
                return tabCurrent;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}