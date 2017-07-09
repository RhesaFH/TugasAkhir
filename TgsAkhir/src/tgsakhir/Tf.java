/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tgsakhir;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 *
 * @author Rhesa
 */
public class Tf {

    String inFile = "src/dataset.txt";
    String stopword = "src/stopwords.txt";
    String delims = "-|\\ ";
    boolean tfidf = false, bobot = false, kemunculan = false, sinonim = false, arti = false;
    float d, X, Y, XX, YY, XY, coun;
    final int ALL_DATA = -1;
    final int NUM_DATA_1 = 1;
    final int NUM_DATA_2 = 2;
    int hitung, rank1, rank2;
    private Idf IDF = null;
    String simpen;
    String[] sw;
    ArrayList<Format> dataset = new ArrayList<>();

    public Tf(Idf idf, boolean sinonim, boolean arti, boolean tfidf) throws ParserConfigurationException, IOException, MalformedURLException, SAXException, InterruptedException{
        Stopw(countKata(stopword));
        this.IDF = idf;
        this.tfidf = tfidf;
        this.bobot = bobot;
        this.kemunculan = kemunculan;
        this.sinonim = sinonim;
        this.arti = arti;
        loadData(ALL_DATA);
        
        // TESTING

        
        for (Format data : dataset) {
            setKatasFromDictToArray(data, NUM_DATA_1);
            setKatasFromDictToArray(data, NUM_DATA_2);
            data.HitungNilaiVektor(IDF, tfidf, sinonim, arti);

            hitung++;
            X = X + data.getGoldStandard();
            Y = Y + data.getVectorScore();
            XY = XY + (data.getGoldStandard() * data.getVectorScore());
            XX = XX + (float) Math.pow(data.getGoldStandard(), 2);
            YY = YY + (float) Math.pow(data.getVectorScore(), 2);
            d = (float) (d + (Math.pow(rank1 - rank2, 2)));

            data.show();
        }
        correlation(X, Y, XY, XX, YY, hitung);
    }


    public int countKata(String p) {
        int counter = 0;
        try {
            BufferedReader finput = new BufferedReader(new InputStreamReader(new FileInputStream(p)));
            while (finput.readLine() != null) {
                counter++;
            }
            finput.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Data Error", JOptionPane.ERROR_MESSAGE);
        }
        return counter;
    }

    public void Stopw(int dataSize) {
        int i = 0, mark1 = 0;
        String tmp;
        sw = new String[dataSize];
        try {
            BufferedReader finput = new BufferedReader(new InputStreamReader(new FileInputStream(stopword)));
            while ((tmp = finput.readLine()) != null) {
                mark1 = tmp.indexOf(' ');

                sw[i] = tmp.substring(0, tmp.length());
                i++;
            }
            finput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Format> getDataset() {
        return dataset;
    }

    public void loadData(int dataSize) {
        if (dataSize == ALL_DATA) {
            int i = 0, mark1 = 0, mark2 = 0;
            String tmp;

            try {
                BufferedReader finput = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
                while ((tmp = finput.readLine()) != null) {
                    mark1 = tmp.indexOf('\t');
                    mark2 = tmp.indexOf('\t', (mark1 + 1));

                    String word1 = tmp.substring(0, mark1);
                    String word2 = tmp.substring(mark1 + 1, mark2);
                    float goldStandard = Float.parseFloat(tmp.substring(mark2 + 1, tmp.length()));

                    dataset.add(new Format(word1, word2, goldStandard));
                    i++;

                }
                finput.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList getNormKatas(String sentence) {
        ArrayList<String> words = new ArrayList<>();
        String word = "";
        for (int i = 0; i < sentence.length(); i++) {
            if (sentence.charAt(i) != ' ') {
                word += sentence.charAt(i);
            } else {
                word = makeNormal(word);
                if (word != "") {
                    words.add(word);
                }
                word = "";
            }
        }
        word = makeNormal(word);
        if (word != "") {
            words.add(word);
        }
        return words;
    }

    public String makeNormal(String oriKata) {
        String result = "";
        String x = "";
        oriKata = oriKata.replaceAll("[^a-z0-9]", "");
        oriKata = tokenizer(oriKata).toLowerCase();
//        System.out.println(oriKata);
        if (!oriKata.equals(null)) {
            for (int i = 0; i < sw.length; i++) {
//            System.out.println(sw[i]);
                if (oriKata.equals(sw[i])) {
                    oriKata = "";
                }
            }
            result = oriKata;
        }
        result = Stemm(result);
        return result;
    }
    
    public String Stemm(String word){
       String res = "";
       Stemmer stemmer = new Stemmer();
       String[] tokens = word.split(" ");  //tokenisasi
       int tokenCount = tokens.length;
       for (int j = 0; j < tokenCount; j++) {  //stemming
        		char[] chars = tokens[j].toCharArray();
                        int len = stemmer.stem(chars, chars.length, true);
                        res += new String(chars,0,len);
		}
        return res.toString();
    }

    public String tokenizer(String oriKata) {
        char delimiter = ' ';

        // calculate number of delimiter characters
        int N = 0;
        ArrayList<String> tokens = new ArrayList();
        String token = "";
        for (int i = 0; i < oriKata.length(); i++) {
            if (oriKata.charAt(i) == delimiter) {
                if (token.equals("")) {
                    token = "";
                } else {

                    tokens.add(token);
                    token = "";
                    N++;
                }
            } else {
                if (Character.isAlphabetic(oriKata.charAt(i))) {
                    token = token + oriKata.charAt(i);
                }
            }
        }
        tokens.add(token);
        N++;

        String normKata = "";
        for (int i = 0; i < N; i++) {
//            normKata = tokens.get(i);
            normKata = tokens.get(i);
//            System.out.println(i + ": " + word);
        }
        return normKata;
    }

    public String getDef(String word) throws InterruptedException, ParserConfigurationException, MalformedURLException, IOException, SAXException{ //ambil definisi kata dari kbbi melalui api kateglo
        String def = "";
        String temp = null;
        URL url;

        try {
            // get URL content

            String a="http://kateglo.com/api.php?format=xml&phrase="+word+""; //kateglo api
           url = new URL(a);
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                               new InputStreamReader(conn.getInputStream()));

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                    temp = temp + inputLine;
            }
            br.close();
            String temp2 = temp.replaceFirst("</def_text>.*", ""); // ambil definisi
            String temp3 = temp2.replaceFirst(".*<def_text>", ""); // ambil definisi
            def = temp3.replaceFirst(".*</script>", ""); 
            def = def + " " + word;
            Thread.sleep(1000);
            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return def;

//        String def = "";
//        String temp = null;
//        String definisi = "http://kateglo.com/api.php?format=xml&phrase="+word;
//        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
//        Document document = docBuilder.parse(new URL(definisi).openStream());

 //       NodeList nodeList = document.getElementsByTagName("def_text");
 //       def = nodeList.item(0).getTextContent()+" "+word; 
 //       Thread.sleep(1000);
   //     return def;
    }
    
    public String getSinonim(String word) throws ParserConfigurationException, MalformedURLException, IOException, SAXException, InterruptedException{ //ambil definisi kata dari kbbi melalui api kateglo
        String def = "";
        String temp = null;
        String definisi = "http://kateglo.com/api.php?format=xml&phrase="+word;
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document document = docBuilder.parse(new URL(definisi).openStream());

        NodeList flowList = document.getElementsByTagName("s");
        for (int i = 0; i < flowList.getLength(); i++) {
            NodeList childList = flowList.item(i).getChildNodes();
            for (int j = 0; j < childList.getLength(); j++) {
                NodeList childNode = childList.item(j).getChildNodes();
                for (int k = 0; k < childNode.getLength(); k++) {
                    Node child = childNode.item(k);
                    if ("related_phrase".equals(child.getNodeName())) {
                            String w = getDef(childNode.item(k).getTextContent().trim());
                            def = def + w + " ";
                        }
                    }
                }
            }        
        return def;
    }

    public void setKatasFromDictToArray(Format data, int NUM_DATA) throws ParserConfigurationException, IOException, MalformedURLException, SAXException, InterruptedException { // array untuk token
       if (NUM_DATA == 1) {
            String DefinisiData = "";
            String DefinisiData1 = "";
            if (sinonim == true) {
                String synonym = getSinonim(data.getWord1());
                DefinisiData1 = DefinisiData1 + synonym + " ";
            }
            if (arti == true) {
                String arti = getDef(data.getWord1());
                DefinisiData = DefinisiData + arti + " ";
            }
            ArrayList<String> StopToken = getNormKatas(DefinisiData);
            ArrayList<String> StopToken1 = getNormKatas(DefinisiData1);

            for (String DefinisiKata : StopToken) {
                data.addRelatedWordForWord1(DefinisiKata);

            }
            for (String DefinisiKata : StopToken1) {
                data.addRelatedWordForWord11(DefinisiKata);

            }
        }
        System.out.println();
        if (NUM_DATA == 2) {

            String DefinisiData = "";
            String DefinisiData1 = "";
            if (sinonim == true) {
                String synonym = getSinonim(data.getWord2());
                DefinisiData1 = DefinisiData1 + synonym + " ";
            }
            if (arti == true) {
                String arti = getDef(data.getWord2());
                DefinisiData = DefinisiData + arti + " ";
            }
            ArrayList<String> StopToken = getNormKatas(DefinisiData);
            ArrayList<String> StopToken1 = getNormKatas(DefinisiData1);

            for (String DefinisiKata : StopToken) {
                data.addRelatedWordForWord2(DefinisiKata);

            }
            for (String DefinisiKata : StopToken1) {
                data.addRelatedWordForWord22(DefinisiKata);

            }
            System.out.println("\n");
        }
    }

    public void correlation(float Xi, float Yi, float XYi, float XXi, float YYi, int banyak) { // hitung korelasi
        coun = (float) (((banyak * XYi) - (Xi * Yi)) / Math.sqrt(((banyak * XXi) - Math.pow(Xi, 2)) * ((banyak * YYi) - Math.pow(Yi, 2))));
        System.out.println("Correlation = " + coun);
    }

}
