package food;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        for (int i = 1; i <= 100; i++) {
            var recipes = getRecipes(i);
            writeToCsv(recipes);
            System.out.println(recipes);
            Thread.sleep(500);
        }

    }

    public static void writeToCsv(List<Recipe> recipeList) throws IOException {
        PrintWriter writer = new PrintWriter(new FileOutputStream("/home/dc/Documents/recipes.csv", true));

        for (Recipe sample : recipeList) {
            writer.println(sample.toString());
        }
        writer.close();
    }

    private static List<Recipe> getRecipes(int page) throws IOException {
        List<Recipe> recipesList = new ArrayList<>();

        Document doc = Jsoup.connect("https://www.povarenok.ru/recipes/~" + page + "/?sort=date_create&order=desc").get();
        Elements recipes = doc.getElementsByTag("article");

        for (Element e : recipes) {
            Recipe recipe = new Recipe();
            Element recipeInfo = e.getElementsByTag("h2").first().getElementsByAttribute("href").first();
            recipe.setUrl(recipeInfo.attr("href"));
            recipe.setName(String.valueOf(recipeInfo.firstChild()));
            recipe.setImg(e.getElementsByTag("img").first().attr("src"));
            recipe.setCategory(String.valueOf(e.getElementsByClass("article-breadcrumbs").first().getElementsByAttribute("href").first().firstChild()));
            recipe.setSummary(String.valueOf(e.getElementsByTag("p").get(1).firstChild()));
            recipesList.add(recipe);
        }
        return recipesList;
    }

}