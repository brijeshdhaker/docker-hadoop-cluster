#
# docker build -t brijeshdhaker/hadoop-nodemanager:3.2.1 -f docker-yarn/nodemanager/Dockerfile .
# docker push brijeshdhaker/hadoop-nodemanager:3.2.1
#

FROM brijeshdhaker/yarn-base:3.2.1

MAINTAINER "Brijesh K Dhaker <brijeshdhaker@gmail.com>"

HEALTHCHECK CMD curl -f http://localhost:8042/ || exit 1

#
RUN set -x && \
    chmod 755 ${YARN_HOME}/etc/hadoop/container-executor.cfg && \
    chown -Rf root:hadoop ${YARN_HOME}/bin/container-executor && chmod 6050 ${YARN_HOME}/bin/container-executor && \
    chown -Rf root:hadoop ${YARN_HOME}/etc && chmod 755 ${YARN_HOME}/etc/hadoop && \
    echo "done."

ADD docker-yarn/nodemanager/run.sh /run.sh
RUN chown yarn:hadoop /run.sh && chmod 775 /run.sh

EXPOSE 8042

# Specify the User that the actual main process will run as
# USER yarn:hadoop

CMD ["/run.sh"]
