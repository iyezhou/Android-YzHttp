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
public class PostFormBuilder extends OkHttpRequestBuilder<PostFormBuilder> implements HasParamsable {

    private List<FileInput> fileInputs = null;

    @Override
    public RequestCall build() {
        return new PostFormRequest(url, tag, params, headers, fileInputs).build();
    }

    public PostFormBuilder addFile(String key, String filename, File file) {
        if (fileInputs == null) {
            fileInputs = new ArrayList<>();
        }
        fileInputs.add(new FileInput(key, filename, file));
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

    public PostFormBuilder files(String key, Map<String, File> files) {
        if (files != null && files.size() > 0) {
            if (fileInputs == null) {
                fileInputs = new ArrayList<>();
            }
            for (String filename : files.keySet()) {
                fileInputs.add(new FileInput(key, filename, files.get(filename)));
            }
        }
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
    public PostFormBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public PostFormBuilder addParam(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }
}
