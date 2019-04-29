package controller;

import model.EmptyIngredientNameException;
import model.Ingredient;
import model.InvalidIngredientAmountException;
import model.StorageIngredient;
import utils.DatabaseHelper;
import utils.SQLiteConnectionException;
import utils.UnitEnum;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StorageIngredientController implements DatabaseController<StorageIngredient> {
    public StorageIngredientController() {
        // Nothing to do
    }

    public ArrayList<StorageIngredient> getAll() throws FetchDataException, InvalidIngredientAmountException, EmptyIngredientNameException {
        DatabaseHelper dbHelper = new DatabaseHelper();
        dbHelper.connectSQLite();
        ArrayList<StorageIngredient> ingredients = new ArrayList<StorageIngredient>();
        String name, unit;
        double amount;
        int id;

        try {
            ResultSet rs = dbHelper.execSqlWithReturn("SELECT * FROM Ingredient");
            while (rs.next()) {
                id = rs.getInt(1);
                name = rs.getString(2);
                amount = rs.getDouble(3);
                unit = rs.getString(4);
                ingredients.add(new StorageIngredient(id, name, amount, UnitEnum.valueOf(unit)));
            }
            dbHelper.closeConnection();
        } catch (SQLException | SQLiteConnectionException e) {
            e.printStackTrace();
            throw new FetchDataException("Could not fetch Storage Ingredients");
        }
        return ingredients;
    }

    public boolean update(StorageIngredient ingredient) {
        DatabaseHelper dbHelper = new DatabaseHelper();
        dbHelper.connectSQLite();
        String query = String.format("UPDATE Ingredient SET Amount=%f,Unit='%s' WHERE Ingredient_ID=%d",
                ingredient.getAmount(), ingredient.getUnit().toString(), ingredient.getID());

        try {
            dbHelper.execSqlUpdate(query);
            dbHelper.closeConnection();
        } catch (SQLiteConnectionException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insert(StorageIngredient ingredient) {
        DatabaseHelper dbHelper = new DatabaseHelper();
        dbHelper.connectSQLite();
        String query = String.format("INSERT INTO Ingredient (Name, Amount, Unit) VALUES ('%s',%f,'%s')",
                ingredient.getName(), ingredient.getAmount(), ingredient.getUnit().toString());

        try {
            dbHelper.execSqlNoReturn(query);
            dbHelper.closeConnection();
        } catch (SQLiteConnectionException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        try {
            StorageIngredient ingredient = new StorageIngredient(1, "Water", 2, UnitEnum.LITER);
            StorageIngredientController sc = new StorageIngredientController();
            sc.update(ingredient);
            System.out.println(sc.getAll());
        } catch (EmptyIngredientNameException | InvalidIngredientAmountException | FetchDataException e) {
            e.printStackTrace();
        }
    }
}
