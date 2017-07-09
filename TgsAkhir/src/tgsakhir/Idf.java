/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tgsakhir;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
public class Idf {

    String inFile = "src/dataset.txt";
    String stopword = "src/stopwords.txt";
    boolean sinonim = false, arti = false, tfidf = false;
    ;
    final int ALL_DATA = -1;
    final int NUM_DATA_1 = 1;
    final int NUM_DATA_2 = 2;
    ArrayList<Format> dataset = new ArrayList<>();
    String sentence1, sentence2;
    ArrayList<String> feature1 = new ArrayList();
    ArrayList<String> feature2 = new ArrayList();
    Map<String, Float> jumlah = new HashMap<>();
    int nilai;
    String[] sw;

    public Idf(boolean sinonim, boolean arti, boolean tfidf) throws ParserConfigurationException, IOException, MalformedURLException, SAXException, InterruptedException{
        Stopw(countWord(stopword));
        this.sinonim = sinonim;
        this.arti = arti;
        loadData(ALL_DATA);
        // TESTING
        Properties props;
        props = new Properties();

        for (Format data : dataset) {
            setWordsFromDictToArray(data, NUM_DATA_1, tfidf);
            setWordsFromDictToArray(data, NUM_DATA_2, tfidf);

        }
        hitung(jumlah, nilai);
        akhir(jumlah);
        DecimalFormat df = new DecimalFormat("#.##");
        if (tfidf == true) {
            if (sinonim == false){
                try
                {
                   BufferedWriter bw = new BufferedWriter(new FileWriter("src/idf.txt"));
                   for(String p:jumlah.keySet())
                   {
                      bw.write(p + "," + jumlah.get(p));
                      bw.newLine();
                   }
                   bw.flush();
                   bw.close();
                }catch (IOException e){}
            } else if (sinonim == true){
                try
                {
                   BufferedWriter bw = new BufferedWriter(new FileWriter("src/idfsinonim.txt"));
                   for(String p:jumlah.keySet())
                   {
                      bw.write(p + "," + jumlah.get(p));
                      bw.newLine();
                   }
                   bw.flush();
                   bw.close();
                }catch (IOException e){}                
            }
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

    public int countWord(String p) {
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

    public Map<String, Float> getHashmap() {
        return jumlah;
    }

    public void setHashmap(Map<String, Float> hashmap) {
        this.jumlah = hashmap;
    }

    public ArrayList getNormWords(String sentence) {
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

    public String makeNormal(String oriWord) {
        String result = "";
        String x = "";
        oriWord = oriWord.replaceAll("[^a-z0-9]", "");
        oriWord = tokenizer(oriWord).toLowerCase();
//        System.out.println(oriWord);
        if (!oriWord.equals(null)) {
            for (int i = 0; i < sw.length; i++) {
//            System.out.println(sw[i]);
                if (oriWord.equals(sw[i])) {
                    oriWord = "";
                }
            }
            result = oriWord;
        }
        result = Stemm(result);
        System.out.print(result);
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
    
    public String tokenizer(String oriWord) {
        char delimiter = ' ';

        // calculate number of delimiter characters
        int N = 0;
        ArrayList<String> tokens = new ArrayList();
        String token = "";
        for (int i = 0; i < oriWord.length(); i++) {
            if (oriWord.charAt(i) == delimiter) {
                if (token.equals("")) {
                    token = "";
                } else {

                    tokens.add(token);
                    token = "";
                    N++;
                }
            } else {
                if (Character.isAlphabetic(oriWord.charAt(i))) {
                    token = token + oriWord.charAt(i);
                }
            }
        }
        tokens.add(token);
        N++;

        String normWord = "";
        for (int i = 0; i < N; i++) {
            normWord = tokens.get(i);

//            System.out.println(i + ": " + word);
        }
        return normWord;
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
            Thread.sleep(1100);
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
        
    public void setWordsFromDictToArray(Format data, int NUM_DATA,boolean tfidf) throws ParserConfigurationException, IOException, MalformedURLException, SAXException, InterruptedException{ // array untuk token idf
        if (NUM_DATA == 1) {
            nilai++;

            String DefinisiData = "";
            if (sinonim == true) {
                String synonym = getSinonim(data.getWord1());
                DefinisiData = DefinisiData + synonym + " ";
            }
            if (arti == true) {
                String arti = getDef(data.getWord1());
                DefinisiData = DefinisiData + arti + " ";
            }

            ArrayList<String> StopToken = getNormWords(DefinisiData);

            for (int i = 0; i < StopToken.size(); i++) {
                if (!feature1.contains(StopToken.get(i))) {
                    feature1.add(StopToken.get(i));

                    if (i == 0) {
                        sentence1 = StopToken.get(i);
                    } else {
                        sentence1 = sentence1 + " " + StopToken.get(i);
                    }                    
                }

            }
 
            for (int i = 0; i < feature1.size(); i++) {
                if (jumlah.containsKey(feature1.get(i))) {
                    jumlah.put(feature1.get(i), jumlah.get(feature1.get(i)) + 1);
                } else {
                    jumlah.put(feature1.get(i), (float) 1);
                }

            }
            feature1.clear();
            if (tfidf == true) {
                System.out.println(jumlah);
            }
        }
 
        if (NUM_DATA == 2) {
            nilai++;

            String DefinisiData = "";

            if (sinonim == true) {
                String synonym = getSinonim(data.getWord2());
                DefinisiData = DefinisiData + synonym + " ";
            }
            if (arti == true) {
                String arti = getDef(data.getWord2());
                DefinisiData = DefinisiData + arti + " ";
            }
            
            ArrayList<String> StopToken = getNormWords(DefinisiData);

            for (int i = 0; i < StopToken.size(); i++) {
                if (!feature2.contains(StopToken.get(i))) {
                    feature2.add(StopToken.get(i));
                    if (i == 0) {
                        sentence2 = StopToken.get(i);
                    } else {
                        sentence2 = sentence2 + " " + StopToken.get(i);
                    }
                }
            }

            //System.out.println(sentence2);
            for (int i = 0; i < feature2.size(); i++) {
                if (jumlah.containsKey(feature2.get(i))) {
                    jumlah.put(feature2.get(i), jumlah.get(feature2.get(i)) + 1);
                } else {
                    jumlah.put(feature2.get(i), (float) 1);
                }

            }

            feature2.clear();
            if (tfidf == true) {
               System.out.println(jumlah);
            }
        }
    }


    public void hitung(Map<String, Float> coun, int nilai) {
        for (Map.Entry<String, Float> cek : coun.entrySet()) {
            String d = cek.getKey();
            float s = cek.getValue();
            float idf = (float) Math.log10(nilai / s);
            coun.put(d, idf);
        }

    }

    public void akhir(Map<String, Float> cecking) {
    }
}
