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
 currently, this plugin doesn't have a maven repository that it can work off. Your only option here is to clone into this project and execute `mvn clean install`.

# command argument
there is no commands required a simple `mvn clean package` or `mvn clean install` will do the job, but there are an optional arguments.
 ## Optional argument
 - `-Dupdate.skip=true`: <br>
 this will generate the file with the current version from the properties file without updating to the next version. this is good for testing.<br>
 **Example command**: `mvn clean package -Dupdate.skip=true`
 
 # Example Maven configuration
 this is suppose to be in the `<build>` tag
```xml
<build>
 .....
 </plugin>
  <groupId>me.invvk</groupId>
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
 .....
<build>
```
