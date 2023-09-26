package model.resources;

import java.util.List;

public class Ingredients {
    private List<String> ingredients;

    public Ingredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public Ingredients() {
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
