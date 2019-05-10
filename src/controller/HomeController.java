package controller;

import model.Equipment;
import model.Recipe;
import model.StorageIngredient;
import utils.EmptyNameException;
import utils.InvalidInputException;
import utils.FetchDataException;
import view.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HomeController {
    public HomeController(){
        // Nothing to do
    }
    public void startManageRecipe(){
        RecipeListController rlc = new RecipeListController();
        try {
            RecipeListView rlv = new RecipeListView(rlc, Recipe.getAll());
            rlv.setVisible(true);
        } catch (FetchDataException | EmptyNameException | InvalidInputException e) {
            e.printStackTrace();
        }
    }
    public void startManageIngredient(){
        IngredientListController ilc = new IngredientListController();
        try {
            IngredientListView ilv = new IngredientListView(ilc, StorageIngredient.getAll());
            ilv.setVisible(true);
        } catch (FetchDataException e) {
            e.printStackTrace();
        }
    }
    public void startNoteList(){
        NoteListController nlc = new NoteListController();
        NoteListView nlv = new NoteListView(nlc);
        nlv.setVisible(true);
    }
    public void startEquipmentInformation(){
        Equipment equipment;
        try {
            equipment = Equipment.getEquipment(1);
            EquipmentInfoController eic = new EquipmentInfoController(equipment);
            EquipmentInfoView ei = new EquipmentInfoView(eic, equipment);
            equipment.setModelListener(ei);
            ei.setVisible(true);
        } catch (FetchDataException | EmptyNameException | InvalidInputException e) {
            e.printStackTrace();
        }
    }
    public void startRecommend(){
// TODO: Check if there are enough ingredient
        try {
            ArrayList<Recipe> recommendRecipe =  new Recipe().getAll();
            ArrayList<Integer> notAvailableList = new ArrayList();
            RecommendRecipeListController rrlc = new RecommendRecipeListController(recommendRecipe);
            boolean viewStatus = true;
            for (Recipe recipe : recommendRecipe) {
                if (recipe.isAvailable()) {
                    viewStatus = false;
                }
                else{
                    //recommendRecipe.remove(recipe);
                    notAvailableList.add(recommendRecipe.indexOf(recipe));
                }
            }
            if (!viewStatus){
                for(Integer index: notAvailableList){
                    recommendRecipe.remove(index.intValue());
                }
            }
            RecommendRecipeListView rrlv = new RecommendRecipeListView(rrlc,recommendRecipe, viewStatus);
            rrlv.setVisible(true);
        } catch (FetchDataException | EmptyNameException | InvalidInputException e) {
            e.printStackTrace();
        }
    }
}
