#
# docker build -t brijeshdhaker/hbase-standalone:2.4.6 -f docker-hbase/standalone/Dockerfile .
#
FROM brijeshdhaker/hbase-base:1.1.1

COPY docker-hbase-1.1.1/standalone/run.sh /run.sh
RUN chmod +x /run.sh

CMD ["/run.sh"]