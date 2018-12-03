# spring-restdocs-postman
A Spring Restdoc Snippet implementation that produces importable Postman collections.

## Usage
### Maven
```xml
<dependency>
    <groupId>com.github.greatermkemeetup</groupId>
    <artifactId>spring-restdocs-postman</artifactId>
    <version>0.0.2</version>
</dependency>
```
### Gradle
```groovy
compile 'com.github.greatermkemeetup:spring-restdocs-postman:0.0.2'
```

## Overview
Use the PostmanSnippet class to generate Postman collection items.  You can customize:
* The name of the item
* The folder the items appear in
* Path variables
* Query parameter variables
* Headers
* Example request body



## Examples

```java

import static org.gmjm.springrestdocspostman.PostmanSnippet.*;

...

@Test
public void getUsersByUsernameAndCompany() {
    
    ...
    
	HttpHeaders headers = new HttpHeaders();
	headers.put("Authorization", Arrays.asList("Bearer: token"));

	mockMvc
		.perform(
		    get("/users")
			.param("username","gobBluth")
			.param("company","Bluth Company")
			.headers(headers)
			.accept(WebConfig.Constants.PRODUCES_HAL))
		.andExpect(status().isOk())
		.andDo(postmanImport("By Username and Company"))
		.andReturn();
}
```

The PostmanSnippet class generates and saves a JSON file at ```$buildDir/postman-snippets/users/GET-By Username and Company.json```:

How the request appears in Postman:
![Example Request](/images/example-request.png?raw=true "Example Request")

The PostmanSnippet class defaults to using [Postman Variables](https://www.getpostman.com/docs/v6/postman/environments_and_globals/variables) to keep the output as flexible, and reusable as possible.

## Customizing Output
### Non Variable Parameters
In some cases, you may not want a path, or query parameter to be a variable in the postman request.
To exclude particular parameters from becoming variables, use the following syntax:

```java
	...
	.andDo(postmanImport("By Username and Company", "company", "username"))
```

![Example Request - Non Variable Parameters](/images/example-request-nonVariableParameters.png?raw=true "Example Request - Non Variable Parameters")

### Non Templated Path
By default, the generator assumes that PathVariables should be Postman variables.  For example

```/users/{userId}/address``` is transformed to ```/users/{{userId}}/address```

If you'd like to skip using variables in the request URL, and use what was used as the URL in the test,
you can call _postmanImport_ as follows:

```java
@Test
public void getUsersByUsernameAndCompany() {
    
    ...
    
	HttpHeaders headers = new HttpHeaders();
	headers.put("Authorization", Arrays.asList("Bearer: token"));

	mockMvc
		.perform(
		    get("/users/gobBluth/address")
		    .param("projection","slim")
			.headers(headers)
			.accept(WebConfig.Constants.PRODUCES_HAL))
		.andExpect(status().isOk())
		.andDo(
			postmanImport(
				new PostmanSnippet("Slim Address By Username")
					.setNonVariableParameters("projection")
					.useTemplatedPath(false))
		.andReturn();
}
```

Results in:

![Example Request - Use Tempalted Path = false](/images/example-request-useTemplatedPath-false.png?raw=true "Example Request - Use Tempalted Path = false")

Rather than:

![Example Request - Use Tempalted Path = true](/images/example-request-useTemplatedPath-true.png?raw=true "Example Request - Use Tempalted Path = true")

## Output

Snippets are staged in the path ```build/postman-snippets/${folder}``` where _folder_ is the last directory
of the path configured for the default rest documentation output.

For example, a JUnitRestDocumentation configured for ```build/generated-snippets/users``` would result in postman
snippets saving to ```build/postman-snippets/users```.
```java
@Rule
public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/generated-snippets/users");
```

Finally, you must configure a gradle task to assemble the snippets into a single collection.

```gradle
task generatePostmanCollection(type: JavaExec) {
	dependsOn 'asciidoctor'
	main = 'org.gmjm.springrestdocspostman.PostmanCollectionGenerator'
	classpath = sourceSets.test.runtimeClasspath
	setArgs([ "$buildDir/postman-snippets".toString(), version.toString() ])
}
```

The task traverses the ```postman-snippets``` directory, and assemble all the snippets in each sub-directory
into a sub-collection.

Finally, the collection export is saved to ```$buildDir/postman/postman-collection-${version}.json```.

[Import this collection into your Postman app](https://www.getpostman.com/docs/v6/postman/collections/data_formats).

The final result:

![Example Collection](/images/example-collection-1.png?raw=true "Example Collection")