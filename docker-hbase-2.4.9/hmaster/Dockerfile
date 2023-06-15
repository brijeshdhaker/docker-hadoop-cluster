#
# docker build -t brijeshdhaker/hbase-master:2.4.9 -f docker-hbase-2.4.9/hmaster/Dockerfile .
# docker push brijeshdhaker/hbase-master:2.4.9
#

FROM brijeshdhaker/hbase-base:2.4.9

COPY docker-hbase-2.4.9/hmaster/run.sh /run.sh
RUN chmod g+x /run.sh

EXPOSE 16000 16010

# Specify the User that the actual main process will run as
USER hbase:hbase

CMD ["/run.sh"]