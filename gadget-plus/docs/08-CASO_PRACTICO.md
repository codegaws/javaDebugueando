
## ⃣📚**Clase 112 al 114:COMO TRABAJAR CON 2 BASES DE DATOS CON JPA⏭️⏭️ ****

- En properties
![images](images/img_20.png)

- En el POMXML

```xml
 <profiles>
        <profile>
            <id>blue</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <dependencies>
                <dependency>
                    <groupId>org.postgresql</groupId>
                    <artifactId>postgresql</artifactId>
                    <scope>runtime</scope>
                </dependency>
            </dependencies>
        </profile>

        <profile>
            <id>red</id>
            <activation>
                <activeByDefault>false</activeByDefault>
            </activation>
            <dependencies>
                <dependency>
                    <groupId>org.mariadb.jdbc</groupId>
                    <artifactId>mariadb-java-client</artifactId>
                    <version>3.5.6</version>
                </dependency>
            </dependencies>
        </profile>
    </profiles>
```

- En el IDE
  ![images](images/img_21.png)

- en la terminal
  ![images](images/img_22.png)
