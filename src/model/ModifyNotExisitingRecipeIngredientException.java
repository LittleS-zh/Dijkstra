package model;

public class ModifyNotExisitingRecipeIngredientException extends Exception {
    public ModifyNotExisitingRecipeIngredientException(String msg){
        super(msg);
    }
}
