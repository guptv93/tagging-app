package edu.nyu.hml.tagging.controller;

import java.util.List;
import lombok.Getter;

@Getter
public class ImageTagDocument {

    private String fileName;
    private String author;
    private List<String> tags;

}
