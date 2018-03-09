package com.kco.myapplication;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by 666666 on 2018/3/9.
 */
public final class CrawlerUtils {

    private static final String baseUrl = "https://www.aitaotu.com/taotu/";

    public static Observable<String> findIndex(){
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Document parse = Jsoup.connect(baseUrl).execute().parse();
                Elements as = parse.select(".taotu-main a > img");
                for (Element a : as){
                    emitter.onNext(a.parent().attr("abs:href"));
                }
                emitter.onComplete();
            }
        });
    }

    public static Observable<String> getImageUrl(final String url){
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                paserImage(url, emitter);
            }
        });
    }

    private static void paserImage(String url, ObservableEmitter<String> emitter) throws Exception{
        Document parse = Jsoup.connect(url).execute().parse();
        Elements imgs = parse.select("#big-pic a img");
        for (Element img : imgs){
            emitter.onNext(img.attr("abs:src"));
        }
        Element nextPage = parse.select("#nl a").first();
        if (nextPage == null){
            emitter.onComplete();
        }else{
            paserImage(nextPage.attr("abs:href"), emitter);
        }
    }
}
