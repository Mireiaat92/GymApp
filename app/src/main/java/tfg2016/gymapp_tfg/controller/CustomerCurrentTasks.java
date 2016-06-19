package tfg2016.gymapp_tfg.controller;

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

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import tfg2016.gymapp_tfg.R;
import tfg2016.gymapp_tfg.model.Client;
import tfg2016.gymapp_tfg.model.Entrenador;
import tfg2016.gymapp_tfg.model.Tasca;
import tfg2016.gymapp_tfg.resources.Complements;

/**
 * Created by Mireia on 18/06/2016.
 */
public class CustomerCurrentTasks extends Fragment {

    // Declare Variables
    ListView listview;
    List<ParseObject> ob;
    ArrayAdapter<String> adapter;

    private Client myClient;
    public Client getMyClient() {
        return myClient;
    }
    public void setMyClient(Client myClient) {
        this.myClient = myClient;
    }

    private Tasca selectedTasca;
    public Tasca getSelectedTasca() {
        return selectedTasca;
    }
    public void setSelectedTasca(Tasca selectedTasca) {
        this.selectedTasca = selectedTasca;
    }

    private Entrenador myEntrenador;

    private static Intent intent;

    long DAY_IN_MILIS = 1000*60*60*24;
    Date date = new Date(System.currentTimeMillis() - (7 * DAY_IN_MILIS));
    Date dateWeekAgo = new Date(date.getTime() - (7*DAY_IN_MILIS));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_customer_current_tasks, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new RemoteDataTask().execute();
    }

        public class RemoteDataTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... params) {
                // Locate the class table named "TASQUES" in Parse.com
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "TASQUES");

                query.whereEqualTo("ID_Client", getMyClient().getObjectId());
                query.whereGreaterThanOrEqualTo("Due_Date", dateWeekAgo);
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

                adapter = new ArrayAdapter<>(getActivity(), R.layout.item_task);

                // Retrieve object "name" from Parse.com database
                for (ParseObject tasques : ob) {
                    String date = Complements.convertStringToDate(tasques.getDate("Due_Date"));
                    adapter.add(date + " - " + tasques.get("Titol"));
                }
                // Binds the Adapter to the ListView
                listview.setAdapter(adapter);

                // Capture button clicks on ListView items
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // Send single item click data to SingleItemView Class
                        Intent i = new Intent(getActivity(), TaskViewFromClient.class);

                        try {
                            HashMap<String, Object> params = new HashMap<String, Object>();
                            params.put("objectid", ob.get(position).getObjectId());
                            List<ParseObject> nameResponse = null;

                            nameResponse = ParseCloud.callFunction("checkTascaData", params);
                            ParseObject userParse = nameResponse.iterator().next();
                            selectedTasca = new Tasca(userParse.getString("idClient"), userParse.getString("Titol"), userParse.getString("Descripcio"), userParse.getDate("Due_Date"), userParse.getBoolean("Completada"), userParse.getString("Comentari"), userParse.getObjectId());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        i.putExtra("selectedTasca", selectedTasca);
                        i.putExtra("myClient", myClient);

                        startActivity(i);
                    }
                });
            }
        }

}