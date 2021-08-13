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
  <version>1.1.0</version>
  <executions>
      <execution>
          <goals>
              <goal>modifyVersion</goal>
          </goals>
      </execution>
  </executions>
</plugin>
```
# Plugin compatibility with Java
Here is an overview of the current plugin compatibility with Java

| Plugin Version  | Required Java Version |
| --------------- | ---------------------:|
| 1.0.X           | Java 1.8              |
| 1.1.0           | Java 1.8              |

# Common issues
 - **Problem: Sometimes maven might not recognize the property `${aip.version.full}` and highlight it as an error.**
   - **Solution:** don't worry, in the building process maven is going to recognize this property. you can suppress the error by adding this line on top of the property like in shown picture.
<br>![image](https://i.ibb.co/vVB6vXG/Capture.png) <br>
 copy and paste: `<!--suppress UnresolvedMavenProperty -->`
 
 - **Problem: Commit has isn't appearing in split mode (`aip.git.commit`) or in `aip.version.full`**
   - **Solution:** Make sure that your project is linked up with a git repository and it contains `.git` directory. Also, Make sure that you don't have `disableGit` option enabled.
 # Licenses
 This project uses [GNU General Public License v3.0](https://github.com/Invvk/auto-increment-plugin/blob/main/LICENSE).
 full artical on this license can be found [**here**](https://www.gnu.org/licenses/gpl-3.0.html).
