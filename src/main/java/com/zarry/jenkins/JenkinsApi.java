package com.zarry.jenkins;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author: lzarou
 * Date: 7/18/13
 * Time: 7:49 PM
 */
abstract class JenkinsApi implements IApi {
    private Document dom;
    private URL url;

    public Document getDom(URL url){
        try {
            dom = new SAXReader().read(url);
        } catch (DocumentException e) {
            return null;
        }
        return dom;
    }

    public URL createUrl (String urlString){
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
