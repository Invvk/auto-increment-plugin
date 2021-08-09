# auto-increment-plugin
Maven plugin that automatically increases the version number accordingly, it support commit hash too!

# How it works
This plugin works by generating a properties file saving three values:
 - `Major`: Major version number **X**.0.0
 - `Minor`: Minor version number 0.**X**.0
 - `BuildNumber`: Build version number 0.0.**X**

everytime a `install` or `package` is initiated, the build number keep increasing.<br>
as soon as the `BuildNumber` hit the limit (`default is 9`) the `Minor` version increases by 1<br>
and the same thing goes for minor, as soon as it hits the limit (`default is 9`), the `Major` version increases by 1

# Using this plugin
Using maven, you can now implement and use this plugin without the need for extra configuration<br>
add this inside `<build>` tag
```xml
  <plugin>
   <groupId>io.github.invvk</groupId>
   <artifactId>auto-increment-plugin</artifactId>
   <version>1.0.0</version>
  </plugin>
```
# Plugin compatibility with Java
Here is an overview of the current plugin compatibility with Java

| Plugin Version  | Required Java Version |
| --------------- | ---------------------:|
| 1.0.X           | Java 1.8              |

# command argument
there is no commands required a simple `mvn clean package` or `mvn clean install` will do the job, but there are an optional arguments.
 ## Optional argument
 - `-Dupdate.skip=true`: <br>
 this will generate the file with the current version from the properties file without updating to the next version. this is good for testing.<br>
 **Example command**: `mvn clean package -Dupdate.skip=true`
 
 # Example Usage of the plugin
Here is an example explaining everything to do with the plugin.<br>
This example will generate a version that looks like this: `1.0.0`
```xml
<build>
 <plugins>
  <plugin>
   <groupId>io.github.invvk</groupId>
   <artifactId>auto-increment-plugin</artifactId>
   <version>1.0.0</version>
   <executions>
    <execution>
     <goals>
      <!-- telling maven to use the modifying goal -->
      <goal>modifyVersion</goal>
     </goals>
     <configuration>
      <!-- properties file name -->
      <storageName>buildData</storageName>
      <!-- Directory to git which is PROJECT_DIRECTORY/.git -->
      <gitDirectory>${gitDir}</gitDirectory>
      <!-- Include build number ? if false, the version would look something like this: 1.0 -->
      <includeBuildNumber>true</includeBuildNumber>
      <!-- Include git hash? if true, the version would look like this 1.0.0-COMMIT_HASH depending if you disabled the build number or not -->
      <includeHash>false</includeHash>
      <!-- Display the full hash or a short version of it ? -->
      <hashMode>SHORT</hashMode>
     </configuration>
    </execution>
   </executions>
  </plugin>
 </plugins>
</build>
```
now after that you can use `${autoincrement.version}` almost everywhere.<br>
As an example, here I'm using it in `maven-shade-plugin` to change the file name after shading
```xml
<plugin>
 <groupId>org.apache.maven.plugins</groupId>
 <artifactId>maven-shade-plugin</artifactId>
 <executions>
  <execution>
   <phase>package</phase>
   <goals>
    <goal>shade</goal>
   </goals>
   <configuration>
    <outputFile>
     test-${autoincrement.version}.jar
    </outputFile>
   </configuration>
  </execution>
 </executions>
</plugin>
```
the final jar file going to look like this: `test-1.0.0.jar`

# Common issues
 - Sometimes maven might not recognize the property `${autoincrement.version}` and highlight it as an error.<br> but don't worry, in the building process maven is going to recognize this property. you can suppress the error by adding this line on top of the property like in shown picture.
<br>![image](https://i.ibb.co/0n56gWC/Capture.png) <br>
 copy and paste: `<!--suppress UnresolvedMavenProperty -->` 
