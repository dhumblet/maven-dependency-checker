WIP

Maven-Dependency-Checker
=

Maven-Dependency-Checker is a straightforward maven plugin to check for dependencies that can be updated.
The results are summarized in a generated HTML file. 

Install
=
```xml
<project>
    ...
    <build>
        <plugins>
            ...
            <plugin>
                <groupId>com.dh</groupId>
                <artifactId>dependency-checker</artifactId>
                <version>0.0.1-SNAPSHOT</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>dependency-checker</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <scope>compile</scope>
                </configuration>
            </plugin>
            ...
        </plugins>
        ...
    </build>
    ...
</project>
```
Execute
= 
```
mvn clean compile
```


TODO
=

Run locally
mvn com.dh:dependency-checker:0.0.1-SNAPSHOT:dependency-checker
