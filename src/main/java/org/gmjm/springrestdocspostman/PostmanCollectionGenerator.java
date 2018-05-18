package org.gmjm.springrestdocspostman;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import static java.nio.file.Files.*;
import static org.gmjm.springrestdocspostman.PostmanSnippet.*;

public class PostmanCollectionGenerator {

    private static ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false )
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );

    public static void main(String[] args) throws IOException {
        System.out.println("Running PostmanCollectionGenerator: " + Arrays.toString(args));

        String base = args.length > 0 ? args[0] : "";

        String version = args.length > 1 ? args[1] : "unknown";

        PostmanCollection postmanCollection = new PostmanCollection();
        postmanCollection.info = new CollectionInfo();
        postmanCollection.info.name = "App - " + version;
        postmanCollection.info.description = "Auto generated postman collection for App. Version: " + version;
        postmanCollection.item = new LinkedList<>();

        walk(new File(base), postmanCollection);

        Collections.sort(postmanCollection.item,   Comparator.comparing(folderItem -> folderItem.name ));

        String collectionJson = objectMapper.writeValueAsString(postmanCollection);

        System.out.println(collectionJson);

        Files.createDirectories(Paths.get("build/postman"));
        Files.write(Paths.get(String.format("build/postman/postman-collection-%s.json", version)), collectionJson.getBytes("UTF-8"));

    }

    private static void walk(File file, HasItems hasItems) {
        Arrays.stream(file.listFiles())
            .forEach ( f -> {
                if (f.isDirectory()) {
                    FolderItem newFolderItem = new FolderItem();
                    newFolderItem.name = f.getName();
                    newFolderItem.item = new LinkedList<>();
                    hasItems.getItemList().add(newFolderItem);
                    walk(f, newFolderItem);
                } else {
                    try {
                        String folderItemString = new String(readAllBytes(f.toPath()), StandardCharsets.UTF_8);
                        FolderItem folderItem = objectMapper.readValue(folderItemString, FolderItem.class);
                        hasItems.getItemList().add(folderItem);
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
    }
}

