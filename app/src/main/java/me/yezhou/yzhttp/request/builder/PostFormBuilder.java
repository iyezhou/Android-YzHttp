package me.yezhou.yzhttp.request.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.yezhou.yzhttp.request.PostFormRequest;
import me.yezhou.yzhttp.request.RequestCall;

/**
 * Created by yezhou on 2017/3/14.
 */
public class PostFormBuilder extends OkHttpRequestBuilder {

    private List<FileInput> fileInputs = null;

    @Override
    public RequestCall build() {
        return new PostFormRequest(url, tag, params, headers, fileInputs).build();
    }

    public PostFormBuilder addFile(String name, String filename, File file) {
        if (fileInputs == null) {
            fileInputs = new ArrayList<>();
        }
        fileInputs.add(new FileInput(name, filename, file));
        return this;
    }

    public PostFormBuilder addFile(FileInput fileInput) {
        if (fileInputs == null) {
            fileInputs = new ArrayList<>();
        }
        fileInputs.add(fileInput);
        return this;
    }

    public PostFormBuilder files(List<FileInput> fileInputs) {
        this.fileInputs = fileInputs;
        return this;
    }

    public static class FileInput {
        public String key;
        public String filename;
        public File file;

        public FileInput(String name, String filename, File file) {
            this.key = name;
            this.filename = filename;
            this.file = file;
        }

        @Override
        public String toString() {
            return "FileInput{" +
                    "key=" + key +
                    ", filename=" + filename +
                    ", file=" + file +
                    '}';
        }
    }

    @Override
    public PostFormBuilder url(String url) {
        return (PostFormBuilder) super.url(url);
    }

    @Override
    public PostFormBuilder tag(Object tag) {
        return (PostFormBuilder) super.tag(tag);
    }

    @Override
    public PostFormBuilder params(Map<String, String> params) {
        return (PostFormBuilder) super.params(params);
    }

    @Override
    public PostFormBuilder addParam(String key, String val) {
        return (PostFormBuilder) super.addParam(key, val);
    }

    @Override
    public PostFormBuilder headers(Map<String, String> headers) {
        return (PostFormBuilder) super.headers(headers);
    }

    @Override
    public PostFormBuilder addHeader(String key, String val) {
        return (PostFormBuilder) super.addHeader(key, val);
    }
}
