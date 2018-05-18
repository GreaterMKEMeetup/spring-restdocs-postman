package org.gmjm.springrestdocspostman;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.StandardOperation;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PostmanSnippet implements Snippet {

    private List<String> nonVariableParameters = Arrays.asList();
    private boolean useTemplatedPath = true;


    public static RestDocumentationResultHandler postmanImport(String name) {
        return MockMvcRestDocumentation.document("postman", new PostmanSnippet(name));
    }

    public static RestDocumentationResultHandler postmanImport(String name, String ... nonVariableParameters) {
        return MockMvcRestDocumentation.document("postman", new PostmanSnippet(name, Arrays.asList(nonVariableParameters)));
    }

    public static RestDocumentationResultHandler postmanImport(PostmanSnippet postmanSnippet) {
        return MockMvcRestDocumentation.document("postman", postmanSnippet);
    }

    public interface HasItems {
        public List<FolderItem> getItemList();
    }

    public static class PostmanCollection implements HasItems{
        List<String> variables;
        CollectionInfo info;
        List<FolderItem> item;

        public PostmanCollection() {
        }

        @Override
        public List<FolderItem> getItemList() {
            return item;
        }

        public List<String> getVariables() {
            return variables;
        }

        public void setVariables(List<String> variables) {
            this.variables = variables;
        }

        public CollectionInfo getInfo() {
            return info;
        }

        public void setInfo(CollectionInfo info) {
            this.info = info;
        }

        public List<FolderItem> getItem() {
            return item;
        }

        public void setItem(List<FolderItem> item) {
            this.item = item;
        }
    }

    public static class CollectionInfo {
        String name;
        String description;
        String schema = "https://schema.getpostman.com/json/collection/v2.0.0/collection.json";

        public CollectionInfo() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSchema() {
            return schema;
        }

        public void setSchema(String schema) {
            this.schema = schema;
        }
    }


    public static class FolderItem implements HasItems{
        String name;
        String description;
        List<FolderItem> item;
        Request request;
        Event event;

        @Override
        public List<FolderItem> getItemList() {
            return item;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public List<FolderItem> getItem() {
            return item;
        }

        public Request getRequest() {
            return request;
        }

        public Event getEvent() {
            return event;
        }
    }

    public static class Event {
        String listen;
        Script script;

        public Event() {
        }

        public String getListen() {
            return listen;
        }

        public void setListen(String listen) {
            this.listen = listen;
        }

        public Script getScript() {
            return script;
        }

        public void setScript(Script script) {
            this.script = script;
        }
    }

    public static class Script {
        String type;
        List<String> exec;

        public Script() {
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<String> getExec() {
            return exec;
        }

        public void setExec(List<String> exec) {
            this.exec = exec;
        }
    }

    public static class RequestItem {
        String name;
        String description;
        Request request;
        Response response;

        public RequestItem() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Request getRequest() {
            return request;
        }

        public void setRequest(Request request) {
            this.request = request;
        }

        public Response getResponse() {
            return response;
        }

        public void setResponse(Response response) {
            this.response = response;
        }
    }

    public static class Request {
        String url;
        String method;
        String description;
        List<Header> header;
        Body body;

        public Request() {
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<Header> getHeader() {
            return header;
        }

        public void setHeader(List<Header> header) {
            this.header = header;
        }

        public Body getBody() {
            return body;
        }

        public void setBody(Body body) {
            this.body = body;
        }
    }

    public static class Response {

        public Response() {
        }


    }

    public static class Header {
        String key;
        String value;
        String description;

        public Header() {
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class Body {
        String mode;
        String raw;
        List<KVT> urlEncoded;
        List<KVT> formdata;

        public Body() {
        }

        public Body(
            String mode,
            String raw,
            List<KVT> urlEncoded,
            List<KVT> formdata
        ) {
            this.mode = mode;
            this.raw = raw;
            this.urlEncoded = urlEncoded;
            this.formdata = formdata;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getRaw() {
            return raw;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public List<KVT> getUrlEncoded() {
            return urlEncoded;
        }

        public void setUrlEncoded(List<KVT> urlEncoded) {
            this.urlEncoded = urlEncoded;
        }

        public List<KVT> getFormdata() {
            return formdata;
        }

        public void setFormdata(List<KVT> formdata) {
            this.formdata = formdata;
        }
    }

    public static class KVT {
        String key;
        String value;
        String type;

        public KVT() {
        }

        public KVT(String key, String value, String type) {
            this.key = key;
            this.value = value;
            this.type = type;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static Body urlEncodedBody(List<KVT> urlEncoded) {
        return new Body( "urlencode",null,  urlEncoded, null);

    }

    public static Body formDataBody(List<KVT> formdata) {
        return new Body( "formdata",null,  null, formdata);

    }

    public static Body rawBody(String raw) {
        return new Body( "raw", raw, null, null);
    }

    public static class UrlEncoded {
        String key;
        String value;
        String type;

        public UrlEncoded() {
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    ObjectMapper objectMapper = new ObjectMapper()
        .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    String name;

    public PostmanSnippet(String name) {
        this.name = name;
    }

    public PostmanSnippet(String name, List<String> nonVariableParameters) {
        this.name = name;
        this.nonVariableParameters = new LinkedList<>(nonVariableParameters);
    }

    public PostmanSnippet useTemplatedPath(boolean useTemplatedPath) {
        this.useTemplatedPath = useTemplatedPath;
        return this;
    }

    public PostmanSnippet setNonVariableParameters(String ... nonVariableParameters) {
        this.nonVariableParameters = Arrays.asList(nonVariableParameters);
        return this;
    }

    @Override
    public void document(Operation operation) throws IOException {

        RequestItem requestItem = new RequestItem();
        requestItem.name = name;

        Request request = new Request();
        requestItem.request = request;

        String urlTemplate;

        if(useTemplatedPath) {
            urlTemplate = ((StandardOperation) operation).getAttributes().get("org.springframework.restdocs.urlTemplate").toString();
        } else {
            urlTemplate = operation.getRequest().getUri().toString().replace("http://localhost:8080","");
        }

        String postmanTemplated = urlTemplate;

        if(!operation.getRequest().getParameters().isEmpty()) {
            postmanTemplated += "?";
            boolean first = true;
            for (Map.Entry entry :operation.getRequest().getParameters().toSingleValueMap().entrySet()) {

                if(nonVariableParameters.contains(entry.getKey().toString())) {
                    postmanTemplated += String.format("%s%s=%s", first ? "" : "&", entry.getKey(), entry.getValue());
                } else {
                    postmanTemplated += String.format("%1$s%2$s={%2$s}", first ? "" : "&", entry.getKey());
                }
                first = false;

            }
        }

        postmanTemplated = postmanTemplated.replace("{","{{").replace("}","}}");

        request.url = "{{protocol}}://{{host}}" + postmanTemplated;
        request.method = operation.getRequest().getMethod().toString();
        request.body = rawBody(operation.getRequest().getContentAsString());
        request.header = new LinkedList<>();

        List<String> headersToIgnore = Arrays.asList("Host", "Content-Length");

        for(Map.Entry requestHeader : operation.getRequest().getHeaders().toSingleValueMap().entrySet()) {
            Header header = new Header();
            header.key = requestHeader.getKey().toString();

            String headerName = requestHeader.getKey().toString();

            if(headersToIgnore.contains(headerName)){
                continue;
            } else if (headerName.equalsIgnoreCase("Authorization")) {
                header.value = "Bearer: {{token}}";
            } else if (headerName.equalsIgnoreCase("DM-ORG-CONTEXT")) {
                header.value = "{{orgContext}}";
            } else {
                header.value = requestHeader.getValue().toString();
            }

            request.header.add(header);
        }


        write(operation,objectMapper.writeValueAsString(requestItem));
    }

    private void write(Operation operation, String string) throws IOException {
        RestDocumentationContext context = (RestDocumentationContext) operation
            .getAttributes().get(RestDocumentationContext.class.getName());

        String asciidocPath = context.getOutputDirectory().getPath();
        String collectionName = asciidocPath.substring(asciidocPath.lastIndexOf('/') + 1, asciidocPath.length());

        String snippetPath = String.format("build/postman-snippets/%s", collectionName);

        new File(snippetPath).mkdirs();

        File file = new File(
            String.format("%s/%s.json",
                snippetPath,
                StringUtils.trimWhitespace(operation.getRequest().getMethod().toString() + "-" + name)));

        Files.write(file.toPath(), string.getBytes());
    }
}
