package project.extractor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import project.ConfigConstants;

public abstract class Extractor<T> {

    protected BufferedReader reader;
    
    public Extractor(BufferedReader reader) {
        this.reader = reader;
    }
    
    public abstract T createExtractedItem();
    
    /**
     * Uses the buffered reader to process the file.
     * Can only the process file once
     */
    public abstract void readLines();
    
}
