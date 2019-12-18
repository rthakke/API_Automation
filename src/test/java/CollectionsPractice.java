import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CollectionsPractice {

    //ArrayList contains Duplicates and
    // maintains Insertion Order
    @Test
    public void arrayListTest(){

        List<String> topCompanies= new ArrayList<String>();
        System.out.println(topCompanies.isEmpty());
        topCompanies.add("Google");
        topCompanies.add("MICROSOFT");
        topCompanies.add("Amazon");
        topCompanies.add("Thoughworks");
        topCompanies.add("Infosys");
        topCompanies.add("Apple");
        topCompanies.add("TCS");
        System.out.println(topCompanies.isEmpty());
       /* for(int i=0;i<topCompanies.size();i++)
        {
            System.out.println(topCompanies.get(i));
        }
        */

        Iterator<String> companiesiterator = topCompanies.iterator();
        while(companiesiterator.hasNext())
            System.out.println(companiesiterator.next());

        System.out.println(topCompanies.get(4));
    }

     @Test
     public void linkedListTest(){

        List<String> humanSpecies = new LinkedList<String>();
        humanSpecies.add("abc");
        humanSpecies.add("xyz");
        humanSpecies.add("Homo Sepians");

        Iterator<String> iterator = humanSpecies.iterator();
       while(iterator.hasNext())
       {
           System.out.println(iterator.next());
       }

        System.out.println(humanSpecies.size());
     }

}
