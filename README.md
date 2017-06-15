MyBatis Generator Tool
============================
XML Configuration
generatorConfiguration &gt; context

```xml
<!-- mybatis-generator-pagination plugins -->
<plugin type="com.github.liyiorg.mbg.plugin.pagination.MySQLPaginationPlugin"></plugin>
<plugin type="com.github.liyiorg.mbg.plugin.pagination.OraclePaginationPlugin"></plugin>
<plugin type="com.github.liyiorg.mbg.plugin.pagination.PostgreSQLPaginationPlugin"></plugin>

<!-- mybatis-generator-service plugins -->
<plugin type="com.github.liyiorg.mbg.plugin.ServiceGeneratorPlugin"></plugin>
```

pom.xml 
```xml
<dependency>
	<groupId>com.github.liyiorg</groupId>
	<artifactId>mbg-suport</artifactId>
	<version>1.0.0-SNAPSHOT</version>
</dependency>


<build>
		***
		<plugins>
			***
			<plugin>
				<!-- maven run： mybatis-generator:generate -->
				<groupId>org.mybatis.generator</groupId>
				<artifactId>mybatis-generator-maven-plugin</artifactId>
				<version>1.3.5</version>
				<dependencies>
					<!-- <dependency> <groupId>org.postgresql</groupId> <artifactId>postgresql</artifactId> 
						<version>9.3-1101-jdbc41</version> </dependency> -->
					<dependency>
						<groupId>mysql</groupId>
						<artifactId>mysql-connector-java</artifactId>
						<version>5.1.32</version>
					</dependency>
					<dependency>
						<groupId>com.github.liyiorg</groupId>
						<artifactId>mbg-plugin</artifactId>
						<version>1.0.0-SNAPSHOT</version>
					</dependency>
				</dependencies>
			</plugin>
			***
		</plugins>
		***
</build>
```
