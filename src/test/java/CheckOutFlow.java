import io.restassured.path.json.JsonPath;
import io.restassured.path.json.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.given;

public class CheckOutFlow {

    String authToken;
    String line_item_id_to_remove;
    @BeforeClass
    public void authToken(){
        Response res = given()
                .formParam("grant_type","password")
                .formParam("username","rajanith@gmail.com")
                .formParam("password","spree123")
                .post("https://spree-vapasi-prod.herokuapp.com/spree_oauth/token");

        authToken = "Bearer " + res.path("access_token");

        System.out.println("Auth token created for the user is " + authToken);
    }

    @Test (priority = 1)
    public void showProduct(){

        Response res = given()
                .log().all()
                .queryParam("filter[name]","bag")
                .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products");

        JsonPath jsonresponse = res.jsonPath();
        List<Map> products = jsonresponse.getList("data");

        System.out.println("Products returned for the search tag bag are: ");

        for (Map prodlist: products)
        {
            Map attributes = (Map) prodlist.get("attributes");
            System.out.println(attributes.get("name"));
            //  Assert.assertEquals(attributes.get("name"),"USD","currency is USD");

        }

    }

    @Test (priority = 2)
    public void addItemToCart() {

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", authToken);

        String createdBy = "{\n" +
                "  \"variant_id\": \"17\",\n" +
                "  \"quantity\": 5\n" +
                "}";

        Response res = given()
                .headers(headers)
                .body(createdBy)
                .when()
                .post("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/cart/add_item");

        Assert.assertEquals(res.statusCode(), 200);
        System.out.println("Added Item to the cart");
    }

    @Test (priority = 3)
    public void viewCart() {

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", authToken);

        Response res = given()
                .headers(headers)
                .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/cart");




        List<Object> relItems = (List<Object>) res.jsonPath().getList("data.relationships.line_items.data.id");

        line_item_id_to_remove = relItems.get(0).toString();

        System.out.println("Number of Line Items in the cart "+ relItems.size());

        //System.out.println("Cart data is :" + res.prettyPrint());

    }

    @Test (priority = 4)
    public void removeLineItem() {

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", authToken);

        System.out.println("Removing line item "+line_item_id_to_remove +" from the cart");

        Response res = given()
                .headers(headers)
                .delete("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/cart/remove_line_item/"+ line_item_id_to_remove) ;
        Assert.assertEquals(res.statusCode(), 200);
        // System.out.println("Cart after removing the line item " + res.prettyPrint());
    }
}
