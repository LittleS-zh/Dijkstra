package model;

import controller.ModelListener;
import utils.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StorageIngredient extends Ingredient implements IDatabaseOperation {
    private ModelListener listener;


    public StorageIngredient() {
        // Nothing to do
    }

    public StorageIngredient(int id) throws EmptyNameException, InvalidInputException {
        this.setID(id);
        DatabaseHelper dbHelper = new DatabaseHelper();
        String query = String.format("SELECT * FROM Ingredient WHERE Ingredient_ID=%d", id);
        try {
            ResultSet rs = dbHelper.execSqlWithReturn(query);
            this.setName(rs.getString(2));
            this.setUnit(UnitEnum.valueOf(rs.getString(4)));
            this.setAmount(rs.getDouble(3));
            dbHelper.closeConnection();
        } catch (SQLiteConnectionException | SQLException e) {
            e.printStackTrace();
        }
    }

    public StorageIngredient(int id, String name, double amount, UnitEnum unit) throws EmptyNameException,
            InvalidInputException {
        super(id, name, amount, unit);
    }

    @Override
    public void addListener(ModelListener listener) {
        this.listener = listener;
    }

    @Override
    public void notifyListener() {
        this.listener.update();
    }

    /**
     * Add the amount of storage ingredient.
     * @param additionAmount The delta of addition amount.
     * @throws InvalidInputException Throws when amount is invalid.
     */
    public void addAmount(double additionAmount) throws InvalidInputException {
        if (additionAmount <= 0) {
            throw new InvalidInputException("Addition amount should be greater than 0!");
        } else {
            this.setAmount(this.getAmount() + additionAmount);
        }
    }

    @Override
    public boolean insert() {
        DatabaseHelper dbHelper = new DatabaseHelper();
        String query = String.format("INSERT OR IGNORE INTO Ingredient (Name, Amount, Unit) VALUES ('%s',%f,'%s')",
                stringParser(this.getName()), this.getAmount(), this.getUnit().toString());
        try {
            dbHelper.execSqlNoReturn(query);
            dbHelper.closeConnection();
        } catch (SQLiteConnectionException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean update() {
        DatabaseHelper dbHelper = new DatabaseHelper();
        String query = String.format("UPDATE Ingredient SET Name='%s',Amount=%f,Unit='%s' WHERE Ingredient_ID=%d",
                stringParser(this.getName()), this.getAmount(), this.getUnit().toString(), this.getID());

        try {
            dbHelper.execSqlUpdate(query);
            dbHelper.closeConnection();
        } catch (SQLiteConnectionException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean delete() {
        DatabaseHelper dbHelper = new DatabaseHelper();
        String checkExist = String.format("SELECT * FROM Ingredient_in_Recipe WHERE Ingredient_ID=%d", this.getID());
        try {
            boolean status = true;
            ResultSet rs = dbHelper.execSqlWithReturn(checkExist);
            while (rs.next()) {
                status = false;
            }
            if (!status)
                return false;
        } catch (SQLException | SQLiteConnectionException e) {
            e.printStackTrace();
        }

        String query = String.format("DELETE FROM Ingredient WHERE Ingredient_ID=%d", this.getID());
        try {
            dbHelper.execSqlNoReturn(query);
            dbHelper.closeConnection();
        } catch (SQLiteConnectionException e) {
            e.printStackTrace();
            return false;
        }

        notifyListener();
        return true;
    }

    /**
     * Get all the storage ingredients in database.
     * @return The ArrayList of all the StorageIngredients.
     * @throws FetchDataException Throws when database operation failed.
     */
    public static ArrayList<StorageIngredient> getAll() throws FetchDataException {
        DatabaseHelper dbHelper = new DatabaseHelper();
        ArrayList<StorageIngredient> ingredients = new ArrayList<>();
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
            throw new FetchDataException("Could not fetch Storage Ingredients.");
        } catch (EmptyNameException | InvalidInputException e) {
            e.printStackTrace();
        }

        return ingredients;
    }
}
