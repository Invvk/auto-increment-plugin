# auto-increment-plugin
[![Java CI with Maven](https://github.com/Invvk/auto-increment-plugin/actions/workflows/maven.yml/badge.svg)](https://github.com/Invvk/auto-increment-plugin/actions/workflows/maven.yml) [![Maven Central](https://img.shields.io/maven-central/v/io.github.invvk/auto-increment-plugin.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.invvk%22%20AND%20a:%22auto-increment-plugin%22)<br>

Maven plugin that automatically increases the version number accordingly, it support commit hash too!

# How it works
This plugin works by generating a properties file saving three values:
 - `Major`: Major version number **X**.0.0
 - `Minor`: Minor version number 0.**X**.0
 - `Patch`: Patch version number 0.0.**X**

everytime a `install` or `package` is initiated, the build number keep increasing.<br>
as soon as the `Patch` hit the limit (`default is 9`) the `Minor` version increases by 1<br>
and the same thing goes for minor, as soon as it hits the limit (`default is 9`), the `Major` version increases by 1

# Using this plugin
Using maven, you can now implement and use this plugin without the need for extra configuration<br>
add this inside `<build>` tag
```xml
<plugin>
  <groupId>io.github.invvk</groupId>
  <artifactId>auto-increment-plugin</artifactId>
  <version>1.0.1</version>
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
 - `-Daip.skip=true`: <br>
 this will generate the file with the current version from the properties file without updating to the next version. this is good for testing.<br>
 **Example command**: `mvn clean package -Dupdate.skip=true`
 - `-Daip.log=true`: <br>
 this will the plugin to not log anything during the build
 - `-Daip.split=true`: <br>
 this will enable split mode, which is going to add 4 more properties: `aip.version.major`, `aip.version.minor`, `aip.version.patch`, `aip.version.commit`
 
 # Example Usage of the plugin
Here is an example explaining everything to do with the plugin.<br>
This example will generate a version that looks like this: `1.0.0`
```xml
<build>
 <plugins>
  <plugin>
    <groupId>io.github.invvk</groupId>
    <artifactId>auto-increment-plugin</artifactId>
    <version>1.0.1</version>
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
       <!-- Include Patch number ? if false, the version would look something like this: 1.0 -->
       <includePatch>true</includePatch>
       <!-- Include git hash? if true, the version would look like this 1.0.0-COMMIT_HASH depending if you disabled the build number or not -->
       <includeHash>false</includeHash>
       <!-- Display the full hash or a short version of it ? Default: SHORT -->
       <hashMode>LONG</hashMode>
       <!-- Should we just print version without updating the properties file ? if true, it will just give you the version without incrementing to the next version-->
       <skipUpdate>false</skipUpdate>
       <!-- Should we add more properties to use other than aip.version.full ? if true it will add 4 more properties -->
       <!-- List: aip.version.major, aip.version.minor, aip.version.patch, aip.version.commit -->
       <splitMode>true</splitMode>
       <!-- Should we enable logging ? -->
       <log>false</log>
       <!-- What number should minor go over to increase major ? default 9 which means if minor becomes bigger than 9 it will increase major -->
       <minorLimit>9</minorLimit>
       <!-- What number should patch go over to increase minor ? default 9 which means if patch becomes bigger than 9 it will increase minor -->
       <patchLimit>9</patchLimit>
      </configuration>
    </execution>
   </executions>
  </plugin>
 </plugins>
</build>
```
now after that you can use `${aip.version.full}` almost everywhere.<br>
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
     <outputFile>test-${aip.version.full}.jar</outputFile>
   </configuration>
  </execution>
 </executions>
</plugin>
```
the final jar file going to look like this: `test-1.0.0.jar`

# Common issues
 - **Problem: Sometimes maven might not recognize the property `${aip.version.full}` and highlight it as an error.**
   - **Solution:** don't worry, in the building process maven is going to recognize this property. you can suppress the error by adding this line on top of the property like in shown picture.
<br>![image](https://i.ibb.co/0n56gWC/Capture.png) <br>
 copy and paste: `<!--suppress UnresolvedMavenProperty -->`
 
 - **Problem: Commit has isn't appearing in split mode (`aip.version.commit`) or in `aip.version.full`**
   - **Solution:** Please make sure you provided the correct directory in `<gitDirectory>` in configuration. it should be your project directory (TIP: where your pom.xml is located) then copy it and add at the end `/.git`.<br> if the problem still presists please open an [issue](https://github.com/Invvk/auto-increment-plugin/issues).
 # Licenses
 This project uses [GNU General Public License v3.0](https://github.com/Invvk/auto-increment-plugin/blob/main/LICENSE).
 full artical on this license can be found [**here**](https://www.gnu.org/licenses/gpl-3.0.html).
