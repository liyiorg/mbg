mbg

```xml

<properties>
	<mybatis.generator.overwrite>true</mybatis.generator.overwrite>
	<mybatis.generator.tableNames></mybatis.generator.tableNames>
</properties>


<dependency>
	<groupId>com.github.liyiorg</groupId>
	<artifactId>mbg-support</artifactId>
	<version>1.0.0-RC6</version>
</dependency>



		<plugin>
			<!-- maven run： mybatis-generator:generate -->
			
			<groupId>org.mybatis.generator</groupId>
			<artifactId>mybatis-generator-maven-plugin</artifactId>
			<version>1.3.5</version>
			<dependencies>
				<!-- 
				<dependency> 
				<groupId>org.postgresql</groupId> 						<artifactId>postgresql</artifactId> 
					<version>9.3-1101-jdbc41</version> 
				</dependency> 
				-->
				<dependency>
					<groupId>mysql</groupId>
					<artifactId>mysql-connector-java</artifactId>
					<version>5.1.32</version>
				</dependency> 
				<dependency>
					<groupId>com.github.liyiorg</groupId>
					<artifactId>mbg-plugin</artifactId>
					<version>1.0.0-RC6</version>
				</dependency>
			</dependencies>
		</plugin>
			
```