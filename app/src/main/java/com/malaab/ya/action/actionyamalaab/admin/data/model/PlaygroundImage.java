package com.malaab.ya.action.actionyamalaab.admin.data.model;

import java.util.Objects;

public class PlaygroundImage {

    public String localPath;
    public String onlinePath;

    public boolean isUploaded;


    public PlaygroundImage(String localPath, String onlinePath, boolean isUploaded) {
        this.localPath = localPath;
        this.onlinePath = onlinePath;
        this.isUploaded = isUploaded;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaygroundImage image = (PlaygroundImage) o;
        return Objects.equals(localPath, image.localPath);
    }

    @Override
    public int hashCode() {

        return Objects.hash(localPath);
    }
}
