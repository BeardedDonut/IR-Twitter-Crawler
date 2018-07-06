package com.gilgamesh.crawler.helpers;

import java.io.File;
import java.io.FileFilter;

/**
 * @author navid
 *         Project-Name: crawler
 *         Date: 7/7/18.
 */
public class TextFilter implements FileFilter {

    @Override
    public boolean accept(File file) {
        return file.getName().toLowerCase().endsWith(".txt");
    }
}
