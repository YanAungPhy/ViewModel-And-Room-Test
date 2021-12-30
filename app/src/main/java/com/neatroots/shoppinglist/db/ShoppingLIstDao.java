package com.neatroots.shoppinglist.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShoppingLIstDao {

    //for categoryList
    @Query("Select * from Category")
    List<Category> getAllCategoryList();

    @Insert
    void insertCategory(Category...categories);

    @Update
    void  updateCategory(Category categories);

    @Delete
    void  deleteCategory(Category categories);


    //for itemlist
    @Query("Select * from Items where categoryId = :catId")
    List<Items> getAllItemsList(int catId);

    @Insert
    void insertItems(Items  items);

    @Update
    void updateItems(Items items);

    @Delete
    void deleteItems(Items items);

}
