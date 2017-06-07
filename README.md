MyBatis Generator Tool
============================
XML Configuration
generatorConfiguration &gt; context

```xml
<!-- mybatis-generator-pagination plugins -->
<plugin type="com.github.liyiorg.mbg.plugin.MySQLPaginationPlugin"></plugin>
<plugin type="com.github.liyiorg.mbg.plugin.PostgreSQLPaginationPlugin"></plugin>
```

pom.xml 
```xml
<dependency>
	<groupId>com.github.liyiorg</groupId>
	<artifactId>mbg</artifactId>
	<version>1.0.0-SNAPSHOT</version>
	<exclusions>
		<exclusion>
			<groupId>org.mybatis.generator</groupId>
			<artifactId>mybatis-generator-core</artifactId>
		</exclusion>
	</exclusions>
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
						<artifactId>mbg</artifactId>
						<version>1.0.0-SNAPSHOT</version>
					</dependency>
				</dependencies>
			</plugin>
			***
		</plugins>
		***
</build>
```
