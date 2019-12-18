import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyData;
import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BasicTest {

    String authToken;

    @Test
    public void testStatusCode()
    {
        given()
                .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products")
                .then()
                .statusCode(200);

    }
    @Test
    public void testLogging() {
        given()
                .log().all()
                .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products");
    }
     @Test
     public void printResponse() {
            Response res= given().when()
                                  .log().all()
                                   .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products");
            System.out.println(res.prettyPrint());
        }


      @Test
      public void testCurrencyCode(){

            given().when()
                    .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products")
                    .then()
                    .body("data[0].attributes.currency", equalTo("USD"));

            System.out.println();

      }

    @Test
    public void testCurrencyCodeForMoreProducts(){

        Response res= given().when()
                .log().all()
                .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products");

        JsonPath jsonresponse = res.jsonPath();
        List<Map> products = jsonresponse.getList("data");
        for (Map prodlist: products)
        {
            Map attributes = (Map) prodlist.get("attributes");
            Assert.assertEquals(attributes.get("currency"),"USD","currency is USD");
        }

    }
     @Test
     public void testFilter(){

       Response res = given()
                 .log().all()
                 .queryParam("filter[name]","bag")
                .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products");

         JsonPath jsonresponse = res.jsonPath();
         List<Map> products = jsonresponse.getList("data");
         for (Map prodlist: products)
         {
             Map attributes = (Map) prodlist.get("attributes");
             System.out.println(attributes.get("name"));
           //  Assert.assertEquals(attributes.get("name"),"USD","currency is USD");

         }

     }

    @Test
    public void testFilterById(){

        Response res = given()
                .log().all()
                .queryParam("filter[ids]",2)
                .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products");

        JsonPath jsonresponse = res.jsonPath();
        List<Map> products = jsonresponse.getList("data");
        for (Map prodlist: products)
        {
            String id = (String) prodlist.get("id");
            System.out.println(id);
            //  Assert.assertEquals(attributes.get("name"),"USD","currency is USD");

        }

    }

    @Test
    public void testFilterByPrice(){

        Response res = given()
                .log().all()
                .queryParam("filter[price]",0.00 - 150.99)
                .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products");

        System.out.println(res.prettyPrint());

    }

    @Test
    public void testFilterBySort(){

        Response res = given()
                .log().all()
                .queryParam("filter[price]","15.99")
                .get("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/products");

        System.out.println(res.prettyPrint());

    }

    @BeforeClass
    public void authToken(){
        Response res = given()
                            .formParam("grant_type","password")
                            .formParam("username","test123@gmail.com")
                            .formParam("password","test123")
                            .post("https://spree-vapasi-prod.herokuapp.com/spree_oauth/token");

        authToken = "Bearer " + res.path("access_token");


        System.out.println(authToken);
    }

    @Test
    public void testPostCall(){

        Map<String,String> headers = new HashMap<String, String>();
        headers.put("Content-Type","application/json");
        headers.put("Authorization" , authToken);

        String createdBy = "{\n" +
                "  \"variant_id\": \"17\",\n" +
                "  \"quantity\": 5\n" +
                "}";

        Response res = given()
                            .headers(headers)
                            .body(createdBy)
                            .when()
                            .post("https://spree-vapasi-prod.herokuapp.com/api/v2/storefront/cart/add_item");

        Assert.assertEquals(res.statusCode(),200);

    }



}
