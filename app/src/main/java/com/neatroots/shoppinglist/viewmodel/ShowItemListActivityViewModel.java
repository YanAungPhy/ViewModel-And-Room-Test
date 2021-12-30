package com.neatroots.shoppinglist.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.neatroots.shoppinglist.db.AppDatabase;
import com.neatroots.shoppinglist.db.Category;
import com.neatroots.shoppinglist.db.Items;

import java.util.List;

public class ShowItemListActivityViewModel extends AndroidViewModel {
    private MutableLiveData<List<Items>> listOfItems;
    private AppDatabase appDatabase;

    public ShowItemListActivityViewModel( Application application) {
        super(application);
        listOfItems = new MutableLiveData<>();

        appDatabase = AppDatabase.getDBinstance(getApplication().getApplicationContext());
    }

    public MutableLiveData<List<Items>> getListItemObserver(){
        return listOfItems;
    }

    public void getAllItemsList(int categoryId){
        List<Items> itemsList = appDatabase.shoppingLIstDao().getAllItemsList(categoryId);
        if(itemsList.size() > 0){
            listOfItems.postValue(itemsList);
        }else {
            listOfItems.postValue(null);
        }
    }

    public void insertItems(Items items){
        appDatabase.shoppingLIstDao().insertItems(items);
        getAllItemsList(items.categoryId);
    }

    public void updateItems(Items items){
        appDatabase.shoppingLIstDao().updateItems(items);
        getAllItemsList(items.categoryId);
    }

    public void deleteItems(Items items){
        appDatabase.shoppingLIstDao().deleteItems(items);
        getAllItemsList(items.categoryId);
    }
}
