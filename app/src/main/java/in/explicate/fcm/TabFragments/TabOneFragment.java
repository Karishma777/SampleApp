package in.explicate.fcm.TabFragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.adapter.CurrentNotificationAdapter;
import in.explicate.fcm.database.DbHandler;
import in.explicate.fcm.model.NotificationModel;
import in.explicate.fcm.util.FcmUtility;

public class TabOneFragment extends Fragment  {


    private SwipeRefreshLayout refreshLayout;
    private  List<NotificationModel> add;
    private View view;
    private CurrentNotificationAdapter  adapter;
    private TextView datanotfound;
    private EditText editTextSearch;
    private Button btnPickerTo,btnPickerFrom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.activity_current, container, false);

         datanotfound=(TextView)view.findViewById(R.id.datanotfound);
         editTextSearch=(EditText) view.findViewById(R.id.editsearch);
        btnPickerTo=(Button)view.findViewById(R.id.datepickerTo);
        btnPickerFrom=(Button)view.findViewById(R.id.datepickerfrom);



        btnPickerTo.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 btnPickerTo.setText("");


                 // Get Current Date
                 final Calendar c = Calendar.getInstance();

                 DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                         new DatePickerDialog.OnDateSetListener() {

                             @Override
                             public void onDateSet(DatePicker view, int year,
                                                   int monthOfYear, int dayOfMonth) {

                                 btnPickerTo.setText("From:"+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);


                             }
                         }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                         datePickerDialog.setTitle("Select From Date");
                         datePickerDialog.show();


             }
         });

        btnPickerFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnPickerFrom.setText("");


                // Get Current Date
                final Calendar c = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                btnPickerFrom.setText("To:"+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);


                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setTitle("Select To Date");
                datePickerDialog.show();


            }
        });



        datanotfound.setVisibility(View.GONE);
         add=new ArrayList<>();

        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_layout);
        refreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                addData();
            }
        });

        addData();




        editTextSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(b){

                    datanotfound.setVisibility(View.GONE);

                }else{

                    datanotfound.setVisibility(View.VISIBLE);

                }
            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                final List<NotificationModel> filteredModelList = filter(add, charSequence+"");
                adapter.setFilter(filteredModelList);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

    }


    private void addData(){

        Log.e("AT","addData");


      if(!add.isEmpty()){

          add.clear();
      }


        DbHandler dbHandler=new DbHandler(getActivity());
        dbHandler.open();
        add=dbHandler.getNotifications();
        dbHandler.close();

        refreshLayout.setRefreshing(false);

        show();

    }

    private void show(){

        RecyclerView   recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutParams);

        adapter = new CurrentNotificationAdapter(add,getActivity());
        recyclerView.setAdapter(adapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                refreshLayout.setEnabled(layoutParams.findFirstCompletelyVisibleItemPosition() == 0);

            }
        });

        if (adapter.getItemCount() == 0){

          //  FcmUtility.showToast(getActivity(),"No new notification to show");
            datanotfound.setVisibility(View.VISIBLE);
        }
    }



  /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        adapter.setFilter(add);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<NotificationModel> filteredModelList = filter(add, newText);
        adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

  */

    private List<NotificationModel> filter(List<NotificationModel> models, String query) {
        query = query.toLowerCase();

        final List<NotificationModel> filteredModelList = new ArrayList<>();
        for (NotificationModel model : models) {
            final String text = model.getBody().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

}
