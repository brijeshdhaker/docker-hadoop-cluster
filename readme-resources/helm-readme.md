-Dspring.profiles.active=cloud

helm create bd-spring-module/helm --namespace AA100121

helm template bd-spring-module ./bd-spring-module/helm \
--namespace AA100121 \
--dry-run \
--debug \
--set author=brijeshdhaker@gmail.com

helm template bd-spring-module ./bd-spring-module/helm/archives/repo/bd-spring-module-1.0.0.tgz \
--namespace AA100121 \
--dry-run \
--debug \
--set author=brijeshdhaker@gmail.com
--output-dir ./bd-spring-module/helm/k8s/

#
helm install bd-spring-module ./bd-spring-module/helm \
--namespace AA100121 \
--dry-run \
--debug \
--set favoriteDrink=slurm \
--version 1.0.0

#
helm upgrade bd-spring-module ./bd-spring-module/helm \
--namespace AA100121 \
--dry-run \
--debug \
--set favoriteDrink=slurm
--output-dir ./bd-spring-module/helm/k8s/

#
helm repo add bd-spring-module https://nexus.repo.com --namespace AA100121
helm repo update bd-spring-module https://nexus.repo.com

#
#
#
docker run --rm -it \
  -p 8080:8080 \
  -e DEBUG=1 \
  -e STORAGE=local \
  -e STORAGE_LOCAL_ROOTDIR=/charts \
  -v $(pwd)/charts:/charts \
  ghcr.io/helm/chartmuseum:v0.14.0
  
#
#
#
<plugin>
    <groupId>com.kiwigrid</groupId>
    <artifactId>helm-maven-plugin</artifactId>
    <configuration>
        <chartDirectory>${project.basedir}</chartDirectory>
        <chartVersion>${helm.chart.version}</chartVersion>
        <useLocalHelmBinary>true</useLocalHelmBinary>
        <uploadRepoStable>
            <name>stable-repo</name>
            <url>${helm.repo.url}</url>
            <type>CHARTMUSEUM</type>
        </uploadRepoStable>
        <uploadRepoSnapshot>
            <name>snapshot-repo</name>
            <url>${helm.repo.url}</url>
            <type>CHARTMUSEUM</type>
        </uploadRepoSnapshot>
    </configuration>
    <executions>
        <execution>
            <id>helm-package</id>
            <phase>package</phase>
            <goals>
                <goal>dependency-build</goal>
                <goal>package</goal>
            </goals>
        </execution>
        <execution>
            <id>helm-upload</id>
            <phase>deploy</phase>
            <goals>
                <goal>upload</goal>
            </goals>
        </execution>
    </executions>
</plugin>
