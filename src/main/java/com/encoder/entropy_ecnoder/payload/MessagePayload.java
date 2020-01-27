package com.encoder.entropy_ecnoder.payload;

import java.util.HashMap;

public class MessagePayload {

    private String title;
    private HashMap<String,Object> content;

    public MessagePayload(){
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HashMap<String, Object> getContent() {
        return content;
    }

    public void setContent(HashMap<String, Object> content) {
        this.content = content;
    }
}
