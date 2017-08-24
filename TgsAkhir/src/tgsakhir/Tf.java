/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tgsakhir;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;


/**
 *
 * @author Rhesa
 */
public class Tf {

    String stopword = "src/stopwords.txt";
    String stopwords = "src/stop.txt";
    boolean tfidf = false, sinonim = false, arti = false, stop = false;
    float d, X, Y, XX, YY, XY, coun;
    final int ALL_DATA = -1;
    final int NUM_DATA_1 = 1;
    final int NUM_DATA_2 = 2;
    int hitung, rank1, rank2;
    private Idf IDF = null;
    Parser ps = new Parser();

    public Tf(Idf idf, boolean sinonim, boolean arti, boolean tfidf, boolean stop) throws ParserConfigurationException, IOException, MalformedURLException, SAXException, InterruptedException{
        this.IDF = idf;
        this.tfidf = tfidf;
        this.sinonim = sinonim;
        this.arti = arti;
        this.stop = stop;

        if (stop == true){
        ps.stopWord(ps.hitungKata(stopword));
        } else if (stop == false) {
        ps.stopWords(ps.hitungKata(stopwords));
                };

        ps.load(ALL_DATA);
        
        for (Format data : ps.dataset) {
            setArrayTf(data, NUM_DATA_1);
            setArrayTf(data, NUM_DATA_2);
            data.HitungNilaiVektor(IDF, tfidf, sinonim, arti, stop);

            hitung++;
            X = X + data.getGoldStandard();
            Y = Y + data.getVectorScore();
            XY = XY + (data.getGoldStandard() * data.getVectorScore());
            XX = XX + (float) Math.pow(data.getGoldStandard(), 2);
            YY = YY + (float) Math.pow(data.getVectorScore(), 2);
            d = (float) (d + (Math.pow(rank1 - rank2, 2)));

            data.show();
        }
        correlation(X, Y, XY, XX, YY, hitung); //nilai korelasi
    }


    public ArrayList<Format> getDataset() {
        return ps.dataset;
    }

    public void setArrayTf(Format data, int NUM_DATA) throws ParserConfigurationException, IOException, MalformedURLException, SAXException, InterruptedException { // array untuk token
        if (NUM_DATA == 1) {
            String DefinisiData = "";
            if (sinonim == true) {
                String synonym = ps.getSinonim(data.getWord1());
                DefinisiData = DefinisiData + synonym + " ";
            }
            if (arti == true) {
                String arti = ps.getDefinisi(data.getWord1());
                DefinisiData = DefinisiData + arti + " ";
            }
            ArrayList<String> DK = ps.kataNorm(DefinisiData);

            for (String DefinisiKata : DK) {
                data.simpanDaftarKata1(DefinisiKata);

            }

        }
        System.out.println();
        if (NUM_DATA == 2) {

            String DefinisiData = "";
            String DefinisiData1 = "";
            if (sinonim == true) {
                String synonym = ps.getSinonim(data.getWord2());
                DefinisiData = DefinisiData + synonym + " ";
            }
            if (arti == true) {
                String arti = ps.getDefinisi(data.getWord2());
                DefinisiData = DefinisiData + arti + " ";
            }
            ArrayList<String> DK = ps.kataNorm(DefinisiData);

            for (String DefinisiKata : DK) {
                data.simpanDaftarKata2(DefinisiKata);

            }

            System.out.println("\n");
        }
    }

    public void correlation(float Xi, float Yi, float XYi, float XXi, float YYi, int banyak) { // hitung korelasi
        coun = (float) (((banyak * XYi) - (Xi * Yi)) / Math.sqrt(((banyak * XXi) - Math.pow(Xi, 2)) * ((banyak * YYi) - Math.pow(Yi, 2))));
        System.out.println("Correlation = " + coun);
    }

}
