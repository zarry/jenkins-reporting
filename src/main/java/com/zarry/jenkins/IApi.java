package com.zarry.jenkins;

import org.dom4j.Document;
import java.net.URL;

/**
 * Author: lzarou
 * Date: 8/3/13
 * Time: 2:53 PM
 */
public interface IApi {
    Document dom = null;
    URL url = null;

    public Document getDom(URL url);

    public URL createUrl (String urlString);


}
