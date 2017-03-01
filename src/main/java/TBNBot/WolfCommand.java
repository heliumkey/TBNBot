package TBNBot;

import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WASubpod;

import java.util.ArrayList;
import java.util.List;


public class WolfCommand {

    WolfQuery wQuery;
    //boolean blocking = false;


    WolfCommand(WolfQuery w){
        wQuery = w;

    }

    public List command(String query){

        //mE.getBot().sendIRC().message(mE.getChannel().getName(), "\u00034" + pod.getTitle() + ": " + "\u000f" + ((WAPlainText) element).getText());

        List queryResult = WolfQuery.query(query);
        WAPod[] podArr = (WAPod[]) queryResult.get(0);
        List podTitles = new ArrayList();
        List podElements = new ArrayList();
        String url = (String) queryResult.get(1);

        for(WAPod pod : podArr){
            for(WASubpod subPod : pod.getSubpods()){
                for(Object element : subPod.getContents()){
                    if (element instanceof WAPlainText) {

                        String elementText = ((WAPlainText) element).getText();

                        //check amount of lines in each element text
                        if(elementText.split("[\n|\r]").length <= 15 && !elementText.isEmpty()){

                            podTitles.add(pod.getTitle());
                            podElements.add(elementText);

                        }
                    }
                }
            }
        }

        String[] podTitleResults = (String[]) podTitles.toArray(new String[podTitles.size()]);
        String[] podElementResults = (String[]) podElements.toArray(new String[podElements.size()]);
        List results = new ArrayList();
        results.add(podTitleResults);
        results.add(podElementResults);
        results.add(url);

        //this.blocking = true;

        return results;
    }

    public String lCommand(String query, int index){

        List commandResults = command(query);
        return ((String[]) commandResults.get(1))[index];


    }

}


