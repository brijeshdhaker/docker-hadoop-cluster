helm create ./bd-spring-module/helm/bd-spring-module

helm template --debug

helm install geared-marsupi ./mychart --dry-run --debug
helm install solid-vulture ./mychart --dry-run --debug --set favoriteDrink=slurm

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
