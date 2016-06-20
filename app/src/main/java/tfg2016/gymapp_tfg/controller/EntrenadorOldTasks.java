package tfg2016.gymapp_tfg.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.model.Entrenador;

/**
 * Created by Mireia on 19/06/2016.
 */
public class EntrenadorOldTasks extends Fragment {
    // Declare Variables
    ListView listview;
    List<ParseObject> ob;
    ProgressDialog mProgressDialog;
    ArrayAdapter<String> adapter;

    private Client selectedClient;
    public Client getSelectedClient() {
        return selectedClient;
    }
    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }

    private Entrenador myEntrenador;
    public Entrenador getMyEntrenador() {
        return myEntrenador;
    }
    public void setMyEntrenador(Entrenador myEntrenador) {
        this.myEntrenador = myEntrenador;
    }

    long DAY_IN_MILIS = 1000*60*60*24;
    Date date = new Date(System.currentTimeMillis() - (7 * DAY_IN_MILIS));
    Date dateWeekAgo = new Date(date.getTime() - (7*DAY_IN_MILIS));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_entrenador_old_tasks, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new RemoteDataTask().execute();
    }
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            // Locate the class table named "TASQUES" in Parse.com
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("TASQUES");
            query.whereEqualTo("ID_Client", selectedClient.getObjectId());
            query.whereEqualTo("Completada", true);
            query.orderByAscending("Due_Date");
            try {
                ob = query.find();
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Locate the listview in listview_main.xml
            listview = (ListView) getView().findViewById(R.id.tascalistview);
            // Pass the results into an ArrayAdapter
            adapter = new ArrayAdapter<String>(getActivity(),
                    R.layout.item);
            // Retrieve object "name" from Parse.com database
            for (ParseObject tasques : ob) {

                String date = convertStringToDate(tasques.getDate("Due_Date"));

                adapter.add(date + " - "+ tasques.get("Titol"));

            }
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Capture button clicks on ListView items
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // Send single item click data to SingleItemView Class
                    Intent i = new Intent(getActivity(), TaskViewFromEntrenador.class);

                    String selectedTascaId = ob.get(position).getObjectId();


                    i.putExtra("selectedTascaId", selectedTascaId);
                    i.putExtra("selectedClient", selectedClient);
                    i.putExtra("myEntrenador", myEntrenador);

                    startActivity(i);
                }
            });
        }
    }

    public String convertStringToDate(Date indate)
    {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("dd MMM");

        try{
            dateString = sdfr.format( indate );
        }catch (Exception ex ){
            System.out.println(ex);
        }
        return dateString;
    }
}
