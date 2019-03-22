mvn clean package
cp ./target/sonar-dependency-plugin-1.0.jar ~/SonarQube/sonarqube-7.6/extensions/plugins/
~/SonarQube/sonarqube-7.6/bin/linux-x86-64/sonar.sh restart