# spring-restdocs-postman
A Spring Restdoc Snippet implementation that produces importable Postman collections.

## Usage
### Maven
```xml
<dependency>
    <groupId>com.github.greatermkemeetup</groupId>
    <artifactId>spring-restdocs-postman</artifactId>
    <version>0.0.1</version>
</dependency>
```
### Gradle
```groovy
compile 'com.github.greatermkemeetup:spring-restdocs-postman:0.0.1'
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
			.headers(validClientAdmin)
			.accept(WebConfig.Constants.PRODUCES_HAL))
		.andExpect(status().isOk())
		.andDo(postmanImport("By Username and Company"))
		.andReturn();
}
```

This are generate the following JSON at ```$buildDir/postman-snippets/GET-By Username and Company.json```:

```json
{
	"name": "By Username and Company",
	"description": null,
	"item": null,
	"request": {
		"url": "{{protocol}}://{{host}}/users?username={{username}}&company={{company}}",
		"method": "GET",
		"description": null,
		"header": [
			{
				"key": "Authorization",
				"value": "Bearer: {{token}}",
				"description": null
			},
			{
				"key": "Accept",
				"value": "application/hal+json",
				"description": null
			}
		],
		"body": {
			"mode": "raw",
			"raw": "",
			"urlEncoded": null,
			"formdata": null
		}
	},
	"response": null
}
```
 
 This like the following in Postman:
![Example Request](/images/example-request.png?raw=true "Example Request")


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

![Example Collection](/images/example-collection.png?raw=true "Example Collection")