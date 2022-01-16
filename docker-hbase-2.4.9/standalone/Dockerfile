#
# docker build -t brijeshdhaker/hbase-standalone:2.4.9 -f docker-hbase/standalone/Dockerfile .
#
FROM brijeshdhaker/hbase-base:2.4.9

COPY docker-hbase/standalone/run.sh /run.sh
RUN chmod +x /run.sh

CMD ["/run.sh"]